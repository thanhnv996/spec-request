ngapp.controller('toolsCtrl', function ($scope, $http, $window, $state, Notification) {
    $scope.deleteAllUsers = function () {
        $scope.eventClick = "delete all users";
    };

    $scope.deleteAllApps = function () {
        $scope.eventClick = "delete all apps";
    };

    $scope.deleteAllRoles = function () {
        $scope.eventClick = "delete all roles";
    };

    $scope.deleteAllPermissions = function () {
        $scope.eventClick = "delete all permissions";
    };

    $scope.initDatabases = function () {
        $scope.eventClick = "init databases";
    };

    $scope.agree = function () {

        if ($scope.eventClick === "delete all users") {
            httpApiDelete($http, 'system/deleteusers', function ($response) {
                console.log($response);
                if ($response.status === 200) {
                    Notification.success({message: 'Deleted all users success', positionY: 'bottom', positionX: 'left'});
                } else {
                    var actionStatus = "Error " + $response.statusText;
                    Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
                }
            });
        }

        if ($scope.eventClick === "delete all apps") {
            httpApiDelete($http, 'system/deleteapps', function ($response) {
                console.log($response);
                if ($response.status === HTTP_OK) {
                    Notification.success({message: 'Deleted all apps success', positionY: 'bottom', positionX: 'left'});
                } else {
                    var actionStatus = "Error " + $response.statusText;
                    Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
                }
            });
        }

        if ($scope.eventClick === "delete all roles") {
            httpApiDelete($http, 'system/deleteroles', function ($response) {
                console.log($response);
                if ($response.status === HTTP_OK) {
                    Notification.success({message: 'Deleted all roles success', positionY: 'bottom', positionX: 'left'});
                } else {
                    var actionStatus = "Error " + $response.statusText;
                    Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
                }
            });
        }

        if ($scope.eventClick === "delete all permissions") {
            httpApiDelete($http, 'system/deletepermisstions', function ($response) {
                console.log($response);
                if ($response.status === HTTP_OK) {
                    Notification.success({message: 'Deleted all permissions success', positionY: 'bottom', positionX: 'left'});
                } else {
                    var actionStatus = "Error " + $response.statusText;
                    Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
                }
            });
        }

        if ($scope.eventClick === "init databases") {
            httpApiPost($http, 'system/initdatabase', function ($response) {
                console.log($response);
                if ($response.status === HTTP_OK) {
                    Notification.success({message: 'init databases success', positionY: 'bottom', positionX: 'left'});
                } else {
                    var actionStatus = "Error " + $response.statusText;
                    Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
                }
            });
        }
    };

    $scope.createAdmin = function (admin) {
        if(!admin) Notification.error({message: "error", positionY: 'bottom', positionX: 'left'});
        else{
            httpApiPost($http, 'system/createadmins', admin, function ($response) {
                console.log($response);
                if ($response.status === 201) {
                    Notification.success({message: 'create admin success', positionY: 'bottom', positionX: 'left'});
                } else {
                    var actionStatus = "Error " + $response.statusText;
                    Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
                }
            });
        }
    };
});