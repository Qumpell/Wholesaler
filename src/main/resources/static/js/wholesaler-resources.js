'use strict';

var hrApp = angular.module('hrApp', ['ngRoute']);

hrApp.value('version', '1.0');

hrApp.config(function ($routeProvider, $locationProvider) {
    $routeProvider
        .when('/', {
            template: "",
        })
        // USERS
        .when('/users/create', {
            templateUrl: '/view/user-form.html',
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'create';
                },
            }
        })
        .when('/users/all', {
            templateUrl: '/view/users.html',
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'all';
                },
            },
        })
        .when('/users/one/:id', {
            templateUrl: '/view/user.html',
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'one';
                },
            },
        })
        .when('/users/update/:id', {
            templateUrl: '/view/user-form.html',
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'update';
                },
            },
        })
        .when('/users/delete/:id', {
            template: "",
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'delete';
                },
            },
        })
        .otherwise({
            template: "<h1 class='red'>Error</h1><p>Wrong routing URL</p>",
        });
});

//==================================================================================================================================
//USER CONTROLLER----------------------------------------------------
//==================================================================================================================================
hrApp.controller('UserController', function ($scope, $http, $log, $routeParams, $location, action) {
    $log.debug('UserController');
    $log.debug('action = ' + action);

    $scope.getAll = function () {
        $http.get('/users')
            .then(
                function success(response) {
                    $scope.users = response.data;

                    $log.debug('GET: /users');
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /users');
                    $log.error(response);
                }
            );
    };

    $scope.getOne = function (id) {
        $http.get('/users/' + id)
            .then(
                function success(response) {
                    $scope.users = response.data;

                    $log.debug('GET: /users/' + id);
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /users/' + id);
                    $log.error(response);
                }
            );
    };

    $scope.createOne = function (data) {
        $http.post('/users/', data)
            .then(
                function success(response) {
                    $scope.users = response.data;

                    $log.debug('POST: /users/');
                    $log.debug(response);

                    $location.path('/users/all');
                },
                function error(response) {
                    $log.error('POST: /users/');
                    $log.error(response);

                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };

    $scope.updateOne = function (id, data) {
        $http.put('/users/' + id, data)
            .then(
                function success(response) {
                    $scope.users = response.data;

                    $log.debug('PUT: /users/' + id);
                    $log.debug(response);

                    $location.path('/users/all');
                },
                function error(response) {
                    $log.error('PUT: /users/' + id);
                    $log.error(response);

                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };

    $scope.deleteOne = function (id) {
        $http.delete('/users/' + id)
            .then(
                function success(response) {
                    $scope.users = response.data;

                    $log.debug('DELETE: /users/' + id);
                    $log.debug(response);

                    $location.path('/users/all');
                },
                function error(response) {
                    $log.error('DELETE: /users/' + id);
                    $log.error(response);
                }
            );
    };

    // GET ONE
    if (action === 'one') {
        var id = $routeParams['id'];
        $scope.getOne(id);
    }

    // GET ALL
    if (action === 'all') {
        $scope.getAll();
    }

    // CREATE ONE
    if (action === 'create') {
        $scope.user = {};
        $scope.getAll();

        $scope.formSubmit = function () {
            $scope.createOne($scope.user);
        }
    }

    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);
        // $scope.getAllPositions();
        // $scope.getAllDepartments();
        $scope.getAll();


        $scope.formSubmit = function () {
            $log.debug('update one: users');
            $log.debug($scope.user);
            // $scope.updateOne($scope.user.id, $scope.user);
            $scope.updateOne(id, $scope.user);
        }
    }

    // DELETE ONE
    if (action === 'delete') {
        var id = $routeParams['id'];
        $scope.deleteOne(id);
    }

});
