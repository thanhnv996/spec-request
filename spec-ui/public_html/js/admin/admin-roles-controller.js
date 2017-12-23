ngapp.controller('rolesCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getRoleCount = function () {
        httpApiGet($http, 'admin.roles/search/'+JSON.stringify($scope.filtparams)+'/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load role error!");
            }
        });
    };
    
    $scope.loadRoleWithStage = function (begin, end) {
        httpApiGet($http, 'admin.roles/search/' +  JSON.stringify($scope.filtparams)+'/'+begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.roles= $response.data;   
            } else {
                alert("Load role error!");
            }
        });
    };
    
    $scope.currentPage = 1;
    $scope.arrayItemsPerPage = [5,10,25,50,100];
    $scope.selectedItemsPerPage = 5 ; 
    $scope.itemsPerPage = 5;
    $scope.paginationRole = function () {        
        $scope.maxSize = 5;

        $scope.getRoleCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / $scope.itemsPerPage);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
                    end = begin + $scope.itemsPerPage - 1;
            $scope.roles = $scope.loadRoleWithStage(begin, end);
        });
    };

    
    $scope.changeItemsPerPage =  function(){
        $scope.itemsPerPage = $scope.selectedItemsPerPage;
        $scope.paginationRole();
    };
//    $scope.loadroles = function () {
//        httpApiGet($http, 'admin.roles', function ($response) {
//            console.log('processLoginResult');
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.roles = $response.data;
//                $scope.paginationrole();
//            } else {
//                alert("Load role error!");
//            }
//        });
//    };

    $scope.loadRoleWihFilter = function () {
        $scope.paginationRole();
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loadRoleWihFilter();
    };

    $scope.deleteRole = function (role, index) {
        httpApiDelete($http, 'admin.roles/' + role.rolecode, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.roles.splice(index, 1);
                Notification.success({message: 'Deleted role success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.updateRole = function (role) {
//        alert(JSON.stringify(role, null, 4));
        httpApiPutJson($http, 'admin.roles/' + role.rolecode, role, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.showEdit = false;
                Notification.success({message: 'Edited role success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.createNewRole = function (role) {
        httpApiPostJson($http, 'admin.roles', role, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.totalItems = parseInt($scope.totalItems) + 1;
                $scope.currentPage = Math.ceil($scope.totalItems / $scope.itemsPerPage) + 1;
//                formatCreatedTime($scope.roles);
                Notification.success({message: 'You created a new role', positionY: 'bottom', positionX: 'left'});
                $scope.showAdd = false;
                $scope.crole={};
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

    $scope.sort = 'asc';
    $scope.sortByPropertyRole = function (propertyrole) {
        if ($scope.sort === 'asc') {
            $scope.roles.sort(dynamicSortDesc(propertyrole));
            $scope.sort = 'desc';
        } else {
            $scope.roles.sort(dynamicSortAsc(propertyrole));
            $scope.sort = 'asc';
        }
    };

    $scope.filtparams = {};
//    $scope.loadroles();
    $scope.paginationRole();

});