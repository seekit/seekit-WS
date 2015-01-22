package com.seekit.ws;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seekit.TRI.Tri;
import com.seekit.TRI.TriCompartido;
import com.seekit.usuario.Usuario;

@RestController
@RequestMapping("/seekit")
public class WebService {

	@RequestMapping(value = "/login")
	public ResponseEntity<Usuario> getLogin(
			@RequestParam(value = "mail", required = true) String mail,
			@RequestParam(value = "contrasenia", required = true) String contrasenia) {
		Usuario usuarioAux = new Usuario(mail, contrasenia);
		boolean existeusuario = usuarioAux.login();
		// mejor ya obtengo todos los datos del usuario y us tris con el login
		if (existeusuario) {
			return new ResponseEntity<Usuario>(usuarioAux, HttpStatus.OK);
		} else {
			return new ResponseEntity<Usuario>(HttpStatus.NO_CONTENT);
		}

	}

	// Seccion del WS dedicada al login, este metodo gracias al mail y la
	// contrasenia
	@RequestMapping(value = "/login2")
	public ResponseEntity<WrapperObject> getLogin2(
			@RequestParam(value = "mail", required = true) String mail,
			@RequestParam(value = "contrasenia", required = true) String contrasenia) {
		Usuario usuarioAux = new Usuario(mail, contrasenia);
		boolean existeusuario = usuarioAux.login();
		// mejor ya obtengo todos los datos del usuario y us tris con el login
		if (existeusuario) {
			List<Tri> lista = new ArrayList<Tri>();
			ResultSet res = usuarioAux.getTrisDelusuario(usuarioAux
					.getidUsuario());
			try {

				while (res.next()) {

					Tri tri = new Tri(res.getString("identificador"),
							res.getString("nombre"), res.getString("foto"),
							res.getInt("activo"), res.getString("location"),
							res.getInt("perdido"), res.getInt("compartido"));
					tri.setIdTri(res.getString("idtri"));
					lista.add(tri);
					System.out.println("Agregue un TRI: "
							+ res.getString("nombre"));
				}
				System.out.println("Despues del While");
			} catch (SQLException e) {

				e.printStackTrace();
			} finally {
				usuarioAux.closeLasCosas();
			}

			/*
			 * System.out.println(usuarioAux.getApellido() + "  " +
			 * usuarioAux.getContrasenia() + "  " + usuarioAux.getMail() + "  "
			 * + usuarioAux.getNombre() + "  " + existeusuario);
			 */
			WrapperObject wrapper = new WrapperObject();
			wrapper.setUsuario(usuarioAux);
			wrapper.setListaTris(lista);
			System.out.println(usuarioAux.getidUsuario());
			return new ResponseEntity<WrapperObject>(wrapper, HttpStatus.OK);
		} else {
			return new ResponseEntity<WrapperObject>(HttpStatus.NO_CONTENT);
		}

	}

	// Seccion del WS dedicada al registro, este metodo gracias a todos los
	// datos de un usuario creara el mismo.
	@RequestMapping(value = "/register")
	public ResponseEntity<Usuario> getRegister(
			@RequestParam(value = "mail", required = false) String mail,
			@RequestParam(value = "contrasenia", required = false) String contrasenia,
			@RequestParam(value = "nombre", required = false) String nombre,
			@RequestParam(value = "apellido", required = false) String apellido) {

		Usuario usuarioAux = new Usuario(nombre, apellido, mail, contrasenia);
		boolean seRegistro = usuarioAux.register();

		/*
		 * System.out.println(usuarioAux.getApellido() + "  " +
		 * usuarioAux.getContrasenia() + "  " + usuarioAux.getMail() + "  " +
		 * usuarioAux.getNombre() + "  " + seRegistro);
		 */
		if (seRegistro) {
			return new ResponseEntity<Usuario>(usuarioAux, HttpStatus.OK);
		}
		return new ResponseEntity<Usuario>(HttpStatus.NOT_ACCEPTABLE);

	}

	// Agregar un TRI asociado a unn usuario
	@RequestMapping(value = "/addTri")
	public ResponseEntity<Tri> getAddTri(
			@RequestParam(value = "idUsuario", required = false) String idUsuario,
			@RequestParam(value = "identificador", required = false) String identificador,
			@RequestParam(value = "nombre", required = false) String nombre,
			@RequestParam(value = "foto", required = false) String foto) {

		Tri triAux = new Tri(identificador, nombre, foto);
		boolean seAgregoTri = triAux.addTri(idUsuario);
		if (seAgregoTri == true) {

			return new ResponseEntity<Tri>(triAux, HttpStatus.OK);
		} else {
			return new ResponseEntity<Tri>(HttpStatus.NOT_ACCEPTABLE);
		}

	}

