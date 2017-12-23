ngapp.controller('permissionsCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getpermissionCount = function () {
        httpApiGet($http, 'admin.permissions/search/'+JSON.stringify($scope.filtparams)+'/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load permission error!");
            }
        });
    };
    
    $scope.loadpermissionWithStage = function (begin, end) {
        httpApiGet($http, 'admin.permissions/search/' +  JSON.stringify($scope.filtparams)+'/'+begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.permissions= $response.data;   
            } else {
                alert("Load permission error!");
            }
        });
    };
    
    $scope.currentPage = 1;
    $scope.arrayItemsPerPage = [5,10,25,50,100];
    $scope.selectedItemsPerPage = 5 ; 
    $scope.itemsPerPage = 5;
    $scope.paginationpermission = function () {        
        $scope.maxSize = 5;

        $scope.getpermissionCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / $scope.itemsPerPage);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
                    end = begin + $scope.itemsPerPage - 1;
            $scope.permissions = $scope.loadpermissionWithStage(begin, end);
        });
    };

    
    $scope.changeItemsPerPage =  function(){
        $scope.itemsPerPage = $scope.selectedItemsPerPage;
        $scope.paginationpermission();
    };
//    $scope.loadpermissions = function () {
//        httpApiGet($http, 'admin.permissions', function ($response) {
//            console.log('processLoginResult');
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.permissions = $response.data;
//                $scope.paginationpermission();
//            } else {
//                alert("Load permission error!");
//            }
//        });
//    };

    $scope.loadpermissionWihFilter = function () {
        $scope.paginationpermission();
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loadpermissionWihFilter();
    };

    $scope.deletepermission = function (permission, index) {
        httpApiDelete($http, 'admin.permissions/' + permission.pmscode, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.permissions.splice(index, 1);
                Notification.success({message: 'Deleted permission success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.updatePermission = function (permission) {
//        alert(JSON.stringify(permission, null, 4));
        httpApiPutJson($http, 'admin.permissions/' + permission.pmscode, permission, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                Notification.success({message: 'Edited permission success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.createNewpermission = function (permission) {
        httpApiPostJson($http, 'admin.permissions', permission, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.totalItems = parseInt($scope.totalItems) + 1;
                $scope.currentPage = Math.ceil($scope.totalItems / $scope.itemsPerPage) + 1;
//                formatCreatedTime($scope.permissions);
                Notification.success({message: 'You created a new permission', positionY: 'bottom', positionX: 'left'});
                $scope.showAdd = false;
                $scope.cpermission={};
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.searchpermissionInputChange = function (searchValue) {
        httpApiGet($http, 'admin.permissions/searchname/' + searchValue, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.searchedpermissions = $response.data;
            }
        });
    };

    $scope.sort = 'asc';
    $scope.sortByPropertyPermission = function (propertypermission) {
        if ($scope.sort === 'asc') {
            $scope.permissions.sort(dynamicSortDesc(propertypermission));
            $scope.sort = 'desc';
        } else {
            $scope.permissions.sort(dynamicSortAsc(propertypermission));
            $scope.sort = 'asc';
        }
    };

    $scope.filtparams = {};
//    $scope.loadpermissions();
    $scope.paginationpermission();

});