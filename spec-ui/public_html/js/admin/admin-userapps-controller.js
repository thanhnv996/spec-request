ngapp.controller('userappsCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getUserappCount = function () {
        httpApiGet($http, 'admin.userapps/search/'+JSON.stringify($scope.filtparams)+'/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load userapp error!");
            }
        });
    };
    
    $scope.loadUserappWithStage = function (begin, end) {
        httpApiGet($http, 'admin.userapps/search/' +  JSON.stringify($scope.filtparams)+'/'+begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.userapps= $response.data;   
                $scope.userapps.forEach(function (userapp) {
                    httpApiGet($http, 'users/' + userapp.userid, function (response) {
                        userapp.user = response.data;
                    });
                    httpApiGet($http, 'apps/' + userapp.appcode, function (response) {
                        userapp.app = response.data;
                    });
                });
                formatCreatedTime($scope.userapps);
            } else {
                alert("Load userapp error!");
            }
        });
    };
    
    $scope.currentPage = 1;
    $scope.arrayItemsPerPage = [5,10,25,50,100];
    $scope.selectedItemsPerPage = 5 ; 
    $scope.itemsPerPage = 5;
    $scope.paginationUserapp = function () {
        $scope.itemsPerPage = 5;
        $scope.maxSize = 5;

        $scope.getUserappCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / $scope.itemsPerPage);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
                    end = begin + $scope.itemsPerPage - 1;
            $scope.userapps = $scope.loadUserappWithStage(begin, end);
        });
    };
    
    $scope.changeItemsPerPage =  function(){
        $scope.itemsPerPage = $scope.selectedItemsPerPage;
        $scope.paginationUserapp();
    };

    $scope.loadUserappWihFilter = function () {
        $scope.paginationUserapp();
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loaduserappWihFilter();
    };

    $scope.deleteUserapp = function (userappid, index) {
        httpApiDelete($http, 'admin.userapps/' + userappid, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.userapps.splice(index, 1);
                Notification.success({message: 'Deleted userapp success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.setCurrentUserapp = function (userapp) {
        $scope.cuserapp = JSON.parse(JSON.stringify(userapp));
        $scope.showNewuserapp = true;
    };

    $scope.hideCurrentUserapp = function () {
        $scope.cuserapp = null;
        $scope.showNewuserapp = false;
    };

    $scope.updateUserapp = function (euserapp,userappid) {
        euserapp.createdtime = new Date();
        euserapp.id = userappid;
        alert(JSON.stringify(euserapp, null, 4));
        httpApiPutJson($http, 'admin.userapps/' + userappid, euserapp, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.showEdit = false;
                $scope.paginationUserapp();
                Notification.success({message: 'Edited userapp success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.createNewUserapp = function (userapp) {
        userapp.createdtime = new Date();
//        alert(JSON.stringify(userapp, null, 4));
        httpApiPostJson($http, 'admin.userapps', userapp, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.totalItems = parseInt($scope.totalItems) + 1;
                $scope.currentPage = Math.ceil($scope.totalItems / $scope.itemsPerPage) + 1;
                Notification.success({message: 'You created a new userapp', positionY: 'bottom', positionX: 'left'});
                $scope.showAdd = false;
                $scope.cuserapp={};
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.searchUserInputChange = function (searchValue) {
        httpApiGet($http, 'admin.users/searchname/' + searchValue, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.searchedUsers = $response.data;
//                alert(JSON.stringify($scope.searchedUsers, null, 4));
            }
        });
    };
    
    $scope.searchAppInputChange = function (searchValue) {
        httpApiGet($http, 'admin.apps/searchname/' + searchValue, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.searchedApps = $response.data;
//                alert(JSON.stringify($scope.searchedApps, null, 4));
            }
        });
    };

    $scope.sort = 'asc';
    $scope.sortByPropertyUserapp = function (propertyuserapp) {
        if ($scope.sort === 'asc') {
            $scope.userapps.sort(dynamicSortDesc(propertyuserapp));
            $scope.sort = 'desc';
        } else {
            $scope.userapps.sort(dynamicSortAsc(propertyuserapp));
            $scope.sort = 'asc';
        }
    };

    $scope.filtparams = {};
    $scope.paginationUserapp();

});