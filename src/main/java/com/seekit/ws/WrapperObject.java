package com.seekit.ws;

import java.util.List;

import com.seekit.TRI.Tri;
import com.seekit.usuario.Usuario;

public class WrapperObject {
	Usuario usuario;
	List<Tri> listaTris;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Tri> getListaTris() {
		return listaTris;
	}

	public void setListaTris(List<Tri> listaTris) {
		this.listaTris = listaTris;
	}

}
