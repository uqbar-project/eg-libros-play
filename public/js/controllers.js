'use strict';

var app = angular.module('librosApp', []);

/* Controllers */
app.controller('TodosLosLibrosCtrl', function ($scope, $http) {
    $scope.actualizarLista = function() {
    	$http.get('/libros').success(function(data) {
            $scope.libros = data;    
        })
    	
    }
    $scope.actualizarLista();

    // AGREGAR
    $scope.errors = [];
    $scope.msgs = [];
    $scope.agregarLibro = function() {
    	$http.post('/libros', $scope.nuevoLibro).success(function(data, status, headers, config) {
            if (data.msg != '') {
            	var id = data.id
                $scope.msgs.push('Libro agregado con id:' + id);
            }
            else {
                $scope.errors.push(data.error);
            }
            $scope.actualizarLista();
        }).error(function(data, status) { 
        	if (data.error) {
        		$scope.errors.push("Error: " + data.error);
        	}
        	else {
        		$scope.errors.push(status + ": " + data);
        	}
        });
    }

    // ELIMINAR
    $scope.eliminar = function(idLibro) {
    	$http.delete('/libros/' + idLibro).success(function(data, status) {
    		$scope.msgs.push('Libro eliminado!');
    		$scope.actualizarLista();
    	});
    }
    
    // ver detalle
    $scope.libroSeleccionado = null;
    $scope.verDetalle = function(libro) {
        $scope.libroSeleccionado = libro;
        $("#verLibroModal").modal({});
    }
    
});