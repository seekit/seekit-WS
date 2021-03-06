package com.seekit.usuario;

import java.sql.*;
import java.util.ArrayList;

import com.seekit.TRI.Tri;
import com.seekit.TRI.TriCompartido;
import com.seekit.bd.DatabaseConnection;

public class Usuario {
	// variables para la BD
	private Connection conn = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	// variables propias de un usuario
	private String idUsuario = null;
	private String nombre = null;
	private String apellido = null;
	private String mail = null;
	private String contrasenia = null;
	// el token, necesario
	private String miToken = null;

	private ArrayList<TriCompartido> arrayListTrisCompartidos = null;

	public Usuario(String nombre, String apellido, String mail,
			String contrasenia) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.mail = mail;
		this.contrasenia = contrasenia;

	}

	public Usuario(String mail, String contrasenia) {
		super();
		this.mail = mail;
		this.contrasenia = contrasenia;
	}

	public Usuario() {
		super();
	}

	public ArrayList<TriCompartido> getArrayListTrisCompartidos() {
		return arrayListTrisCompartidos;
	}

	public void setArrayListTrisCompartidos(
			ArrayList<TriCompartido> arrayListTrisCompartidos) {
		this.arrayListTrisCompartidos = arrayListTrisCompartidos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getidUsuario() {
		return idUsuario;
	}

	public void setidUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getMiToken() {
		return miToken;
	}

	public void setMiToken(String miToken) {
		this.miToken = miToken;
	}

	// metodo login: permitira al usuario chequearse contra la BD
	public boolean login() {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "SELECT * FROM usuario JOIN autenticación ON usuario.idusuario=autenticación.usuario_idusuario JOIN tokens ON usuario.idusuario=tokens.idusuario WHERE usuario.mail='"
					+ mail + "' AND autenticación.pass='" + contrasenia + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			System.out.println("funciona");
			// si este usuario existe en la BD el login puede hacerse.
			if (resultSet.next()) {
				// seteo el nombre y el apellido para el usuario en caso de que
				// este exista.
				setApellido(resultSet.getString("apellido"));
				setNombre(resultSet.getString("nombre"));
				setidUsuario(resultSet.getString("idusuario"));
				setMiToken(resultSet.getString("token"));
				return true;
				// si no existe no podta loguearse y un error aparecera.
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			closeLasCosas();

		}

	}

	public String register() {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			// en caso de que ya exista el usu paramos todo
			if (existeUsu()) {
				closeLasCosas();
				System.out.println("Esixe el usu");
				return "NOK";
			}

			// inserta el usuario en la BD
			String sql = "INSERT INTO usuario( mail, nombre, apellido) VALUES ('"
					+ mail + "','" + nombre + "','" + apellido + "')";
			System.out.println(sql);
			statement.executeUpdate(sql);

			// El tema es, con que id?? tengo que obtenerlo haciendo otra
			// sentencia sql
			resultSet = statement
					.executeQuery("SELECT MAX(idusuario) FROM usuario");

			// ahora con el ID, puedo rellenar la tabla autenticacion
			if (resultSet.next()) {
				setidUsuario(resultSet.getString(1));

				System.out.println(resultSet.getString(1));
				statement
						.executeUpdate("INSERT INTO autenticación(usuario_idusuario, mail, pass) VALUES ('"
								+ resultSet.getString(1)
								+ "','"
								+ mail
								+ "','"
								+ contrasenia + "')");

			}
			return getidUsuario();

		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NOK";
		} finally {
			closeLasCosas();
		}

	}

	// cierra las cosas reacionadas a la BD
	public void closeLasCosas() {
		System.out.println("aaaa");
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("a");
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println("b");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("c");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// metodo para saber si existe le usaurio
	public boolean existeUsu() {
		try {
			String sql = "Select * FROM usuario WHERE mail='" + mail + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				System.out.println("Este mail es unico");
				return true;

			} else {
				return false;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

	// Este metodo devolvera el array de Tris.
	public ResultSet getTrisDelusuario(String idUsuario) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "select * FROM tri where usuario_idusuario ='"
					+ idUsuario + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			return resultSet;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
		}

	}

	// va a devolver los tris que le compartieron al usuario y esta habililtados
	public ResultSet getCompartidosConmigo(String idUsuario2) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			// String sql =
			// "SELECT * FROM `tris compartidos` INNER JOIN tri ON `tris compartidos`.`tri_idtri`=tri.`idtri` WHERE `tris compartidos`.`usuario_idusuario`= '"
			// + idUsuario2 + "' AND `tris compartidos`.habilitado='1'";
			String sql = "SELECT * FROM `tris compartidos` INNER JOIN tri ON `tris compartidos`.`tri_idtri`=tri.`idtri` INNER JOIN usuario ON usuario.idusuario=`tris compartidos`.tri_usuario_idusuario WHERE `tris compartidos`.`usuario_idusuario`= '"
					+ idUsuario2 + "' AND `tris compartidos`.habilitado='1'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			return resultSet;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
		}

	}

	// para la edicion del usuario.
	public boolean editarUsuario(String passNew) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();

		try {
			statement = conn.createStatement();
			String sql;
			sql = "SELECT * FROM `autenticación` WHERE pass='" + contrasenia
					+ "' AND usuario_idusuario='" + idUsuario + "'";

			resultSet = statement.executeQuery(sql);

			if (!resultSet.next()) {
				System.out
						.println("Este usuario NO existe o los parametros no son los correctos");
				return false;

			}

			sql = "UPDATE usuario SET mail='" + mail + "',nombre='" + nombre
					+ "',apellido='" + apellido + "' WHERE idUsuario="
					+ idUsuario;

			System.out.println(sql);
			statement.executeUpdate(sql);

			if (!passNew.equals("null")) {
				sql = "UPDATE autenticación SET mail='" + mail + "', pass='"
						+ passNew + "' WHERE usuario_idusuario=" + idUsuario;
				System.out.println(sql);
				statement.executeUpdate(sql);

			} else {
				sql = "UPDATE autenticación SET mail='" + mail
						+ "' WHERE usuario_idusuario=" + idUsuario;
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

	// devuelven los aquellos usuarios a los que se les compartio un tri
	// especifico.
	public ArrayList<Usuario> triCompartido(String idTri) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		ArrayList<Usuario> resp = new ArrayList<Usuario>();

		try {
			statement = conn.createStatement();
			String sql = "SELECT usuario.idusuario, usuario.mail, usuario.nombre,usuario.apellido FROM `tris compartidos` INNER JOIN tri ON `tris compartidos`.tri_idtri=tri.idtri INNER JOIN usuario ON `tris compartidos`.usuario_idusuario=usuario.idusuario WHERE idtri='"
					+ idTri + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Usuario usuAux = new Usuario();
				usuAux.setApellido(resultSet.getString("apellido"));
				usuAux.setNombre(resultSet.getString("nombre"));
				usuAux.setidUsuario(resultSet.getString("idusuario"));
				usuAux.setMail(resultSet.getString("mail"));

				resp.add(usuAux);

			}

			return resp;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		} finally {

			closeLasCosas();

		}

	}

	// devuelve aquellos usuarios que quieran compartir un tri a el usuario
	// logueado.
	public ArrayList<TriCompartido> notificaciones() {

		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		// ArrayList<Usuario> resp = new ArrayList<Usuario>();

		try {
			statement = conn.createStatement();
			String sql = "SELECT usuario.idusuario, usuario.mail, usuario.nombre,usuario.apellido, tri.nombre,`tris compartidos`.comentario,tri.identificador, tri.idtri,tri.foto FROM `tris compartidos` INNER JOIN tri ON `tris compartidos`.tri_idtri=tri.idtri INNER JOIN usuario ON `tris compartidos`.tri_usuario_idusuario=usuario.idusuario WHERE `tris compartidos`.usuario_idusuario='"
					+ idUsuario + "' AND `tris compartidos`.habilitado='0'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			/*
			 * String idUsuAux = "-1"; ArrayList<TriCompartido> arrayAuxTris =
			 * null; Usuario usuAux = null; TriCompartido triAux = null;
			 */

			ArrayList<TriCompartido> resp = new ArrayList<TriCompartido>();
			TriCompartido triAux = null;
			while (resultSet.next()) {
				triAux = new TriCompartido();
				triAux.setIdTri(resultSet.getString("idtri"));
				triAux.setFoto(resultSet.getString("foto"));
				triAux.setIdentificador(resultSet.getString("identificador"));
				triAux.setComentario(resultSet.getString("comentario"));
				triAux.setNombre(resultSet.getString(5));
				triAux.setIdUsuario(resultSet.getString("idusuario"));
				triAux.setNombreUsuario(resultSet.getString(3));
				triAux.setApellidoUsuario(resultSet.getString("apellido"));
				triAux.setMailUsuario(resultSet.getString("mail"));
				resp.add(triAux);

			}

			/*
			 * while (resultSet.next()) { if (idUsuAux == "-1") { arrayAuxTris =
			 * new ArrayList<TriCompartido>(); usuAux = new Usuario();
			 * usuAux.setApellido(resultSet.getString("apellido"));
			 * usuAux.setNombre(resultSet.getString("nombre"));
			 * usuAux.setidUsuario(resultSet.getString("idusuario"));
			 * usuAux.setMail(resultSet.getString("mail"));
			 * 
			 * }
			 * 
			 * triAux = new TriCompartido();
			 * triAux.setIdTri(resultSet.getString("idtri"));
			 * triAux.setFoto(resultSet.getString("foto"));
			 * triAux.setIdentificador(resultSet.getString("identificador"));
			 * triAux.setComentario(resultSet.getString("comentario"));
			 * triAux.setNombre(resultSet.getString("nombre"));
			 * 
			 * if (idUsuAux.equals(resultSet.getString("idusuario")) ||
			 * idUsuAux.equals("-1")) { arrayAuxTris.add(triAux);
			 * usuAux.setArrayListTrisCompartidos(arrayAuxTris); idUsuAux =
			 * resultSet.getString("idusuario"); if (resultSet.isLast()) {
			 * 
			 * resp.add(usuAux); }
			 * 
			 * } else { resp.add(usuAux); usuAux = new Usuario();
			 * usuAux.setApellido(resultSet.getString("apellido"));
			 * usuAux.setNombre(resultSet.getString("nombre"));
			 * usuAux.setidUsuario(resultSet.getString("idusuario"));
			 * usuAux.setMail(resultSet.getString("mail"));
			 * usuAux.setidUsuario(resultSet.getString("idusuario"));
			 * 
			 * arrayAuxTris = new ArrayList<TriCompartido>();
			 * arrayAuxTris.add(triAux);
			 * usuAux.setArrayListTrisCompartidos(arrayAuxTris);
			 * 
			 * idUsuAux = resultSet.getString("idusuario"); if
			 * (resultSet.isLast()) { resp.add(usuAux); } }
			 * 
			 * }
			 */
			return resp;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		} finally {

			closeLasCosas();

		}

	}

	public boolean eliminarUsuario() {

		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		String sql = null;
		boolean ejecutar = false;
		try {
			statement = conn.createStatement();
			// borro los tris que le compartieron
			sql = "SELECT tri_usuario_idusuario FROM `tris compartidos` WHERE `tri_usuario_idusuario`='"
					+ idUsuario + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			sql = "DELETE FROM `tris compartidos` WHERE `tri_usuario_idusuario` IN (";
			while (resultSet.next()) {
				sql = sql.concat(resultSet.getString("tri_usuario_idusuario"));
				if (!resultSet.isLast()) {
					sql = sql.concat(",");
				}

				ejecutar = true;

			}
			sql = sql.concat(")");
			System.out.println(sql);
			if (ejecutar) {
				statement.executeUpdate(sql);
			}
			ejecutar = false;
			// borro los tris que compartio (al revez es pero ta)
			sql = "SELECT usuario_idusuario FROM `tris compartidos` WHERE `usuario_idusuario`='"
					+ idUsuario + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			sql = "DELETE FROM `tris compartidos` WHERE `usuario_idusuario` IN (";
			while (resultSet.next()) {
				sql = sql.concat(resultSet.getString("usuario_idusuario"));
				if (!resultSet.isLast()) {
					sql = sql.concat(",");
				}
				ejecutar = true;
			}
			sql = sql.concat(")");
			System.out.println(sql);
			if (ejecutar) {
				statement.executeUpdate(sql);
			}
			ejecutar = false;
			// si marcon uno como perdido, desaparece.
			sql = "SELECT tri_idtri FROM `tris perdidos` WHERE tri_usuario_idusuario='"
					+ idUsuario + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			sql = "DELETE FROM `tris perdidos` WHERE tri_idtri IN (";

			while (resultSet.next()) {
				sql = sql.concat(resultSet.getString("tri_idtri"));
				if (!resultSet.isLast()) {
					sql = sql.concat(",");

				}
				ejecutar = true;
			}
			sql = sql.concat(")");
			System.out.println(sql);
			if (ejecutar) {
				statement.executeUpdate(sql);
			}
			ejecutar = false;

			// borros los tris pertenencientes al usuario
			sql = "SELECT idtri,identificador FROM tri WHERE usuario_idusuario='" + idUsuario
					+ "'";
			System.out.println(sql);

			resultSet = statement.executeQuery(sql);
			sql = "DELETE FROM tri WHERE idtri IN (";
			String sql2 = "UPDATE `trisfabricados` SET `usado`='0' WHERE `identificador` IN ('";
			while (resultSet.next()) {
				sql = sql.concat(resultSet.getString("idtri"));
				sql2=sql2.concat(resultSet.getString("identificador"));
				if (!resultSet.isLast()) {
					sql = sql.concat(",");
					sql2=sql2.concat("','");
				}
				ejecutar = true;
			}
			sql = sql.concat(")");
			sql2=sql2.concat("')");
			System.out.println(sql);
			System.out.println(sql2);
			if (ejecutar) {
				statement.executeUpdate(sql);
				
				statement.executeUpdate(sql2);
			}
			ejecutar = false;

			// elimino la contrasenia de la tabla autenticacion
			sql = "DELETE FROM `autenticación` WHERE usuario_idusuario='"
					+ idUsuario + "'";
			System.out.println(sql);
			statement.executeUpdate(sql);

			// elimino el usuario mismo
			sql = "DELETE FROM usuario WHERE idusuario IN (" + idUsuario + ")";
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

	// este metodo debe devolver todos los tris que se encontraron y
	// desPerderlos en la base de datos
	public ArrayList<Tri> seEncontroAlgunTriPerdidoMio() {

		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		ArrayList<Tri> resp = new ArrayList<Tri>();
		ArrayList<String> listaTris = new ArrayList<String>();

		try {
			statement = conn.createStatement();
			String sql = "SELECT * FROM tri WHERE perdido='2' AND usuario_idusuario='"
					+ idUsuario + "'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Tri triAux = new Tri();
				triAux.setIdTri(resultSet.getString("idtri"));
				triAux.setFoto(resultSet.getString("foto"));
				triAux.setIdentificador(resultSet.getString("identificador"));
				triAux.setNombre(resultSet.getString("nombre"));
				triAux.setLatitud(resultSet.getString("latitud"));
				triAux.setLongitud(resultSet.getString("longitud"));
				resp.add(triAux);
				listaTris.add(resultSet.getString("idtri"));

			}
			//si es 0 significa que ese usario no tiene tris perdidos
			if (listaTris.size() != 0) {

				sql = "UPDATE `tri` SET perdido='0' WHERE `idtri` IN (";
				for (int i = 0; i < listaTris.size(); i++) {
					sql = sql.concat(listaTris.get(i));
					if (i < listaTris.size() - 1) {
						sql = sql.concat(",");
					}
				}
				sql = sql.concat(")");
				System.out.println(sql);
				statement.executeUpdate(sql);

				sql = "DELETE FROM `tris perdidos` WHERE `tri_idtri` IN (";

				for (int i = 0; i < listaTris.size(); i++) {
					sql = sql.concat(listaTris.get(i));
					if (i < listaTris.size() - 1) {
						sql = sql.concat(",");
					}
				}
				sql = sql.concat(")");
				System.out.println(sql);
				statement.executeUpdate(sql);
			}
			return resp;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		} finally {

			closeLasCosas();

		}

	}
}
