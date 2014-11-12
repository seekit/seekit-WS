package com.seekit.usuario;

import java.sql.*;

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
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
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

	public boolean editarusuario() {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "";
			System.out.println(sql);
			resultSet = statement.executeQuery(sql);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {

		}

	}
}
