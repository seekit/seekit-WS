package com.seekit.ws;

import java.util.ArrayList;
import java.util.List;

import com.seekit.TRI.Tri;
import com.seekit.TRI.TriCompartido;

public class WrapperTrisTrisCompartidos {

	List<Tri> listaTri = null;
	List<TriCompartido> listaTriCompartido = null;

	public WrapperTrisTrisCompartidos() {
		super();

	}

	public List<Tri> getListaTri() {
		return listaTri;
	}

	public void setListaTri(List<Tri> listaTri) {
		this.listaTri = listaTri;
	}

	public List<TriCompartido> getListaTriCompartido() {
		return listaTriCompartido;
	}

	public void setListaTriCompartido(List<TriCompartido> listaTriCompartido) {
		this.listaTriCompartido = listaTriCompartido;
	}

}
