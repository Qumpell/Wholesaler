//*********************************************************************************************************
//TRADE NOTE CONTROLLER
//*********************************************************************************************************
hrApp.controller('TradeNoteController', function ($scope, $http, $log, $routeParams, $location, action) {
    $log.debug('TradeNote');
    $log.debug('action = ' + action); //logowanie akcji
    //READ-ALL
    $scope.getAll = function () {  //  scope dla wywołania getall
        $http.get('/tradeNotes')
            .then(
                function success(response) {
                    $scope.tradeNotes = response.data;
                    $log.debug('GET: /tradeNotes');
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: /tradeNotes');
                    $log.error(response);
                }
            );
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
        $scope.tradeNote = {}; // utworz pusty obiekt
       // $scope.getAll; // wykonaj akcje i zwróc wszystkie
        $scope.formSubmit = function () { // formSubmit ng-submit
            $scope.createOne($scope.tradeNote);
        };
    }
    // UPDATE ONE
    if (action === 'update') {
        var id = $routeParams['id'];
        $scope.getOne(id);
        //$scope.getAll();
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