ngapp.controller('rolepermissionsCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getRolepermissionCount = function () {
        httpApiGet($http, 'admin.rolepermissions/search/'+JSON.stringify($scope.filtparams)+'/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load rolepermission error!");
            }
        });
    };
    
    $scope.loadRolepermissionWithStage = function (begin, end) {
        httpApiGet($http, 'admin.rolepermissions/search/' +  JSON.stringify($scope.filtparams)+'/'+begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.rolepermissions= $response.data;   
                $scope.rolepermissions.forEach(function (rolepermission) {
                    httpApiGet($http, 'roles/' + rolepermission.rolecode, function (response) {
                        rolepermission.role = response.data;
                    });
//                    httpApiGet($http, 'permissions/' + rolepermission.pmscode, function (response) {
//                        rolepermission.permission = response.data;
//                    });
                });
                formatCreatedTime($scope.rolepermissions);
            } else {
                alert("Load rolepermission error!");
            }
        });
    };
    
    $scope.currentPage = 1;
    $scope.arrayItemsPerPage = [5,10,25,50,100];
    $scope.selectedItemsPerPage = 5 ; 
    $scope.itemsPerPage = 5;
    $scope.paginationRolepermission = function () {
        $scope.itemsPerPage = 5;
        $scope.maxSize = 5;

        $scope.getRolepermissionCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / $scope.itemsPerPage);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
                    end = begin + $scope.itemsPerPage - 1;
            $scope.rolepermissions = $scope.loadRolepermissionWithStage(begin, end);
        });
    };
    
    $scope.changeItemsPerPage =  function(){
        $scope.itemsPerPage = $scope.selectedItemsPerPage;
        $scope.paginationRolepermission();
    };

    $scope.loadRolepermissionWihFilter = function () {
        $scope.paginationRolepermission();
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loadrolepermissionWihFilter();
    };

    $scope.deleteRolepermission = function (rolepermissionid, index) {
        httpApiDelete($http, 'admin.rolepermissions/' + rolepermissionid, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.rolepermissions.splice(index, 1);
                Notification.success({message: 'Deleted rolepermission success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };


    $scope.updateRolepermission = function (rolepermissionid,rolepermission) {
        rolepermission.createdtime = new Date();
        rolepermission.id = rolepermissionid;
//        alert(JSON.stringify(rolepermission, null, 4));
        httpApiPutJson($http, 'admin.rolepermissions/' + rolepermissionid, rolepermission, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.showEdit = false;
                $scope.paginationRolepermission();
                Notification.success({message: 'Edited rolepermission success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.createNewRolepermission = function (rolepermission) {
        rolepermission.createdtime = new Date();
//        alert(JSON.stringify(rolepermission, null, 4));
        httpApiPostJson($http, 'admin.rolepermissions', rolepermission, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.totalItems = parseInt($scope.totalItems) + 1;
                $scope.currentPage = Math.ceil($scope.totalItems / $scope.itemsPerPage) + 1;
                Notification.success({message: 'You created a new rolepermission', positionY: 'bottom', positionX: 'left'});
                $scope.showAdd = false;
                $scope.crolepermission={};
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
//                alert(JSON.stringify($scope.searchedApps, null, 4));
            }
        });
    };
    
    $scope.searchPermissionInputChange = function (searchValue) {
        httpApiGet($http, 'admin.permissions/searchname/' + searchValue, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.searchedPermissions = $response.data;
//                alert(JSON.stringify($scope.searchedUsers, null, 4));
            }
        });
    };

    $scope.sort = 'asc';
    $scope.sortByPropertyRolepermission = function (propertyrolepermission) {
        if ($scope.sort === 'asc') {
            $scope.rolepermissions.sort(dynamicSortDesc(propertyrolepermission));
            $scope.sort = 'desc';
        } else {
            $scope.rolepermissions.sort(dynamicSortAsc(propertyrolepermission));
            $scope.sort = 'asc';
        }
    };

    $scope.filtparams = {};
//    $scope.loadrolepermissions();
    $scope.paginationRolepermission();

});