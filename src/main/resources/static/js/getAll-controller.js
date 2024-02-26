hrApp.controller('GetAllController', function ($scope, $http, $log) {
    $log.debug('GetAllController');

    $scope.pageSize = 10;
    $scope.field = 'id';
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.order = "asc";

    $scope.getAll = function (url) {
        var params = {
            pageNumber: $scope.currentPage,
            pageSize: $scope.pageSize,
            field: $scope.field,
            order: $scope.order
        };
        $http.get(url, { params: params })
            .then(
                function success(response) {
                    $scope.items = response.data.content;
                    $scope.totalItems = response.data.totalElements;
                    $scope.totalPages = response.data.totalPages;
                    console.log(response);
                    $log.debug('GET: ' + url);
                    $log.debug(response);
                },
                function error(response) {
                    $log.error('GET: ' + url);
                    $log.error(response);
                }
            );
    };

    $scope.changePage = function (pageChange, url) {
        $scope.currentPage += pageChange;
        $scope.getAll(url);
    };

    $scope.sortByColumn = function (column, url) {
        if ($scope.field === column) {
            $scope.order = ($scope.order === 'asc') ? 'desc' : 'asc';
        } else {
            $scope.field = column;
            $scope.order = 'asc';
        }

        $scope.getAll(url);
    };
});


