//*********************************************************************************************************
//USER CONTROLLER
//*********************************************************************************************************
hrApp.controller('UserController', function ($scope, $http, $log, $routeParams, $location, action) {
    $log.debug('UserController');
    $log.debug('action = ' + action); //logowanie akcji
    //READ-ALL
    $scope.getAll = function () {  //  scope dla wywołania getall
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
    // READ-ONE
    $scope.getOne = function (id) {
        $http.get('/users/' + id)
            .then(
                function success(response) {
                    $scope.user = response.data;
                    $log.debug('GET: /users/' + id);
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /users/' + id);
                    $log.error(response);
                    if (response.status === 404) {
                        // Handle 404 error (resource not found)
                        $location.path('/notFound'); // Redirect to a not-found page
                    }
                }
            );
    };
    //CREATE
    $scope.createOne = function (data) {
        $http.post('/users/', data)
            .then(
                function success(response) {
                    $scope.user = response.data; // do obiektu user podstaw dane z odpowiedzi
                    $log.debug('POST: /users/');
                    $log.debug(response);
                    $location.path('/users/all'); // zwraca sciezke bez parametrów
                },
                function error(response) {
                    $log.error('POST: /users/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };
    //UPDATE
    $scope.updateOne = function (id, data) {
        $http.put('/users/' + id, data)
            .then(
                function success(response) {
                    $scope.user = response.data;
                    $log.debug('PUT: /users/');
                    $log.debug(response);
                    $location.path('/users/all');
                },
                function error(response) {
                    $log.error('PUT: /users/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };
    //DELETE
    $scope.deleteUser = function (id) {
        $http.delete('/users/' + id)
            .then(
                function success(response) {
                    $scope.user = response.data;
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

    $scope.getAllRoles = function () {  //  scope dla wywołania getall
        $http.get('/roles')
            .then(
                function success(response) {
                    $scope.roles = response.data;
                    $log.debug('GET: /roles');
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /roles');
                    $log.error(response);
                }
            );
    };


    // AKCJA wywoluje dany scope

    // GET ONE
    if (action === 'one') {
        var id = $routeParams['id'];  // $routeParams service wbudowany
        $scope.getOne(id);
    }
    // GET ALL
    if (action === 'all') {
        $scope.getAll();
    }
    // CREATE ONE
    if (action === 'add') {
        $scope.user = {}; // utworz pusty obiekt
//        $scope.getAll(); // wykonaj akcje i zwróc wszystkie
        $scope.getAllRoles();
        $scope.formSubmit = function () { // formSubmit ng-submit
            $scope.createOne($scope.user);
        }
    }
    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);
        //$scope.getAll();
        $scope.getAllRoles();
        $scope.formSubmit = function () {
            $log.debug('update one: user');
            $log.debug($scope.user);
            $scope.updateOne($scope.user.id, $scope.user);
        }
    }
    // DELETE ONE
    if (action === 'delete') {
        var id = $routeParams['id'];
        $scope.deleteUser(id);
    }


});

