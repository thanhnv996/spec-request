ngapp.controller('appsCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getAppCount = function () {
        httpApiGet($http, 'admin.apps/search/'+JSON.stringify($scope.filtparams)+'/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load app error!");
            }
        });
    };
    
    $scope.loadAppWithStage = function (begin, end) {
        httpApiGet($http, 'admin.apps/search/' +  JSON.stringify($scope.filtparams)+'/'+begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.apps= $response.data;   
                $scope.apps.forEach(function (app) {
                    httpApiGet($http, 'users/' + app.userid, function (response) {
                        app.user = response.data;
                    });
                });
                formatCreatedTime($scope.apps);
            } else {
                alert("Load app error!");
            }
        });
    };
    
    $scope.currentPage = 1;
    $scope.arrayItemsPerPage = [5,10,25,50,100];
    $scope.selectedItemsPerPage = 5 ; 
    $scope.itemsPerPage = 5;
    $scope.paginationApp = function () {
        $scope.itemsPerPage = 5;
        $scope.maxSize = 5;

        $scope.getAppCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / $scope.itemsPerPage);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
                    end = begin + $scope.itemsPerPage - 1;
            $scope.apps = $scope.loadAppWithStage(begin, end);
        });
    };
    
    $scope.changeItemsPerPage =  function(){
        $scope.itemsPerPage = $scope.selectedItemsPerPage;
        $scope.paginationApp();
    };
    

//    $scope.loadapps = function () {
//        httpApiGet($http, 'admin.apps', function ($response) {
//            console.log('processLoginResult');
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.apps = $response.data;
//                $scope.paginationapp();
//            } else {
//                alert("Load app error!");
//            }
//        });
//    };

    $scope.loadAppWihFilter = function () {
        $scope.paginationApp();
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loadAppWihFilter();
    };

    $scope.deleteApp = function (appcode, index) {
        httpApiDelete($http, 'admin.apps/' + appcode, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.apps.splice(index, 1);
                Notification.success({message: 'Deleted app success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.setCurrentApp = function (app) {
        $scope.capp = JSON.parse(JSON.stringify(app));
        $scope.showNewapp = true;
    };

    $scope.hideCurrentApp = function () {
        $scope.capp = null;
        $scope.showNewapp = false;
    };

    $scope.updateApp = function (app) {
        app.tokenvalue = createTokenValue();
//        alert(JSON.stringify(app, null, 4));
        httpApiPutJson($http, 'admin.apps/' + app.appcode, app, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.showEdit=false;
                Notification.success({message: 'Edited app success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.createNewApp = function (app) {
        app.createdtime = new Date();
        app.tokenvalue=createTokenValue();
//        alert(JSON.stringify(app, null, 4));
        httpApiPostJson($http, 'admin.apps', app, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.totalItems = parseInt($scope.totalItems) + 1;
                $scope.currentPage = Math.ceil($scope.totalItems / $scope.itemsPerPage) + 1;
//                formatCreatedTime($scope.apps);
                Notification.success({message: 'You created a new app', positionY: 'bottom', positionX: 'left'});
                $scope.showAdd = false;
                $scope.capp={};
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
            }
        });
    };

    $scope.sort = 'asc';
    $scope.sortByPropertyApp = function (propertyapp) {
        if ($scope.sort === 'asc') {
            $scope.apps.sort(dynamicSortDesc(propertyapp));
            $scope.sort = 'desc';
        } else {
            $scope.apps.sort(dynamicSortAsc(propertyapp));
            $scope.sort = 'asc';
        }
    };

    $scope.filtparams = {};
//    $scope.loadapps();
    $scope.paginationApp();

});