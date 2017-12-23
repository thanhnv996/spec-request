ngapp.controller('dashbroadCtrl', function ($rootScope, $state, $scope, $http, $window, Notification, authsession) {
//    $scope.getCountNewUser = function () {
//        httpApiGet($http, 'users/countnew/days/7', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.countNewUser = parseInt($response.data);
//                runCounter($scope.countNewUser, 1000, "countnewuser");
//            } else {
//                $state.go('login');
//                authsession.setUser({});
//            }
//        });
//    };
//
//    $scope.getCountNewApp = function () {
//        httpApiGet($http, 'apps/countnew/days/7', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.countNewApp = parseInt($response.data);
//                runCounter($scope.countNewApp, 1000, "countnewapp");
//            } else {
//                $state.go('login');
//                authsession.setUser({});
//            }
//        });
//
//        httpApiGet($http, 'apps/countnew/days/2', function ($response1) {
//            console.log($response1);
//            if ($response1.status === HTTP_OK) {
//                httpApiGet($http, 'apps/countnew/days/1', function ($response2) {
//                    console.log($response2);
//                    if ($response2.status === HTTP_OK) {
//                        $scope.appcreatedtoday = parseInt($response2.data);
//                        $scope.appcreatedyesterday = parseInt($response1.data) - $scope.appcreatedtoday;
//                    } else {
//                    }
//                });
//            } else {
//            }
//        });
//        httpApiGet($http, 'apps/countnew/days/7', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.appcreatedweek = parseInt($response.data);
//            } else {
//            }
//        });
//        httpApiGet($http, 'apps/countnew/days/30', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.appcreatedmoth = parseInt($response.data);
//            } else {
//            }
//        });
//        httpApiGet($http, 'apps/count', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.apptotal = parseInt($response.data);
//            } else {
//            }
//        });
//    };
//
//    $scope.getCountNewProduct = function () {
//        httpApiGet($http, 'products/countnew/days/7', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                runCounter(parseInt($response.data), 1000, "countnewproduct");
//            } else {
//                $state.go('login');
//                authsession.setUser({});
//            }
//        });
//    };
//
//    $scope.getCountNewPurchase = function () {
//        httpApiGet($http, 'purchases/countnew/days/7', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                runCounter(parseInt($response.data), 1000, "countnewpurchase");
//            } else {
//                $state.go('login');
//                authsession.setUser({});
//            }
//        });
//    };
//
//    $scope.viewNewApp = function () {
//        httpApiGet($http, 'apps/new/days/7', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.newApps = $response.data;
//                formatCreatedTime($scope.newApps);
//            } else {
//                $state.go('login');
//                authsession.setUser({});
//            }
//        });
//    };
//
//    $scope.viewNewProduct = function () {
//        httpApiGet($http, 'products/new/days/7', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.newProducts = $response.data;
//                formatCreatedTime($scope.newProducts);
//            } else {
//                $state.go('login');
//                authsession.setUser({});
//            }
//        });
//    };
//
//    $scope.showApp = function ($appcode) {
//        $state.go('app', {'appcode': $appcode});
//    };
//
//    $scope.showUser = function ($userid) {
//        $state.go('user', {'userid': $userid});
//    };
//
//    $scope.getTopAppPopular = function () {
//        httpApiGet($http, 'apps/topapppopular/5', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.topAppsPopular = $response.data;
//            } else {
//
//            }
//        });
//    }
//    
//    $scope.getTopUserCreateApp = function () {
//        httpApiGet($http, 'users/topusercreateapp/5', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.topUsers = $response.data;
//            } else {
//
//            }
//        });
//    }
//    
//    $scope.getTask = function() {
//        httpApiGet($http, 'apps/find/%/0/4', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.tasks = $response.data;
//            } else {
//
//            }
//        });
//    }
//
//    $scope.getCountNewUser();
//    $scope.getCountNewApp();
//    $scope.getCountNewProduct();
//    $scope.getCountNewPurchase();
//    $scope.getTopAppPopular();
//    $scope.getTopUserCreateApp();
//    $scope.getTask();
});
