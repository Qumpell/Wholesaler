//*********************************************************************************************************
//ROLE CONTROLLER
//*********************************************************************************************************
hrApp.controller('RoleController', function ($scope, $http, $log, $routeParams, $location, action) {
    $log.debug('RoleController');
    $log.debug('action = ' + action); //logowanie akcji
    //READ-ALL
    $scope.getAll = function () {  //  scope dla wywołania getall
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
        $scope.getAll();
    }
    // CREATE ONE
    if (action === 'add') {
        $scope.role = {}; // utworz pusty obiekt
        $scope.getAll; // wykonaj akcje i zwróc wszystkie
        $scope.formSubmit = function () { // formSubmit ng-submit
            $scope.createOne($scope.role);
        }
    }
    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);
        $scope.getAll();
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