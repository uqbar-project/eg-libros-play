package controllers;

import java.util.Collection;
import java.util.List;

import org.uqbar.commons.model.UserException;

import play.libs.Json;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;
import uqbar.libros.domain.Biblioteca;
import uqbar.libros.domain.Libro;
import views.html.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Controller del servicio backend.
 * Acepta métodos que espera recibirán objetos en formato JSON.
 * Retorna también JSON.
 * 
 * No está para nada coplado con HTML, CSS, ni nada de vista.
 * 
 * @author jfernandes
 */
public class Application extends Controller {
  
    public static Result libros() {
    	response().setContentType("application/json");
    	Collection<Libro> libros = Biblioteca.getInstance().todasLasInstancias();
    	return ok(Json.toJson(libros));
    }
    
    public static Result libro(int id) {
    	try {
    		return ok(Json.toJson(Biblioteca.getInstance().getLibro(id)));
    	}
    	catch (UserException e) {
    		return notFound("No existe libro con id '" + id + "'");
    	}
    }
    
    public static Result eliminarLibro(int id) {
    	try {
    		Biblioteca.getInstance().eliminarLibro(id);
    		return ok(crearJsonOk());
    	}
    	catch (UserException e) {
    		return notFound("No existe libro con id '" + id + "'");
    	}
    }
    
    public static Result agregarLibro() {
    	try {
    		JsonNode node = request().body().asJson();
    		Libro nuevo = Json.fromJson(node, Libro.class);
    		nuevo.validar();
    		nuevo = Biblioteca.getInstance().agregarLibro(nuevo.getTitulo(), nuevo.getAutor());
    		
    		ObjectNode result = Json.newObject();
    		result.put("id", nuevo.getId());
    		return ok(result);
    	}
    	catch (UserException e) {
    		return badRequest(crearJsonError(e.getMessage()));
    	}
    }

	public static Result actualizar(int id) {
    	Libro actualizado = Json.fromJson(request().body().asJson(), Libro.class);
    	if (id != actualizado.getId()) {
    		return badRequest(crearJsonError("Id en URL distinto del cuerpo"));
    	}
    	Biblioteca.getInstance().actualizarLibro(actualizado);
    	return ok(crearJsonOk());
    }
    
	public static Result buscar(String titulo) {
    	List<Libro> libros = Biblioteca.getInstance().buscar(titulo);
    	return ok(Json.toJson(libros));
    }
    
    // utils
    
    protected static ObjectNode crearJsonError(String message) {
    	ObjectNode result = Json.newObject();
		result.put("error", message);
		return result;
	}
    
    protected static ObjectNode crearJsonOk() {
    	ObjectNode result = Json.newObject();
		result.put("status", "OK");
		return result;
	}

}