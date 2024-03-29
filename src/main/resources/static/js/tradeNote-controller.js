//*********************************************************************************************************
//TRADE NOTE CONTROLLER
//*********************************************************************************************************
hrApp.controller('TradeNoteController', function ($scope, $controller, $http, $log, $routeParams, $location, action) {
    $log.debug('TradeNote');
    $log.debug('action = ' + action); //logowanie akcji
    $controller('GetAllController', {$scope: $scope, $log: $log});

    //READ-ALL
    $scope.getAllTradeNotes = function () {  //  scope dla wywołania getall
        $scope.getAll("/tradeNotes");
    };

    $scope.changeTradeNotesPage = function(pageChange) {
        $scope.changePage(pageChange, "/tradeNotes");
    };

    $scope.sortTradeNotesByColumn = function(column) {
        $scope.sortByColumn(column, "/tradeNotes");
    };

    // READ-ONE
    $scope.getOne = function (id) {
        $http.get('/tradeNotes/' + id)
            .then(
                function success(response) {
                    $scope.tradeNote = response.data;
                    $log.debug('GET: /tradeNotes/' + id);
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /tradeNotes/' + id);
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
        $http.post('/tradeNotes/', data)
            .then(
                function success(response) {
                    $scope.tradeNote = response.data; // do obiektu user podstaw dane z odpowiedzi
                    $log.debug('POST: /tradeNotes/');
                    $log.debug(response);
                    $location.path('/tradeNotes/all'); // zwraca sciezke bez parametrów
                },
                function error(response) {
                    $log.error('POST: /tradeNotes/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };
    //UPDATE
    $scope.updateOne = function (id, data) {
        $http.put('/tradeNotes/' + id, data)
            .then(
                function success(response) {
                    $scope.tradeNote = response.data;
                    $log.debug('PUT: /tradeNotes/');
                    $log.debug(response);
                    $location.path('/tradeNotes/all');
                },
                function error(response) {
                    $log.error('PUT: /tradeNotes/');
                    $log.error(response);
                    $scope.formErrors = response.data.fieldErrors;
                }
            );
    };
    //DELETE
    $scope.deleteOne = function (id) {
        $http.delete('/tradeNotes/' + id)
            .then(
                function success(response) {
                    $scope.tradeNote = response.data;
                    $log.debug('DELETE: /tradeNotes/' + id);
                    $log.debug(response);
                    $location.path('/tradeNotes/all');
                },
                function error(response) {
                    $log.error('DELETE: /tradeNotes/' + id);
                    $log.error(response);
                }
            );
    };
    // Function to handle auto-resize of textarea
    $scope.resizeTextarea = function () {
        console.log('resizeTextarea called');
        var textarea = document.getElementById('content');
        textarea.style.height = 'auto';
        textarea.style.height = textarea.scrollHeight + 'px';
    };


    $scope.getAllCompanies = function () {  //  scope dla wywołania getall
      $scope.getAll("/companies")
    };
    $scope.changeCompaniesFormPage = function(pageChange) {

        $scope.changePage(pageChange, "/companies");
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
        var id = $routeParams['id'];
        $scope.getOne(id);
    }
    // GET ALL
    if (action === 'all') {
        $scope.getAllTradeNotes();
    }
    // CREATE ONE
    if (action === 'add') {
        $scope.tradeNote = {}; // utworz pusty obiekt
        $scope.getAllCompanies();
        $scope.getAllUsers();
        $scope.formSubmit = function () { // formSubmit ng-submit
            $scope.createOne($scope.tradeNote);
        };
    }
    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);

        $scope.getAllCompanies();
        $scope.getAllUsers();
        $scope.formSubmit = function () {
            $log.debug('update one: role');
            $log.debug($scope.tradeNote);
            $scope.updateOne($scope.tradeNote.id, $scope.tradeNote);
        };
    }
    // DELETE ONE
    if (action === 'delete') {
        var id = $routeParams['id'];
        $scope.deleteOne(id);
    }

});