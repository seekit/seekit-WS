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
import com.seekit.usuario.Usuario;

@RestController
@RequestMapping("/seekit")
public class WebService {

	// Seccion del WS dedicada al login, este metodo gracias al mail y la
	// contrasenia
	@RequestMapping(value = "/login")
	public ResponseEntity<WrapperObject> getLogin(
			@RequestParam(value = "mail", required = false) String mail,
			@RequestParam(value = "contrasenia", required = false) String contrasenia) {
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
	public ResponseEntity<List<Tri>> getTrisDelUsario(
			@RequestParam(value = "idUsuario", required = false) String idUsuario) {

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
				lista.add(tri);
			}
			System.out.println("Despues del While");
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			usu.closeLasCosas();
		}

		return new ResponseEntity<List<Tri>>(lista, HttpStatus.OK);
	}

	// Tri marcad como perdido
	@RequestMapping(value = "/markAsLost")
	public ResponseEntity<Tri> getMarkAsLost(
			@RequestParam(value = "idTri", required = true) String idTri,
			@RequestParam(value = "idUsuario", required = true) String idUsuario,
			@RequestParam(value = "identificador", required = false, defaultValue = "null") String identificador,
			@RequestParam(value = "location", required = false, defaultValue = "null") String location) {

		Tri triAux = new Tri(idTri);
		triAux.setIdentificador(identificador);
		boolean seMarconComoPerdido = triAux.markAsLost(idUsuario);
		if (seMarconComoPerdido) {
			return new ResponseEntity<Tri>(triAux, HttpStatus.OK);
		} else {
			return new ResponseEntity<Tri>(HttpStatus.NOT_MODIFIED);
		}

	}

	@RequestMapping(value = "/compartir")
	public ResponseEntity<Tri> getCompartir(
			@RequestParam(value = "mailUsuACompartir", required = true) String mailUsuACompartir,
			@RequestParam(value = "idUsuario", required = true) String idUsuario,
			@RequestParam(value = "idTri", required = true) String idTri,
			@RequestParam(value = "habiliado", required = false, defaultValue = "0") String habilitado) {

		Tri triAuxCompartir = new Tri(idTri);
		boolean seCompartio = triAuxCompartir.Compartir(mailUsuACompartir,
				idUsuario, habilitado);

		if (seCompartio) {
			return new ResponseEntity<Tri>(triAuxCompartir, HttpStatus.OK);
		} else {
			return new ResponseEntity<Tri>(HttpStatus.NOT_MODIFIED);
		}

	}

	@RequestMapping(value = "/editarUsuario")
	public ResponseEntity<Tri> getEditarUsuario(
			@RequestParam(value = "idusuario", required = true) String idUsuario,
			@RequestParam(value = "mail", required = true) String mail,
			@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "apellido", required = false, defaultValue = "0") String apellido) {

		Usuario usuEdit = new Usuario();
		usuEdit.setApellido(apellido);
		usuEdit.setMail(mail);
		usuEdit.setNombre(nombre);

		boolean seEditoUsu = usuEdit.editarusuario();
		return null;
	}

}
