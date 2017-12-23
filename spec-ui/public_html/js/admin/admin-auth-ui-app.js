'use strict';

var ngapp = angular.module('admin-auth-ui-app', ['ngAnimate', 'ui.router', 'xeditable', 'ui.bootstrap', 'ui-notification', 'chieffancypants.loadingBar']).config(function ($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
});
ngapp.controller('sessionstatusCtrl', function ($scope, $http, $timeout, cfpLoadingBar) {
    $scope.posts = [];
    $scope.section = null;


    $scope.start = function () {
        cfpLoadingBar.start();
    };

    $scope.complete = function () {
        cfpLoadingBar.complete();
    };


    // fake the initial load so first time users can see it right away:
    $scope.start();
    $scope.fakeIntro = true;
    $timeout(function () {
        $scope.complete();
        $scope.fakeIntro = false;
    }, 750);

});

ngapp.service('authsession', function () {
    var user = {};
    function getUser() {
        return user;
    }
    function setUser(newuser) {
        user = newuser;
    }
    return {
        getUser: getUser,
        setUser: setUser
    };
});

ngapp.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/users");

        $stateProvider
                .state('users', {
                    url: "/users",
                    templateUrl: "adminui/users.html",
                    controller: 'usersCtrl'
                })
                .state('apps', {
                    url: "/apps",
                    templateUrl: "adminui/apps.html",
                    controller: 'appsCtrl'
                })
                .state('userapps', {
                    url: "/userapps",
                    templateUrl: "adminui/userapps.html",
                    controller: 'userappsCtrl'
                })
                .state('roles', {
                    url: "/roles",
                    templateUrl: "adminui/roles.html",
                    controller: 'rolesCtrl'
                })
                .state('permissions', {
                    url: "/permissions",
                    templateUrl: "adminui/permissions.html",
                    controller: 'permissionsCtrl'
                })                
                .state('rolepermissions', {
                    url: "/rolepermissions",
                    templateUrl: "adminui/rolepermissions.html",
                    controller: 'rolepermissionsCtrl'
                })
                .state('products', {
                    url: "/products",
                    templateUrl: "adminui/products.html",
                    controller: 'productsCtrl'
                })
                .state('payments', {
                    url: "/payments",
                    templateUrl: "adminui/payments.html",
                    controller: 'paymentsCtrl'
                })
                .state('purchases', {
                    url: "/purchases",
                    templateUrl: "adminui/purchases.html",
                    controller: 'purchasesCtrl'
                })
                .state('sessions', {
                    url: "/sessions",
                    templateUrl: "adminui/sessions.html",
                    controller: 'sessionsCtrl'
                })
                .state('tools', {
                    url: "/tools",
                    templateUrl: "adminui/tools.html",
                    controller: 'toolsCtrl'
                })
                .state('login', {
                    url: "/login",
                    templateUrl: "common/login.html",
                    controller: 'loginCtrl'
                })
                .state('register', {
                    url: "/register",
                    templateUrl: "common/register.html",
                    controller: 'registerCtrl'
                })


                ;
    }]);

