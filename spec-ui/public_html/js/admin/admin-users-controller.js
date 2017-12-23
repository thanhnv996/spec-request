ngapp.controller('usersCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getUserCount = function () {
        httpApiGet($http, 'admin.users/search/'+JSON.stringify($scope.filtparams)+'/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load user error!");
            }
        });
    };
    
    $scope.loadUserWithStage = function (begin, end) {
        httpApiGet($http, 'admin.users/search/' +  JSON.stringify($scope.filtparams)+'/'+begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.users= $response.data;
                formatCreatedTime($scope.users);
                
            } else {
                alert("Load user error!");
            }
        });
    };
    
    $scope.paginationUser = function () {        
        $scope.maxSize = 5;

        $scope.getUserCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / PAGING_ITEMS_PER_PAGE);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * PAGING_ITEMS_PER_PAGE),
                    end = begin + PAGING_ITEMS_PER_PAGE - 1;
            $scope.users = $scope.loadUserWithStage(begin, end);
        });
    };

    
//    $scope.changeItemsPerPage =  function(){
//        $scope.itemsPerPage = $scope.selectedItemsPerPage;
//        $scope.paginationUser();
//    };
//    $scope.loadUsers = function () {
//        httpApiGet($http, 'admin.users', function ($response) {
//            console.log('processLoginResult');
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.users = $response.data;
//                $scope.paginationUser();
//            } else {
//                alert("Load user error!");
//            }
//        });
//    };

    $scope.loadUserWihFilter = function () {
        $scope.paginationUser();
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loadUserWihFilter();
    };

    $scope.deleteUser = function (user, index) {
        var user = {"id": user};
        httpApiDelete($http, 'admin.users/' + user.id, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.users.splice(index, 1);
                Notification.success({message: 'Deleted user success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };
    
    $scope.signalEditUser = function (user) {
        cuser = JSON.parse(JSON.stringify(user));
        user.e = cuser;
    };
    $scope.signalExitEditUser = function (user) {
        user.e = null;
    };
    
    $scope.setCurrentUser = function (user) {
        $scope.cuser = JSON.parse(JSON.stringify(user));
        $scope.showNewUser = true;
    };

    $scope.hideCurrentUser = function () {
        $scope.cuser = null;
        $scope.showNewUser = false;
    };

    $scope.updateUser = function (user) {
        //alert(JSON.stringify(user, null, 4));
        httpApiPutJson($http, 'admin.users/' + user.id, user, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.paginationUser();
                Notification.success({message: 'Edited user success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.createNewUser = function (user) {
        user.id = null;
        user.createdtime = new Date();
        httpApiPostJson($http, 'admin.users', user, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.totalItems = parseInt($scope.totalItems) + 1;
                $scope.currentPage = Math.ceil($scope.totalItems / PAGING_ITEMS_PER_PAGE) + 1;
                $scope.paginationUser();
//                formatCreatedTime($scope.users);
                Notification.success({message: 'You created a new user', positionY: 'bottom', positionX: 'left'});
                $scope.showAdd = false;
                $scope.cuser={};
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.searchRoleInputChange = function (searchValue) {
        httpApiGet($http, 'admin.roles/searchname/' + searchValue, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.searchedRoles = $response.data;
            }
        });
    };

    
    $scope.sortByPropertyUser = function (propertyUser) {
        if ($scope.sort === 'asc') {
            $scope.users.sort(dynamicSortDesc(propertyUser));
            $scope.sort = 'desc';
        } else {
            $scope.users.sort(dynamicSortAsc(propertyUser));
            $scope.sort = 'asc';
        }
    };
    
    $scope.currentPage = 1;
    $scope.sort = 'asc';
    $scope.itemsPerPage = PAGING_ITEMS_PER_PAGE;
    $scope.filtparams = {};
    $scope.paginationUser();

});