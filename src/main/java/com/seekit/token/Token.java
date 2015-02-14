package com.seekit.token;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.seekit.bd.DatabaseConnection;

public class Token {
	// variables para la BD
		private Connection conn = null;
		private Statement statement = null;
		private ResultSet resultSet = null;
		//variable de la clase
String idToken=null;
String token=null;
String idUsuario=null;
	
	
	
	public Token(){
		
	}



	public String getIdToken() {
		return idToken;
	}



	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}



	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	public String getIdUsuario() {
		return idUsuario;
	}



	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}




	public boolean chequearToken() {
			
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			
			String sql = "SELECT * FROM tokens WHERE token = '"+this.getToken()+"' AND idusuario='"+idUsuario+"'";
			
			System.out.println(sql);
			resultSet=statement.executeQuery(sql);

			if (resultSet.next()) {
				return true;
			}else{
				return false;	
			}
			

		} catch (SQLException e) {
			
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



	public boolean crearToken() {

		  
		DatabaseConnection dbc = new DatabaseConnection();
		conn = dbc.getConection();
		try {
			statement = conn.createStatement();
			
			String sql = "INSERT INTO tokens(token, idusuario) VALUES ('"+token+"' , '"+idUsuario+"')";
			
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

