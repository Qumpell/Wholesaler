//*********************************************************************************************************
//COMPANY CONTROLLER
//*********************************************************************************************************
hrApp.controller('CompanyController', function ($scope,  $controller, $http, $log, $routeParams, $location, action) {
    $log.debug('CompanyController');
    $log.debug('action = ' + action); //logowanie akcji
    $controller('GetAllController', {$scope: $scope, $log: $log});

    //READ-ALL
    $scope.getAllCompanies = function () {  //  scope dla wywołania getall
        $scope.getAll("/companies");
    };
    $scope.changeCompaniesPage = function(pageChange) {

        $scope.changePage(pageChange, "/companies");
    };

    $scope.sortCompaniesByColumn = function(column) {
        $scope.sortByColumn(column, "/companies");
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
        $scope.getAll("/industries");
    };
    $scope.changeIndustriesFormPage = function(pageChange) {

        $scope.changePage(pageChange, "/industries");
    };
    $scope.pageUserSize = 10;
    $scope.currentUsersPage = 0;
    $scope.totalUsersPages = 0;
    $scope.getAllUsers = function () {
        var params = {
            pageNumber: $scope.currentPage,
            pageSize: $scope.pageSize,
            field: $scope.field,
            order: $scope.order
        };
        var url = "/users"
        $http.get(url, { params: params })
            .then(
                function success(response) {
                    $scope.users = response.data.content;
                    $scope.totalUsersItems = response.data.totalElements;
                    $scope.totalUsersPages = response.data.totalPages;
                    $log.debug('GET: ' + url);
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: ' + url);
                    $log.error(response);
                }
            );
    };
    $scope.changeUsersFormPage = function(pageChange) {
        $scope.currentUsersPage += pageChange;

        $scope.getAllUsers();
    };

    // AKCJA wywoluje dany scope

    // GET ONE
    if (action === 'one') {
        var id = $routeParams['id'];  // $routeParams service wbudowany
        $scope.getOne(id);
    }
    // GET ALL
    if (action === 'all') {
        $scope.getAllCompanies();
    }
    // CREATE ONE
    if (action === 'add') {
        $scope.company = {}; // utworz pusty obiekt

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