	// Obtener todos los tris de un usuario
	@RequestMapping(value = "/getTris")
	public ResponseEntity<WrapperTrisTrisCompartidos> getTrisDelUsario(
			@RequestParam(value = "idUsuario", required = false) String idUsuario) {
		WrapperTrisTrisCompartidos wrapper = new WrapperTrisTrisCompartidos();
		Usuario usu = new Usuario();
		List<Tri> lista = new ArrayList<Tri>();
		ResultSet res = usu.getTrisDelusuario(idUsuario);
		try {
			System.out.println("antes del Try");
			while (res.next()) {
				System.out.println("En el while");
				Tri tri = new Tri(res.getString("identificador"),
						res.getString("nombre"), res.getString("foto"),
						res.getInt("activo"), res.getString("location"),
						res.getInt("perdido"), res.getInt("compartido"));
				tri.setIdTri(res.getString("idtri"));
				lista.add(tri);
			}

			System.out.println("Despues del While");
			List<TriCompartido> listaCompartidosConMigo = new ArrayList<TriCompartido>();
			ResultSet res2 = usu.getCompartidosConmigo(idUsuario);
			while (res2.next()) {
				System.out.println("En el while");
				TriCompartido triCompartidosConMigo = new TriCompartido();
				triCompartidosConMigo.setFoto(res2.getString("foto"));
				triCompartidosConMigo.setHabilitado(res2
						.getString("habilitado"));
				triCompartidosConMigo.setIdentificador(res2
						.getString("identificador"));
				triCompartidosConMigo.setIdUsuarioPropietario(res2
						.getString("tri_usuario_idusuario"));
				triCompartidosConMigo.setNombre(res2.getString("nombre"));
				triCompartidosConMigo.setActivo(res2.getInt("activo"));
				triCompartidosConMigo.setIdTri(res2.getString("idtri"));
				triCompartidosConMigo.setIdUsuario(res2.getString("idusuario"));
				triCompartidosConMigo.setNombreUsuario(res2.getString(17));
				triCompartidosConMigo.setApellidoUsuario(res2.getString(18));
				triCompartidosConMigo.setMailUsuario(res2.getString(16));

				listaCompartidosConMigo.add(triCompartidosConMigo);
			}

			wrapper.setListaTri(lista);
			wrapper.setListaTriCompartido(listaCompartidosConMigo);

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			usu.closeLasCosas();
		}

		return new ResponseEntity<WrapperTrisTrisCompartidos>(wrapper,
				HttpStatus.OK);
	}

	// Tri marcad como perdido
	@RequestMapping(value = "/marcarComoPerdido")
	public ResponseEntity<Tri> getMarcarComoPerdido(
			@RequestParam(value = "idTri", required = true) String idTri,
			@RequestParam(value = "idUsuario", required = true) String idUsuario,
			@RequestParam(value = "identificador", required = false, defaultValue = "null") String identificador,
			@RequestParam(value = "location", required = false, defaultValue = "null") String location) {

		Tri triAux = new Tri(idTri);
		triAux.setIdentificador(identificador);
		boolean seMarconComoPerdido = triAux.marcarComoPerdido(idUsuario);
		if (seMarconComoPerdido) {
			return new ResponseEntity<Tri>(triAux, HttpStatus.OK);
		} else {
			return new ResponseEntity<Tri>(HttpStatus.NOT_MODIFIED);
		}

	}

	// Desmarcar como perdido
	@RequestMapping(value = "/desmarcarComoPerdido")
	public ResponseEntity<Tri> getDesmarcarComoPerdido(
			@RequestParam(value = "idTri", required = true) String idTri,
			@RequestParam(value = "idUsuario", required = true) String idUsuario,
			@RequestParam(value = "identificador", required = false, defaultValue = "null") String identificador,
			@RequestParam(value = "location", required = false, defaultValue = "null") String location) {

		Tri triAux = new Tri(idTri);
		triAux.setIdentificador(identificador);
		boolean sedesmarcoComoPerdido = triAux.desmarcarComoPerdido(idUsuario);
		if (sedesmarcoComoPerdido) {
			return new ResponseEntity<Tri>(triAux, HttpStatus.OK);
		} else {
			return new ResponseEntity<Tri>(HttpStatus.NOT_MODIFIED);
		}

	}

	@RequestMapping(value = "/compartirTri")
	public ResponseEntity<Tri> getCompartirTri(
			@RequestParam(value = "mailUsuACompartir", required = true) String mailUsuACompartir,
			@RequestParam(value = "idUsuario", required = true) String idUsuario,
			@RequestParam(value = "idTri", required = true) String idTri,
			@RequestParam(value = "habilitado", required = false, defaultValue = "0") String habilitado,
			@RequestParam(value = "comentario", required = false, defaultValue = "null") String comentario) {

		Tri triAuxCompartir = new Tri(idTri);
		boolean seCompartio = triAuxCompartir.compartir(mailUsuACompartir,
				idUsuario, habilitado, comentario);

		if (seCompartio) {
			return new ResponseEntity<Tri>(triAuxCompartir, HttpStatus.OK);
		} else {
			return new ResponseEntity<Tri>(HttpStatus.NOT_MODIFIED);
		}

	}

	@RequestMapping(value = "/triCompartido")
	public ResponseEntity<ArrayList<Usuario>> getCompartidosTri(
			@RequestParam(value = "idTri", required = true) String idTri) {

		Usuario triAuxCompartido = new Usuario();
		ArrayList<Usuario> arrayTriCompartido = triAuxCompartido
				.triCompartido(idTri);

		return new ResponseEntity<ArrayList<Usuario>>(arrayTriCompartido,
				HttpStatus.OK);

	}

