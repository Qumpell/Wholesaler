//*********************************************************************************************************
//CONTACT PERSON CONTROLLER
//*********************************************************************************************************
hrApp.controller('ContactPersonController', function ($scope, $http, $log, $routeParams, $location, action) {
    $log.debug('ContactPerson');
    $log.debug('action = ' + action); //logowanie akcji
    //READ-ALL
    $scope.getAll = function () {  //  scope dla wywołania getall
        $http.get('/contactPersons')
            .then(
                function success(response) {
                    $scope.contactPersons = response.data;
                    $log.debug('GET: /contactPersons');
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /contactPersons');
                    $log.error(response);
                }
            );
    };
    // READ-ONE
    $scope.getOne = function (id) {
        $http.get('/contactPersons/' + id)
            .then(
                function success(response) {
                    $scope.contactPerson = response.data;
                    $log.debug('GET: /contactPersons/' + id);
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /contactPersons/' + id);
                    $log.error(response);
                }
            );
    };
    //CREATE
    $scope.createOne = function (data) {
        $http.post('/contactPersons/', data)
            .then(
                function success(response) {
                    $scope.contactPerson = response.data; // do obiektu user podstaw dane z odpowiedzi
                    $log.debug('POST: /contactPersons/');
                    $log.debug(response);
                    $location.path('/contactPersons/all'); // zwraca sciezke bez parametrów
                },
                function error(response) {
                    $log.error('POST: /contactPersons/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };
    //UPDATE
    $scope.updateOne = function (id, data) {
        $http.put('/contactPersons/' + id, data)
            .then(
                function success(response) {
                    $scope.contactPerson = response.data;
                    $log.debug('PUT: /contactPersons/');
                    $log.debug(response);
                    $location.path('/contactPersons/all');
                },
                function error(response) {
                    $log.error('PUT: /contactPersons/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };
    //DELETE
    $scope.deleteOne = function (id) {
        $http.delete('/contactPersons/' + id)
            .then(
                function success(response) {
                    $scope.contactPerson = response.data;
                    $log.debug('DELETE: /contactPersons/' + id);
                    $log.debug(response);
                    $location.path('/contactPersons/all');
                },
                function error(response) {
                    $log.error('DELETE: /contactPersons/' + id);
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
        $scope.contactPerson = {}; // utworz pusty obiekt
        // $scope.getAll; // wykonaj akcje i zwróc wszystkie
        $scope.formSubmit = function () { // formSubmit ng-submit
            $scope.createOne($scope.contactPerson);
        }
    }
    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);
        //$scope.getAll();
        $scope.formSubmit = function () {
            $log.debug('update one: contactPerson');
            $log.debug($scope.contactPerson);
            $scope.updateOne($scope.contactPerson.id, $scope.contactPerson);
        }
    }
    // DELETE ONE
    if (action === 'delete') {
        var id = $routeParams['id'];
        $scope.deleteOne(id);
    }

});