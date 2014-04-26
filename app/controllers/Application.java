package controllers;

import java.util.Collection;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import uqbar.libros.domain.Biblioteca;
import uqbar.libros.domain.Libro;
import views.html.index;

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
    	catch(RuntimeException e) {
    		return notFound();
    	}
    }
    
    public static Result eliminarLibro(int id) {
    	try {
    		Biblioteca.getInstance().eliminarLibro(id);
    		return ok("OK");
    	}
    	catch(RuntimeException e) {
    		return notFound();
    	}
    }

}
