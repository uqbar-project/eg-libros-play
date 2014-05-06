package controllers;

import java.util.Collection;
import java.util.List;

import org.uqbar.commons.model.UserException;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import uqbar.libros.domain.Biblioteca;
import uqbar.libros.domain.Libro;
import views.html.index;

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
  
    public static Result index() {
    	return ok(index.render("Your new application is ready."));
    }
    
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
    		return notFound();
    	}
    }
    
    public static Result eliminarLibro(int id) {
    	try {
    		Biblioteca.getInstance().eliminarLibro(id);
    		return ok("OK");
    	}
    	catch (UserException e) {
    		return notFound();
    	}
    }
    
    public static Result agregarLibro() {
    	try {
    		Libro nuevo = Json.fromJson(request().body().asJson(), Libro.class);
    		nuevo.validar();
    		nuevo = Biblioteca.getInstance().agregarLibro(nuevo.getTitulo(), nuevo.getAutor());
    		
    		ObjectNode result = Json.newObject();
    		result.put("id", nuevo.getId());
    		return ok(result);
    	}
    	catch (UserException e) {
    		ObjectNode result = Json.newObject();
    		result.put("error", e.getMessage());
    		return badRequest(result);
    	}
    }
    
    public static Result actualizar(int id) {
    	Libro actualizado = Json.fromJson(request().body().asJson(), Libro.class);
    	if (id != actualizado.getId()) {
    		return badRequest("Id en URL distinto del cuerpo");
    	}
    	Biblioteca.getInstance().actualizarLibro(actualizado);
    	return ok("OK");
    }
    
    public static Result buscar(String titulo) {
    	List<Libro> libros = Biblioteca.getInstance().buscar(titulo);
    	return ok(Json.toJson(libros));
    }

}