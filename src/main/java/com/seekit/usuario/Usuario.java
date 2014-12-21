package com.seekit.usuario;

import java.sql.*;
import java.util.ArrayList;

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

	// metodo login: permitira al usuario chequearse contra la BD
	public boolean login() {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "SELECT * FROM usuario JOIN autenticación ON usuario.idusuario=autenticación.usuario_idusuario WHERE usuario.mail='"
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

	public boolean register() {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			// en caso de que ya exista el usu paramos todo
			if (existeUsu()) {
				closeLasCosas();
				System.out.println("Esixe el usu");
				return false;
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
			return true;

		} catch (SQLException e) {
			System.out.println("A");
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			closeLasCosas();
		}

	}

	// cierra las cosas reacionadas a la BD
	public void closeLasCosas() {
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
	public boolean editarUsuario() {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "UPDATE usuario SET mail='" + mail + "',nombre='"
					+ nombre + "',apellido='" + apellido + "' WHERE idUsuario="
					+ idUsuario;
			System.out.println(sql);
			statement.executeUpdate(sql);
			sql = "UPDATE autenticación SET mail='" + mail + "',pass='"
					+ contrasenia + "' WHERE usuario_idusuario=" + idUsuario;
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
		//ArrayList<Usuario> resp = new ArrayList<Usuario>();

		try {
			statement = conn.createStatement();
			String sql = "SELECT usuario.idusuario, usuario.mail, usuario.nombre,usuario.apellido, tri.nombre,`tris compartidos`.comentario,tri.identificador, tri.idtri,tri.foto FROM `tris compartidos` INNER JOIN tri ON `tris compartidos`.tri_idtri=tri.idtri INNER JOIN usuario ON `tris compartidos`.tri_usuario_idusuario=usuario.idusuario WHERE `tris compartidos`.usuario_idusuario='"
					+ idUsuario + "' AND `tris compartidos`.habilitado='0'";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			/*String idUsuAux = "-1";
			ArrayList<TriCompartido> arrayAuxTris = null;
			Usuario usuAux = null;
			TriCompartido triAux = null;*/
			
			ArrayList<TriCompartido> resp = new ArrayList<TriCompartido>();
			TriCompartido triAux = null;
	while(resultSet.next()){
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
			
			
			/*		while (resultSet.next()) {
				if (idUsuAux == "-1") {
					arrayAuxTris = new ArrayList<TriCompartido>();
					usuAux = new Usuario();
					usuAux.setApellido(resultSet.getString("apellido"));
					usuAux.setNombre(resultSet.getString("nombre"));
					usuAux.setidUsuario(resultSet.getString("idusuario"));
					usuAux.setMail(resultSet.getString("mail"));

				}

				triAux = new TriCompartido();
				triAux.setIdTri(resultSet.getString("idtri"));
				triAux.setFoto(resultSet.getString("foto"));
				triAux.setIdentificador(resultSet.getString("identificador"));
				triAux.setComentario(resultSet.getString("comentario"));
				triAux.setNombre(resultSet.getString("nombre"));

				if (idUsuAux.equals(resultSet.getString("idusuario"))
						|| idUsuAux.equals("-1")) {
					arrayAuxTris.add(triAux);
					usuAux.setArrayListTrisCompartidos(arrayAuxTris);
					idUsuAux = resultSet.getString("idusuario");
					if (resultSet.isLast()) {

						resp.add(usuAux);
					}

				} else {
					resp.add(usuAux);
					usuAux = new Usuario();
					usuAux.setApellido(resultSet.getString("apellido"));
					usuAux.setNombre(resultSet.getString("nombre"));
					usuAux.setidUsuario(resultSet.getString("idusuario"));
					usuAux.setMail(resultSet.getString("mail"));
					usuAux.setidUsuario(resultSet.getString("idusuario"));

					arrayAuxTris = new ArrayList<TriCompartido>();
					arrayAuxTris.add(triAux);
					usuAux.setArrayListTrisCompartidos(arrayAuxTris);

					idUsuAux = resultSet.getString("idusuario");
					if (resultSet.isLast()) {
						resp.add(usuAux);
					}
				}

			}
*/
			return resp;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		} finally {

			closeLasCosas();

		}

	}
}
