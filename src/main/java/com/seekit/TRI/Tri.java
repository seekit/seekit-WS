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
	String localizacion = null;
	int perdido = 0;
	int compartido = 0;

	public Tri(String identificador, String nombre, String foto, int activo,
			String localizacion, int perdido, int compartido) {
		super();
		this.identificador = identificador;
		this.nombre = nombre;
		this.foto = foto;
		this.activo = activo;
		this.localizacion = localizacion;
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

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
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

	public boolean addTri(String idUsuario) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();

		try {
			statement = conn.createStatement();
			String sql = "INSERT INTO tri(usuario_idusuario, identificador, nombre, foto, activo, location, perdido, compartido) VALUES ('"
					+ idUsuario
					+ "','"
					+ identificador
					+ "','"
					+ nombre
					+ "','" + foto + "','1','" + localizacion + "','0','0')";
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

	public boolean markAsLost(String idUsuario) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			String sql = "INSERT INTO `tris perdidos`(tri_idtri, tri_usuario_idusuario, identificador, location) VALUES ('"
					+ idTri
					+ "','"
					+ idUsuario
					+ "','"
					+ identificador
					+ "','null')";
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

	

}
