//*********************************************************************************************************
//ROLE CONTROLLER
//*********************************************************************************************************
hrApp.controller('RoleController', function ($scope, $controller, $http, $log, $routeParams, $location, action) {
    $log.debug('RoleController');
    $log.debug('action = ' + action); //logowanie akcji
    $controller('GetAllController', {$scope: $scope, $log: $log});

    //READ-ALL
    $scope.getAllRoles = function () {  //  scope dla wywołania getall
        $scope.getAll("/roles");
    };
    $scope.changeRolesPage = function(pageChange) {

        $scope.changePage(pageChange, "/roles");
    };

    $scope.sortRolesByColumn = function(column) {
        $scope.sortByColumn(column, "/roles");
    };

    // READ-ONE
    $scope.getOne = function (id) {
        $http.get('/roles/' + id)
            .then(
                function success(response) {
                    $scope.role = response.data;
                    $log.debug('GET: /roles/' + id);
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /roles/' + id);
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
        $http.post('/roles/', data)
            .then(
                function success(response) {
                    $scope.role = response.data; // do obiektu user podstaw dane z odpowiedzi
                    $log.debug('POST: /roles/');
                    $log.debug(response);
                    $location.path('/roles/all'); // zwraca sciezke bez parametrów
                },
                function error(response) {
                    $log.error('POST: /roles/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                    if (response.status === 409) {
                        $scope.errorMessage = 'Role with such name already exists.';
                    } else {
                        $scope.errorMessage = 'There was an error while creating new role.';
                    }
                }
            );
    };
    //UPDATE
    $scope.updateOne = function (id, data) {
        $http.put('/roles/' + id, data)
            .then(
                function success(response) {
                    $scope.role = response.data;
                    $log.debug('PUT: /roles/');
                    $log.debug(response);
                    $location.path('/roles/all');
                },
                function error(response) {
                    $log.error('PUT: /roles/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                    if (response.status === 409) {
                        $scope.errorMessage = 'Role with such name already exists.';
                    } else {
                        $scope.errorMessage = 'There was an error while updating the role.';
                    }
                }
            );
    };
    //DELETE
    $scope.deleteOne = function (id) {
        $http.delete('/roles/' + id)
            .then(
                function success(response) {
                    $scope.role = response.data;
                    $log.debug('DELETE: /roles/' + id);
                    $log.debug(response);
                    $location.path('/roles/all');
                },
                function error(response) {
                    $log.error('DELETE: /roles/' + id);
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
        $scope.getAllRoles();
    }
    // CREATE ONE
    if (action === 'add') {
        $scope.role = {}; // utworz pusty obiekt
        $scope.formSubmit = function () { // formSubmit ng-submit
            $scope.createOne($scope.role);
        }
    }
    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);
        $scope.formSubmit = function () {
            $log.debug('update one: role');
            $log.debug($scope.role);
            $scope.updateOne($scope.role.id, $scope.role);
        }
    }
    // DELETE ONE
    if (action === 'delete') {
        var id = $routeParams['id'];
        $scope.deleteOne(id);
    }

});