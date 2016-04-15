// script.js

// create the module and name it blogApp
var blogApp = angular.module('blogApp', ['ngRoute']);

//ng-routes
blogApp.config(function($routeProvider) {
        $routeProvider

            // route for the home page
            .when('/home', {
                templateUrl : 'pages/home.html',
                controller  : 'mainCtrl'
            })

            // route for the about page
            .when('/about', {
                templateUrl : 'pages/about.html',
                controller  : 'aboutCtrl'
            })

            // route for the notes page
            .when('/notes', {
                templateUrl : 'pages/notes.html',
                controller  : 'notesCtrl'
            })

            .otherwise({
                           redirectTo: '/home'
                        });
    });

// create the controller and inject Angular's $scope
blogApp.controller('mainCtrl', function($scope) {

});

// create the controller and inject Angular's $scope
blogApp.controller('aboutCtrl', function($scope) {

});

// create the controller and inject Angular's $scope
blogApp.controller('notesCtrl', function($scope) {

});