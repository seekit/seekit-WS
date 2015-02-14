package com.seekit.TRI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

	public void setFoto(String foto) {
		this.foto = foto;
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

	public boolean addTri(String idUsuario) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();

		try {
			statement = conn.createStatement();
			String sql = "INSERT INTO tri(usuario_idusuario, identificador, nombre, foto, activo, latitud, longitud, perdido, compartido) VALUES ('"
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
					+ "','0','0')";
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

	public boolean editarTri() {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "UPDATE tri SET identificador='" + identificador
					+ "',nombre='" + nombre + "',foto='" + foto
					+ "' WHERE idTri=" + idTri;
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
		try {
			statement = conn.createStatement();
			String sql = "DELETE FROM tri WHERE idtri='" + idTri + "'";
			System.out.println(sql);
			statement.executeUpdate(sql);
			sql = "DELETE FROM `tris compartidos` WHERE tri_usuario_idusuario IN ("
					+ idTri + ")";
			System.out.println(sql);
			statement.executeUpdate(sql);
			sql = "DELETE FROM `tris compartidos` WHERE usuario_idusuario IN ("
					+ idTri + ")";
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
			ArrayList<String> listaAux) {

		ArrayList<String> listaIdTrisUsuario = new ArrayList<String>();
		ArrayList<String> listaTrisDelUsuaioOrigenEncontrados = new ArrayList<String>();
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "SELECT idtri FROM `tri` WHERE usuario_idusuario='"
					+ idUsuarioOrigen + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				listaIdTrisUsuario.add(resultSet.getString("idtri"));
			}

			for (int i = 0; i < listaAux.size(); i++) {
				System.out.println(i + " - " + listaAux.size());
				for (int j = 0; j < listaIdTrisUsuario.size(); j++) {
					System.out.println(j + " - " + listaIdTrisUsuario.size());
					if ((listaAux.get(i)).equals((listaIdTrisUsuario).get(j))) {
						System.out.println(listaAux.get(i));
						listaTrisDelUsuaioOrigenEncontrados
								.add(listaAux.get(i));
					}

				}
			}

			sql = "UPDATE tri SET latitud='" + latitud + "',longitud = '"
					+ longitud + "' WHERE idtri IN (";
			for (int i = 0; i < listaTrisDelUsuaioOrigenEncontrados.size(); i++) {

				sql = sql.concat(listaTrisDelUsuaioOrigenEncontrados.get(i));

				if (i < listaTrisDelUsuaioOrigenEncontrados.size() - 1) {
					sql = sql.concat(",");
				}

			}
			sql = sql.concat(")");
			System.out.println(sql);
			statement.executeUpdate(sql);

			sql = "UPDATE `tris perdidos` SET latitud='" + latitud
					+ "',longitud = '" + longitud + "' WHERE tri_idtri IN (";
			for (int i = 0; i < listaAux.size(); i++) {
				sql = sql.concat(listaAux.get(i));
				if (i < listaAux.size() - 1) {
					sql = sql.concat(",");
				}

			}
			sql = sql.concat(")");
			System.out.println(sql);
			statement.executeUpdate(sql);

			sql = "UPDATE tri SET perdido='2' WHERE perdido='1' AND idtri IN (";
			for (int i = 0; i < listaAux.size(); i++) {
				sql = sql.concat(listaAux.get(i));
				if (i < listaAux.size() - 1) {
					sql = sql.concat(",");
				}

			}
			sql = sql.concat(")");

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

}