	@RequestMapping(value = "/descompartirTri")
	public ResponseEntity<TriCompartido> getDescompartirTri(
			@RequestParam(value = "idTri", required = true) String idTri,
			@RequestParam(value = "idUsuario", required = true) String idUsuarioCompartido) {

		TriCompartido triCompartidoAux = new TriCompartido();
		triCompartidoAux.setIdTri(idTri);
		boolean seDescompartio = triCompartidoAux
				.descompartirTri(idUsuarioCompartido);

		if (seDescompartio) {
			return new ResponseEntity<TriCompartido>(triCompartidoAux,
					HttpStatus.OK);
		} else {
			return new ResponseEntity<TriCompartido>(HttpStatus.NOT_MODIFIED);
		}

	}

	// para cuando se comparta un tri, voy a eliminar la tupla de la BD en la
	// tabla tris compartidos si el usuario no quiere el tri, o en el caso de
	// que el usuario acepte el tri, voy a modificar la propiedad habilitado de
	// 0 a 1.
	@RequestMapping(value = "/confirmarTri")
	public ResponseEntity<TriCompartido> getConfirmarTri(
			@RequestParam(value = "idTri", required = true) String idTri,
			@RequestParam(value = "idUsuario", required = true) String idUsuarioCompartido,
			@RequestParam(value = "idUsuarioPropietario", required = true) String idUsuarioPropietario,
			@RequestParam(value = "confirmar", required = true) String confirmar) {

		TriCompartido triCompartidoAux = new TriCompartido();
		triCompartidoAux.setIdTri(idTri);
		triCompartidoAux.setIdUsuarioPropietario(idUsuarioPropietario);
		triCompartidoAux.setIdUsuario(idUsuarioCompartido);
		boolean seConfirmo = triCompartidoAux.confirmarTri(confirmar);

		if (seConfirmo) {
			return new ResponseEntity<TriCompartido>(HttpStatus.OK);
		} else {
			return new ResponseEntity<TriCompartido>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/editarUsuario")
	public ResponseEntity<Usuario> getEditarUsuario(
			@RequestParam(value = "idUsuario", required = true) String idUsuario,
			@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "apellido", required = false, defaultValue = "0") String apellido,
			@RequestParam(value = "mail", required = true) String mail,
			@RequestParam(value = "contrasenia", required = true) String contrasenia) {

		Usuario usuEdit = new Usuario();
		usuEdit.setidUsuario(idUsuario);
		usuEdit.setApellido(apellido);
		usuEdit.setMail(mail);
		usuEdit.setNombre(nombre);
		usuEdit.setContrasenia(contrasenia);

		boolean seEditoUsu = usuEdit.editarUsuario();
		if (seEditoUsu) {
			return new ResponseEntity<Usuario>(usuEdit, HttpStatus.OK);
		} else {
			return new ResponseEntity<Usuario>(HttpStatus.NOT_MODIFIED);
		}
	}

	@RequestMapping(value = "/editarTri")
	public ResponseEntity<Tri> getEditarTri(
			@RequestParam(value = "idTri", required = true) String idTri,
			@RequestParam(value = "identificador", required = true) String identificador,
			@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "foto", required = false, defaultValue = "null") String foto) {

		Tri triEdit = new Tri();
		triEdit.setIdTri(idTri);
		triEdit.setIdentificador(identificador);
		triEdit.setNombre(nombre);
		triEdit.setFoto(foto);
		boolean seEditoTri = triEdit.editarTri();
		if (seEditoTri) {
			return new ResponseEntity<Tri>(triEdit, HttpStatus.OK);
		} else {
			return new ResponseEntity<Tri>(HttpStatus.NOT_MODIFIED);
		}
	}

	@RequestMapping(value = "/getNotificaciones")
	public ResponseEntity<ArrayList<TriCompartido>> getNotificaciones(
			@RequestParam(value = "idUsuario", required = true) String idUsuario) {

		Usuario usuNoti = new Usuario();
		usuNoti.setidUsuario(idUsuario);
		ArrayList<TriCompartido> listaTrisCompartidos = usuNoti
				.notificaciones();

		return new ResponseEntity<ArrayList<TriCompartido>>(
				listaTrisCompartidos, HttpStatus.OK);

	}

	// si un tri se encuentra como perdido, se debera eliminar de kla tupla y
	// actualizar el
	@RequestMapping(value = "/seEncontroTriPerdido")
	public ResponseEntity<Tri> seEncontroTriPerdido(
			@RequestParam(value = "idTri", required = true) String idTri,
			@RequestParam(value = "location", required = true) String location) {

		Tri triAux = new Tri();
		triAux.setIdTri(idTri);
		triAux.setLocalizacion(location);
		boolean seEncontro = triAux.seEncontroTriPerdido();
		if (seEncontro) {
			return new ResponseEntity<Tri>(triAux, HttpStatus.OK);
		} else {
			return new ResponseEntity<Tri>(HttpStatus.NOT_MODIFIED);
		}

	}

}
