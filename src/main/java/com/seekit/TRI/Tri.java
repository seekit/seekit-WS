package com.seekit.TRI;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.aop.BeforeAdvice;

import com.seekit.bd.DatabaseConnection;

public class Tri {
	// variables para la BD
	private Connection conn = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	// variable de la clase
	String idTri = null;
	String identificador = null;
	String nombre = null;
	String foto = null;
	int activo = 1;
	String latitud = null;
	String longitud = null;
	String descripcion = null;
	int perdido = 0;
	int compartido = 0;

	public Tri(String identificador, String nombre, String foto, int activo,
			String latitud, int perdido, int compartido) {
		super();
		this.identificador = identificador;
		this.nombre = nombre;
		this.foto = foto;
		this.activo = activo;
		this.latitud = latitud;
		this.perdido = perdido;
		this.compartido = compartido;
	}

	public Tri(String identificador, String nombre, String foto) {
		super();
		this.identificador = identificador;
		this.nombre = nombre;
		this.foto = foto;
	}

	public Tri(String idTri) {
		this.idTri = idTri;
	}

	public Tri() {

	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getIdTri() {
		return idTri;
	}

	public void setIdTri(String idTri) {
		this.idTri = idTri;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto2) {
		this.foto = foto2;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public int getPerdido() {
		return perdido;
	}

	public void setPerdido(int perdido) {
		this.perdido = perdido;
	}

	public int getCompartido() {
		return compartido;
	}

	public void setCompartido(int compartido) {
		this.compartido = compartido;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String addTri(String idUsuario) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		String res = "-1";

		try {

			statement = conn.createStatement();
			String triFabricado = chequearExistenciaDelTriYUso(statement);
			if (triFabricado.equals("usado")) {
				return "usado";

			}
			if (triFabricado.equals("inexistente")) {
				return "inexistente";

			}
			if (triFabricado.equals("libre")) {
				// updateo el valor usado en la tabla trisfabricados
				String sql = "UPDATE `trisfabricados` SET `usado`='1' WHERE `identificador`= '"
						+ identificador + "'";
				System.out.println(sql);
				statement.executeUpdate(sql);
				// y lo agrego a la de tris
				sql = "INSERT INTO tri(usuario_idusuario, identificador, nombre, foto, activo, latitud, longitud, perdido, compartido, descripcion) VALUES ('"
						+ idUsuario
						+ "','"
						+ identificador
						+ "','"
						+ nombre
						+ "','"
						+ foto
						+ "','1','"
						+ latitud
						+ "','"
						+ longitud
						+ "','0','0','" + descripcion + "')";
				System.out.println(sql);
				statement.executeUpdate(sql);
				// ahora tengo que hacer un select para devolver el idtri
				resultSet = statement
						.executeQuery("SELECT MAX(idtri) FROM tri");
				if (resultSet.next()) {
					res = resultSet.getString(1);

				}
			}
			return res;
		} catch (SQLException e) {

			e.printStackTrace();
			return res;
		} finally {
			closeLasCosas();

		}

	}

	private String chequearExistenciaDelTriYUso(Statement statement) {
		String sql = "SELECT * FROM trisfabricados WHERE identificador ='"
				+ identificador + "'";
		System.out.println(sql);

		// ahora tengo que hacer un select para devolver el idtri
		try {
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String uso = resultSet.getString("usado");
				if (uso.equals("1")) {
					return "usado";
				} else {
					return "libre";
				}

			} else {
				return "inexistente";
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "-1";
		}

	}

	void closeLasCosas() {

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {

			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {

			}
		}

	}

	public boolean marcarComoPerdido(String idUsuario) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			// primero insertamos, si esto sale bien updateamos la fila del tri
			// perdido
			String sql = "INSERT INTO `tris perdidos`(tri_idtri, tri_usuario_idusuario, identificador, latitud , longitud ) VALUES ('"
					+ idTri
					+ "','"
					+ idUsuario
					+ "','"
					+ identificador
					+ "','null','null')";
			System.out.println(sql);
			int resp = statement.executeUpdate(sql);

			if (resp == 0) {
				return false;
			} else {
				sql = "UPDATE tri SET perdido='1' WHERE idtri='" + idTri + "'";

				System.out.println(sql);

				resp = statement.executeUpdate(sql);

				System.out.println(resp);
				if (resp == 0) {
					return false;
				} else {
					return true;
				}

			}

		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		} finally {
			closeLasCosas();

		}

	}

