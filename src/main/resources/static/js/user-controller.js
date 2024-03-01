//*********************************************************************************************************
//USER CONTROLLER
//*********************************************************************************************************
hrApp.controller('UserController', function ($scope, $controller, $http, $log, $routeParams, $location, action) {
    $log.debug('UserController');
    $log.debug('action = ' + action); //logowanie akcji
    $controller('GetAllController', {$scope: $scope, $log: $log});

    //READ-ALL
    $scope.getAllUsers = function () {  //  scope dla wywołania getall
         $scope.getAll("/users");
    };

    $scope.changeUsersPage = function(pageChange) {

        $scope.changePage(pageChange, "/users");
    };

    $scope.sortUsersByColumn = function(column) {
        $scope.sortByColumn(column, "/users");
    };

    // READ-ONE
    $scope.getOne = function (id) {
        $http.get('/users/' + id)
            .then(
                function success(response) {
                    $scope.user = response.data;
                    $log.debug('GET: /users/' + id);
                    $log.debug(response);
                    console.log($scope.user);
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
                    if (response.status === 404) {
                        // Handle 404 error (resource not found)
                        $location.path('/notFound'); // Redirect to a not-found page
                    }
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
                    if (response.status === 404) {
                        // Handle 404 error (resource not found)
                        $location.path('/notFound'); // Redirect to a not-found page
                    }
                }
            );
    };

    $scope.getAllRoles = function () {  //  scope dla wywołania getall
           $scope.getAll("/roles");
    };

    $scope.changeRolesFormPage = function(pageChange) {

        $scope.changePage(pageChange, "/roles");
    };


    // AKCJA wywoluje dany scope

    // GET ONE
    if (action === 'one') {
        var id = $routeParams['id'];  // $routeParams service wbudowany
        $scope.getOne(id);
    }
    // GET ALL
    if (action === 'all') {
        $scope.getAllUsers();
    }
    // CREATE ONE
    if (action === 'add') {
        $scope.user = {}; // utworz pusty obiekt
//        $scope.getAll(); // wykonaj akcje i zwróc wszystkie
       $scope.getAllRoles();
        $scope.formSubmit = function () { // formSubmit ng-submit
            console.log($scope.user);
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

