package com.seekit.TRI;
//http://192.168.1.43:8080/seekit/seekit/triCompartido?idTri=50
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.seekit.bd.DatabaseConnection;

public class TriCompartido {

	// variables para la BD
	private Connection conn = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	// variables de la clase
	String idTri = null;
	String identificador = null;
	String nombre = null;
	String foto = null;
	int activo = 1;
	String latitud = null;
	String longitud = null;
	int perdido = 0;
	int compartido = 0;
	String idUsuarioPropietario = null;
	String habilitado = "0";
	String comentario = null;
	String descripcion = null;

	// usuario propietario de este TRI
	String idUsuario = null;
	String nombreUsuario = null;
	String apellidoUsuario = null;
	String mailUsuario = null;

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getApellidoUsuario() {
		return apellidoUsuario;
	}

	public void setApellidoUsuario(String apellidoUsuario) {
		this.apellidoUsuario = apellidoUsuario;
	}

	public String getMailUsuario() {
		return mailUsuario;
	}

	public void setMailUsuario(String mailUsuario) {
		this.mailUsuario = mailUsuario;
	}

	public TriCompartido() {
		super();
	}

	public String getIdTri() {
		return idTri;
	}

	public void setIdTri(String idTri) {
		this.idTri = idTri;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
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

	public String getIdUsuarioPropietario() {
		return idUsuarioPropietario;
	}

	public void setIdUsuarioPropietario(String idUsuarioPropietario) {
		this.idUsuarioPropietario = idUsuarioPropietario;
	}

	public String getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(String habilitado) {
		this.habilitado = habilitado;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public boolean descompartirTri(String idUsuarioCompartido) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();

		try {
			statement = conn.createStatement();
			String sql = "DELETE FROM `tris compartidos` WHERE `usuario_idusuario`='"
					+ idUsuarioCompartido + "' AND `tri_idtri`='" + idTri + "'";
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

	public boolean confirmarTri(String confirmar) {
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();

		try {
			statement = conn.createStatement();
			String sql = null;
			if (confirmar.equals("1")) {
				sql = "UPDATE `tris compartidos` SET habilitado='1' WHERE usuario_idusuario='"
						+ idUsuario
						+ "' AND tri_idtri='"
						+ idTri
						+ "' AND tri_usuario_idusuario='"
						+ idUsuarioPropietario + "'";
			} else {
				sql = "DELETE FROM `tris compartidos` WHERE usuario_idusuario='"
						+ idUsuario
						+ "' AND tri_idtri='"
						+ idTri
						+ "' AND tri_usuario_idusuario='"
						+ idUsuarioPropietario + "'";
			}

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

}
