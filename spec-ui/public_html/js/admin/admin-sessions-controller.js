ngapp.controller('sessionsCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getSessionCount = function () {
        httpApiGet($http, 'admin/sessions/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load session error!");
            }
        });
    };

    $scope.loadSessionWithStage = function (begin, end) {
        httpApiGet($http, 'admin/sessions/' + begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.sessions = $response.data;
                formatCreatedTime($scope.sessions);

            } else {
                alert("Load session error!");
            }
        });
    };

    $scope.paginationSession = function () {
        $scope.maxSize = 5;

        $scope.getSessionCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / PAGING_ITEMS_PER_PAGE);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * PAGING_ITEMS_PER_PAGE),
                    end = begin + PAGING_ITEMS_PER_PAGE - 1;
            $scope.sessions = $scope.loadSessionWithStage(begin, end);
        });
    };

    $scope.loadSessionWihFilter = function () {
        httpApiGet($http, 'admin/sessions/find/' + JSON.stringify($scope.filtparams) , function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.sessions = $response.data;
                formatCreatedTime($scope.sessions);

            } else {
                alert("Load user error!");
            }
        });
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loadSessionWihFilter();
    };

    $scope.deleteSession = function (sessionvalue, index) {
        httpApiDelete($http, 'admin/sessions/' + sessionvalue, function ($response) {
            console.log($response);
            $scope.sessions.splice(index, 1);
                Notification.success({message: 'Deleted session success', positionY: 'bottom', positionX: 'left'});
//            if ($response.status === HTTP_GONE) {
//                $scope.sessions.splice(index, 1);
//                Notification.success({message: 'Deleted session success', positionY: 'bottom', positionX: 'left'});
//            } else {
//                var actionStatus = "Error " + $response.statusText;
//                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
//            }
        });
    };   

    $scope.sortByPropertySession = function (propertysession) {
        if ($scope.sort === 'asc') {
            $scope.sessions.sort(dynamicSortDesc(propertysession));
            $scope.sort = 'desc';
        } else {
            $scope.sessions.sort(dynamicSortAsc(propertysession));
            $scope.sort = 'asc';
        }
    };

    $scope.currentPage = 1;
    $scope.sort = 'asc';
    $scope.itemsPerPage = PAGING_ITEMS_PER_PAGE;
    $scope.filtparams = {};
    $scope.paginationSession();

});