//*********************************************************************************************************
//CONTACT PERSON CONTROLLER
//*********************************************************************************************************
hrApp.controller('ContactPersonController', function ($scope, $controller, $http, $log, $routeParams, $location, action) {
    $log.debug('ContactPerson');
    $log.debug('action = ' + action); //logowanie akcji
    $controller('GetAllController', {$scope: $scope, $log: $log});

    //READ-ALL
    $scope.getAllContactPeople = function () {  //  scope dla wywołania getall
        $scope.getAll("/contactPersons");
    };

    $scope.changeContactPeoplePage = function(pageChange) {
        $scope.changePage(pageChange, "/contactPersons");
    };

    $scope.sortContactPeopleByColumn = function(column) {
        $scope.sortByColumn(column, "/contactPersons");
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
                    if (response.status === 404) {
                        // Handle 404 error (resource not found)
                        $location.path('/notFound'); // Redirect to a not-found page
                    }
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

    $scope.getAllCompanies = function () {  //  scope dla wywołania getall
        $http.get('/companies')
            .then(
                function success(response) {
                    $scope.companies = response.data;
                    $log.debug('GET: /companies');
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /companies');
                    $log.error(response);
                }
            );
    };
    $scope.getAllUsers = function () {  //  scope dla wywołania getall
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
    // AKCJA wywoluje dany scope

    // GET ONE
    if (action === 'one') {
        var id = $routeParams['id'];
        $scope.getOne(id);
    }
    // GET ALL
    if (action === 'all') {
        $scope.getAllContactPeople();
    }
    // CREATE ONE
    if (action === 'add') {
        $scope.contactPerson = {};
        $scope.getAllCompanies();
        $scope.getAllUsers();
        $scope.formSubmit = function () { // formSubmit ng-submit
            $scope.createOne($scope.contactPerson);
        }
    }
    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);
        $scope.getAllCompanies();
        $scope.getAllUsers();
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