	public boolean compartir(String mailUsuACompartir, String idUsuario,
			String habilitado, String comentario) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		// primero tengo que obtener el id del usuario destino.
		try {
			statement = conn.createStatement();
			String sql = "SELECT idusuario FROM usuario WHERE mail='"
					+ mailUsuACompartir + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String usuarioReceptor = resultSet.getString("idusuario");
				sql = "INSERT INTO `tris compartidos`(usuario_idusuario, tri_idtri, tri_usuario_idusuario, habilitado, comentario) VALUES ('"
						+ usuarioReceptor
						+ "','"
						+ idTri
						+ "','"
						+ idUsuario
						+ "','" + habilitado + "','" + comentario + "')";
				System.out.println(sql);
				statement.executeUpdate(sql);
			}

			return true;
		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		} finally {
			closeLasCosas();
		}

	}

	public String editarTri() {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "SELECT * FROM `trisfabricados` WHERE identificador='"
					+ identificador + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				return "inexistente";

			} else {
				String uso = resultSet.getString("usado");
				if (uso.equals("1")) {
					return "usado";

				}
			}

			sql = "SELECT identificador FROM `tri` WHERE idTri='" + idTri + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String id = resultSet.getString("identificador");
				sql = "UPDATE `trisfabricados` SET `usado`='0' WHERE `identificador`='"
						+ id + "'";
				statement.executeUpdate(sql);
			}
			sql = "UPDATE tri SET identificador='" + identificador
					+ "',nombre='" + nombre + "',foto='" + foto
					+ "', descripcion= '" + descripcion + "' WHERE idTri="
					+ idTri;
			System.out.println(sql);
			statement.executeUpdate(sql);

			sql = "UPDATE `trisfabricados` SET `usado`='1' WHERE `identificador`='"
					+ identificador + "'";
			System.out.println(sql);
			statement.executeUpdate(sql);
			return "OK";
		} catch (SQLException e) {
			e.printStackTrace();
			return "-1";
		} finally {
			closeLasCosas();
		}
	}

	public boolean desmarcarComoPerdido(String idUsuario) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "DELETE FROM `tris perdidos` WHERE `tri_idtri`='"
					+ idTri + "' AND `tri_usuario_idusuario`='" + idUsuario
					+ "' ";
			System.out.println(sql);
			int resp = statement.executeUpdate(sql);
			System.out.println(resp);
			if (resp == 0) {

				return false;
			} else {

				sql = "UPDATE tri SET perdido='0' WHERE idtri='" + idTri + "'";

				System.out.println(sql);

				resp = statement.executeUpdate(sql);

				System.out.println(resp);
				if (resp == 0) {
					return false;
				} else {
					return true;
				}

			}

		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		} finally {
			closeLasCosas();

		}
	}

	public boolean seEncontroTriPerdido() {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "DELETE FROM `tris perdidos` WHERE `tri_idtri`='"
					+ idTri + "'";

			System.out.println(sql);

			int resp = statement.executeUpdate(sql);

			System.out.println(resp);
			if (resp == 0) {
				return false;
			} else {
				sql = "UPDATE tri SET latitud='" + latitud + "',longitud='"
						+ longitud + "' ,perdido='2' WHERE idtri='" + idTri
						+ "'";
				System.out.println(sql);

				resp = statement.executeUpdate(sql);

				System.out.println(resp);
				if (resp == 0) {
					return false;
				} else {
					return true;
				}

			}
		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		} finally {
			closeLasCosas();

		}

	}

	public boolean eliminarTri() {

		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		boolean ejecutar = false;
		try {
			statement = conn.createStatement();
			//
			String sql = "SELECT * FROM `tris compartidos` WHERE `tri_idtri`='"
					+ idTri + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				sql = "DELETE FROM `tris compartidos` WHERE `tri_idtri` ='"
						+ idTri + "'";
				System.out.println(sql);
				ejecutar = true;

			}

			if (ejecutar) {
				statement.executeUpdate(sql);
				sql = "UPDATE `trisfabricados` SET `usado`='0' WHERE `idTri`= '"
						+ idTri + "'";
				statement.executeUpdate(sql);
			}
			ejecutar = false;

			// si marcon uno como perdido, desaparece.
			sql = "SELECT * FROM `tris perdidos` WHERE tri_idtri='" + idTri
					+ "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				sql = "DELETE FROM `tris perdidos` WHERE tri_idtri ='" + idTri
						+ "'";
				System.out.println(sql);
				ejecutar = true;
			}

			if (ejecutar) {
				statement.executeUpdate(sql);
			}
			ejecutar = false;

			//
			sql = "DELETE FROM tri WHERE idtri='" + idTri + "'";
			System.out.println(sql);
			statement.executeUpdate(sql);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeLasCosas();
		}

	}

	public boolean actualizarGPS(String idUsuarioOrigen,
			ArrayList<String> listaTrisEncontradosPorElCelular) {

		ArrayList<String> listaIdTrisUsuario = new ArrayList<String>();
		ArrayList<String> listaTrisDelUsuaioOrigenEncontradosPorElCelular = new ArrayList<String>();
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			// primero selecciono todos los tris del usuario
			String sql = "SELECT idtri FROM `tri` WHERE usuario_idusuario='"
					+ idUsuarioOrigen + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {

				resultSet.beforeFirst();
				while (resultSet.next()) {
					// agrego esos tris a una lista.
					listaIdTrisUsuario.add(resultSet.getString("idtri"));
					System.out.println("tri del usuarioorigen="
							+ resultSet.getString("idtri"));
				}

				// recorro la lista de tris que encontro el celular y le envio
				// al
				// servidor.
				for (int i = 0; i < listaTrisEncontradosPorElCelular.size(); i++) {
					System.out.println("tris encntrados por el celular="
							+ listaTrisEncontradosPorElCelular.get(i));
					for (int j = 0; j < listaIdTrisUsuario.size(); j++) {

						// SOLO se actualizara el GPS de los tris del usuario de
						// origen (no los compartidos ni los de otros)
						if ((listaTrisEncontradosPorElCelular.get(i))
								.equals((listaIdTrisUsuario).get(j))) {

							listaTrisDelUsuaioOrigenEncontradosPorElCelular
									.add(listaTrisEncontradosPorElCelular
											.get(i));
						}

					}
				}
				if (listaTrisDelUsuaioOrigenEncontradosPorElCelular.size() != 0) {

					// se updatea la latitud y longitud de los tris encontrados
					// pertencientes al usuario
					sql = "UPDATE tri SET latitud='" + latitud
							+ "',longitud = '" + longitud
							+ "' WHERE idtri IN (";
					for (int i = 0; i < listaTrisDelUsuaioOrigenEncontradosPorElCelular
							.size(); i++) {
						System.out
								.println("Tris cuya ubicacion fue updateado="
										+ listaTrisDelUsuaioOrigenEncontradosPorElCelular
												.get(i));
						sql = sql
								.concat(listaTrisDelUsuaioOrigenEncontradosPorElCelular
										.get(i));

						if (i < listaTrisDelUsuaioOrigenEncontradosPorElCelular
								.size() - 1) {
							sql = sql.concat(",");
						}

					}
					sql = sql.concat(")");
					System.out.println(sql);
					statement.executeUpdate(sql);
				} else {

				}
			} else {

			}

			// primero obtengo todos los tris perdidos.
			sql = "SELECT tri_idtri FROM `tris perdidos`";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			// si hay tris perdidos, prosigo.
			if (resultSet.next()) {
				resultSet.beforeFirst();
				ArrayList<String> idTrisPerdidos = new ArrayList<String>();
				while (resultSet.next()) {
					// agrego esos tris a una lista.
					idTrisPerdidos.add(resultSet.getString("tri_idtri"));
					System.out.println("lista de tris perdidos="
							+ resultSet.getString("tri_idtri"));
				}
				// chequeo cuales de los tris perdidos, fueron encontrados por
				// el celular.
				ArrayList<String> idTrisPerdidosEncontradosPorElCelular = new ArrayList<String>();
				for (int i = 0; i < idTrisPerdidos.size(); i++) {
					for (int j = 0; j < listaTrisEncontradosPorElCelular.size(); j++) {

						if (idTrisPerdidos.get(i).equals(
								listaTrisEncontradosPorElCelular.get(j))) {
							System.out
									.println("lista de tris perdidos que encontro el cel="
											+ idTrisPerdidos.get(i));
							idTrisPerdidosEncontradosPorElCelular
									.add(idTrisPerdidos.get(i));
						}

					}

				}

				if (idTrisPerdidosEncontradosPorElCelular.size() != 0) {

					// se updatea la latitud y longitud de aqellos tris que
					// estan en
					// la
					// tabla trisPerdidos
					sql = "UPDATE `tris perdidos` SET latitud='" + latitud
							+ "',longitud = '" + longitud
							+ "' WHERE tri_idtri IN (";
					for (int i = 0; i < idTrisPerdidosEncontradosPorElCelular
							.size(); i++) {
						sql = sql.concat(idTrisPerdidosEncontradosPorElCelular
								.get(i));
						if (i < idTrisPerdidosEncontradosPorElCelular.size() - 1) {
							sql = sql.concat(",");
						}

					}
					sql = sql.concat(")");
					System.out.println(sql);
					statement.executeUpdate(sql);
					// se updatea la latitud y longitud de aqellos tris que
					// estan en
					// la
					// tabla tris
					sql = "UPDATE `tri` SET latitud='" + latitud
							+ "',longitud = '" + longitud
							+ "' WHERE idtri IN (";
					for (int i = 0; i < idTrisPerdidosEncontradosPorElCelular
							.size(); i++) {
						sql = sql.concat(idTrisPerdidosEncontradosPorElCelular
								.get(i));
						if (i < idTrisPerdidosEncontradosPorElCelular.size() - 1) {
							sql = sql.concat(",");
						}

					}
					sql = sql.concat(")");
					System.out.println(sql);
					statement.executeUpdate(sql);

					// se updatea el valor perdido de la tabla TRI, de esta
					// manera
					// el
					// tri pasa a estar encontrado.
					sql = "UPDATE tri SET perdido='2' WHERE perdido='1' AND idtri IN (";
					for (int i = 0; i < idTrisPerdidosEncontradosPorElCelular
							.size(); i++) {
						sql = sql.concat(idTrisPerdidosEncontradosPorElCelular
								.get(i));
						if (i < idTrisPerdidosEncontradosPorElCelular.size() - 1) {
							sql = sql.concat(",");
						}

					}
					sql = sql.concat(")");

					System.out.println(sql);
					statement.executeUpdate(sql);

					return true;
				} else {
					// hay tris perdidos, pero el celular no enontro ninguno.
					return true;
				}
			} else {
				// no hay tris perdidos pero el metodo anduvo OK
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeLasCosas();
		}

	}

	public Tri obtenerUbicacion() {

		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "SELECT * FROM tri WHERE idtri= '" + idTri + "'";

			System.out.println(sql);

			resultSet = statement.executeQuery(sql);
			Tri tri = new Tri();
			while (resultSet.next()) {
				System.out.println("En el while");

				tri.setIdentificador(resultSet.getString("identificador"));
				tri.setNombre(resultSet.getString("nombre"));
				tri.setFoto(resultSet.getString("foto"));
				tri.setActivo(resultSet.getInt("activo"));
				tri.setLatitud(resultSet.getString("latitud"));
				tri.setLongitud(resultSet.getString("longitud"));
				tri.setPerdido(resultSet.getInt("perdido"));
				tri.setCompartido(resultSet.getInt("compartido"));
				tri.setIdTri(resultSet.getString("idtri"));
				tri.setDescripcion(resultSet.getString("descripcion"));

			}

			return tri;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		} finally {
			closeLasCosas();

		}

	}

}
