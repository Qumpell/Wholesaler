//*********************************************************************************************************
//COMPANY CONTROLLER
//*********************************************************************************************************
hrApp.controller('CompanyController', function ($scope, $http, $log, $routeParams, $location, action) {
    $log.debug('CompanyController');
    $log.debug('action = ' + action); //logowanie akcji

    $scope.offset = 0;
    $scope.pageSize = 10;
    $scope.field = 'id';
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.order = "asc";

    //READ-ALL
    $scope.getAll = function () {  //  scope dla wywołania getall
        var params = {
            offset: $scope.offset,
            pageSize: $scope.pageSize,
            field: $scope.field,
            order: $scope.order
        };

        $http.get('/companies',{params: params})
            .then(
                function success(response) {

                    $scope.companies = response.data.content;
                    $scope.totalItems = response.data.totalElements;
                    $scope.totalPages = response.data.totalPages;
                    $log.debug('GET: /companies');
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /companies');
                    $log.error(response);
                }
            );
    };
    $scope.changePage = function(pageChange) {

        $scope.offset += pageChange * $scope.pageSize;
        $scope.currentPage += pageChange;
        $scope.getAll();
    };

    $scope.sortByColumn = function(column) {
        if ($scope.field === column) {
            $scope.order = ($scope.order === 'asc') ? 'desc' : 'asc';
        } else {

            $scope.field = column;
            $scope.order = 'asc';
        }

        $scope.getAll();
    };

    // READ-ONE
    $scope.getOne = function (id) {
        $http.get('/companies/' + id)
            .then(
                function success(response) {
                    $scope.company = response.data;
                    $log.debug('GET: /companies/' + id);
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /companies/' + id);
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
        $http.post('/companies/', data)
            .then(
                function success(response) {
                    $scope.company = response.data; // do obiektu user podstaw dane z odpowiedzi
                    $log.debug('POST: /companies/');
                    $log.debug(response);
                    $location.path('/companies/all'); // zwraca sciezke bez parametrów
                },
                function error(response) {
                    $log.error('POST: /companies/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };
    //UPDATE
    $scope.updateOne = function (id, data) {
        $http.put('/companies/' + id, data)
            .then(
                function success(response) {
                    $scope.company = response.data;
                    $log.debug('PUT: /companies/');
                    $log.debug(response);
                    $location.path('/companies/all');
                },
                function error(response) {
                    $log.error('PUT: /companies/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };
    //DELETE
    $scope.deleteOne = function (id) {
        $http.delete('/companies/' + id)
            .then(
                function success(response) {
                    $scope.company = response.data;
                    $log.debug('DELETE: /companies/' + id);
                    $log.debug(response);
                    $location.path('/companies/all');
                },
                function error(response) {
                    $log.error('DELETE: /companies/' + id);
                    $log.error(response);
                }
            );
    };

    $scope.getAllIndustries = function () {  //  scope dla wywołania getall
        $http.get('/industries')
            .then(
                function success(response) {
                    $scope.industries = response.data;
                    $log.debug('GET: /industries');
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /industries');
                    $log.error(response);
                }
            );
    };

    $scope.getAllUsers = function () {
        $http.get('/users')
            .then(
                function success(response) {
                    $scope.users = response.data;
                    // console.log($scope.users); // Sprawdź dane użytkowników po pobraniu
                },
                function error(response) {
                    console.error('Error getting users', response);
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
        $scope.company = {}; // utworz pusty obiekt
        //$scope.getAll(); // wykonaj akcje i zwróc wszystkie
        $scope.getAllIndustries();
        $scope.getAllUsers();
        $scope.formSubmit = function () { // formSubmit ng-submit
            $scope.createOne($scope.company);
        }
    }
    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);
        //$scope.getAll();
        $scope.getAllIndustries();
        $scope.getAllUsers();
        $scope.formSubmit = function () {
            $log.debug('update one: company');
            $log.debug($scope.company);
            $scope.updateOne($scope.company.id, $scope.company);
        }
    }
    // DELETE ONE
    if (action === 'delete') {
        var id = $routeParams['id'];
        $scope.deleteOne(id);
    }

});