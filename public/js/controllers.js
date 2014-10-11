'use strict';

var app = angular.module('librosApp', ['ngAnimate', 'chieffancypants.loadingBar']);




/* Controllers */
app.controller('TodosLosLibrosCtrl', function ($scope, $http, $timeout, cfpLoadingBar) {
	
    $scope.actualizarLista = function() {
    	$http.get('/libros')
    		.success(function(data) {
    			$scope.libros = data;
    		}
    	)
    }
    
    $scope.actualizarLista();

    // AGREGAR
    $scope.agregarLibro = function() {
    	$http.post('/libros', $scope.nuevoLibro)
    	.success(function(data) {
            if (data.msg != '') {
            	$scope.notificarMensaje('Libro agregado con id:' + data.id);
            }
            else {
            	$scope.notificarError(data.error);
            }
            $scope.actualizarLista();
            $scope.nuevoLibro = null;
        }
    	).error(function(data, status) { 
        	if (data.error) {
        		$scope.notificarError("Error: " + data.error);
        	}
        	else {
        		$scope.notificarError(status + ": " + data);
        	}
        });
    }
    
    // ELIMINAR
    $scope.eliminar = function(libro) {
    	var mensaje = "Seguro quiere eliminar '" + libro.titulo + "'?"
    	bootbox.confirm(mensaje, function(confirma) {
    		if (confirma) {
    			$http.delete('/libros/' + libro.id).success(function() {
    	    		$scope.notificarMensaje('Libro eliminado!');
    	    		$scope.actualizarLista();
    	    	});
    		}
    	});
    }
    
    // VER DETALLE
    $scope.libroSeleccionado = null;
    $scope.verDetalle = function(libro) {
        $scope.libroSeleccionado = libro;
        $("#verLibroModal").modal({});
    }
    
    // EDITAR LIBRO
    $scope.editarLibro = function(libro) {
    	$scope.libroSeleccionado = libro;
    	$("#editarLibroModal").modal({});
    }
    
    $scope.guardarLibro = function() {
    	$http.put('/libros/' + $scope.libroSeleccionado.id, $scope.libroSeleccionado)
    		.success(function() {
    			$scope.notificarMensaje('Libro actualizado!');
    			$scope.actualizarLista();
    		})
    		.error(function(data, status) { 
    			if (data.error)
    				$scope.notificarError("Error: " + data.error);
    			else
    				$scope.notificarError(status + ": " + data);
    		});
    	$scope.libroSeleccionado = null;
    	$("#editarLibroModal").modal('toggle');
    }
    
	// FEEDBACK & ERRORES
	$scope.msgs = [];
	$scope.notificarMensaje = function(mensaje) {
		$scope.msgs.push(mensaje);
		
		$timeout(function(){
			while($scope.msgs.length > 0) 
				$scope.msgs.pop();
	    }, 3000);
	};

	$scope.errors = [];
    $scope.notificarError = function(mensaje) {
    	$scope.errors.push(mensaje);
		$timeout(function(){
			while($scope.errors.length > 0) 
				$scope.errors.pop();
	    }, 3000);
    }

});