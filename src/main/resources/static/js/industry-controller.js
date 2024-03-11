//*********************************************************************************************************
//ROLE CONTROLLER
//*********************************************************************************************************
hrApp.controller('IndustryController', function ($scope, $controller,  $http, $log, $routeParams, $location, action) {
    $log.debug('IndustryController');
    $log.debug('action = ' + action); //logowanie akcji
    $controller('GetAllController', {$scope: $scope, $log: $log});

    //READ-ALL
    $scope.getAllIndustries = function () {  //  scope dla wywołania getall
        $scope.getAll("/industries");
    };
    $scope.changeIndustriesPage = function(pageChange) {
        $scope.changePage(pageChange, "/industries");
    };

    $scope.sortIndustriesByColumn = function(column) {
        $scope.sortByColumn(column, "/industries");
    };


    // READ-ONE
    $scope.getOne = function (id) {
        $http.get('/industries/' + id)
            .then(
                function success(response) {
                    $scope.industry = response.data;
                    $log.debug('GET: /industries/' + id);
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /industries/' + id);
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
        $http.post('/industries', data)
            .then(
                function success(response) {
                    $scope.industry = response.data; // do obiektu user podstaw dane z odpowiedzi
                    $log.debug('POST: /industries/');
                    $log.debug(response);
                    $location.path('/industries/all'); // zwraca sciezke bez parametrów
                },
                function error(response) {
                    $log.error('POST: /industries/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                    if (response.status === 409) {
                        $scope.errorMessage = 'Industry with such name already exists.';
                    } else {
                        $scope.errorMessage = 'There was an error while creating new industry.';
                    }
                }
            );
    };
    //UPDATE
    $scope.updateOne = function (id, data) {
        $http.put('/industries/' + id, data)
            .then(
                function success(response) {
                    $scope.industry = response.data;
                    $log.debug('PUT: /industries/');
                    $log.debug(response);
                    $location.path('/industries/all');
                },
                function error(response) {
                    $log.error('PUT: /industries/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                    if (response.status === 409) {
                        $scope.errorMessage = 'Industry with such name already exists.';
                    } else {
                        $scope.errorMessage = 'There was an error while creating new industry.';
                    }
                }
            );
    };
    //DELETE
    $scope.deleteOne = function (id) {
        $http.delete('/industries/' + id)
            .then(
                function success(response) {
                    $scope.industry = response.data;
                    $log.debug('DELETE: /industries/' + id);
                    $log.debug(response);
                    $location.path('/industries/all');
                },
                function error(response) {
                    $log.error('DELETE: /industries/' + id);
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
        $scope.getAllIndustries();
    }
    // CREATE ONE
    if (action === 'add') {
        $scope.industry = {}; // utworz pusty obiekt
        // $scope.getAllIndustries(); // wykonaj akcje i zwróc wszystkie
        $scope.formSubmit = function () { // formSubmit ng-submit
            $scope.createOne($scope.industry);
        }
    }
    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);
        $scope.getAll();
        $scope.formSubmit = function () {
            $log.debug('update one: industry');
            $log.debug($scope.industry);
            $scope.updateOne($scope.industry.id, $scope.industry);
        }
    }
    // DELETE ONE
    if (action === 'delete') {
        var id = $routeParams['id'];
        $scope.deleteOne(id);
    }

});