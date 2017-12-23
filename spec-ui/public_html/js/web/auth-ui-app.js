'use strict';

var ngapp = angular.module('auth-ui-app', ['ngAnimate', 'ui.router', 'ui-notification', 'ui.bootstrap', 'chieffancypants.loadingBar'])
        .config(function ($httpProvider) {
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
    }, 1500);

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
        $urlRouterProvider.otherwise("/dashbroad");

        $stateProvider
                .state('intro', {
                    url: "/",
                    templateUrl: "index.html",
                    controller: 'sessionstatusCtrl'
                })
                .state('me', {
                    url: "/me",
                    templateUrl: "webui/me.html",
                    controller: 'meCtrl'
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
                .state('users', {
                    url: "/users",
                    templateUrl: "webui/users.html",
                    controller: 'usersCtrl'
                })
                .state('users.detail', {
                    url: "/{userid}",
                    templateUrl: "webui/user.html",
                    controller: 'userCtrl',
                    resolve: {
                        userid: ['$stateParams', function ($stateParams) {
                                return $stateParams.userid;
                            }],
                        userid2: ['$stateParams', function ($stateParams) {
                                return $stateParams.userid;
                            }]
                    }
                })
                .state('add-ticket', {
                    url: '/add-ticket',
                    templateUrl: 'webui/add-ticket.html',
                    controller: 'ticketCtrl'
                })
                
                
//                Công việc tôi tạo
                .state('me-create-all', {
                    url: '/me-create-all',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meCreateCtrl'
                })
                .state('me-create-new', {
                    url: '/me-create-new',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meCreateCtrl'
                })
                .state('me-create-inprogress', {
                    url: '/me-create-inprogress',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meCreateCtrl'
                })
                .state('me-create-resolved', {
                    url: '/me-create-resolved',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meCreateCtrl'
                })
                .state('me-create-outofdate', {
                    url: '/me-create-outofdate',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meCreateCtrl'
                })
                
                
                
//                công việc tôi liên quan
                .state('me-relate-all', {
                    url: '/me-relate-all',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRelateCtrl'
                })
                .state('me-relate-new', {
                    url: '/me-relate-new',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRelateCtrl'
                })
                .state('me-relate-inprogress', {
                    url: '/me-relate-inprogress',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRelateCtrl'
                })
                .state('me-relate-resolved', {
                    url: '/me-relate-resolved',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRelateCtrl'
                })
                .state('me-relate-outofdate', {
                    url: '/me-relate-outofdate',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRelateCtrl'
                })
                
//                công việc tôi được giao
                .state('me-requested-all', {
                    url: '/me-requested-all',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRequestCtrl'
                })
                .state('me-requested-new', {
                    url: '/me-requested-new',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRequestCtrl'
                })
                .state('me-requested-inprogress', {
                    url: '/me-requested-inprogress',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRequestCtrl'
                })
                .state('me-requested-resolved', {
                    url: '/me-requested-resolved',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRequestCtrl'
                })
                .state('me-requested-outofdate', {
                    url: '/me-requested-outofdate',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'meRequestCtrl'
                })
                
                
//                công việc của team
                .state('all-ticket-team', {
                    url: '/all-ticket-team',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketTeamCtrl'
                })
                .state('new-ticket-team', {
                    url: '/new-ticket-team',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketTeamCtrl'
                })
                .state('inprogress-ticket-team', {
                    url: '/inprogress-ticket-team',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketTeamCtrl'
                })
                .state('resolved-ticket-team', {
                    url: '/resolved-ticket-team',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketTeamCtrl'
                })
                .state('outofdate-ticket-team', {
                    url: '/outofdate-ticket-team',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketTeamCtrl'
                })
                
                
//                công việc của bộ phận it
                .state('all-ticket-partit', {
                    url: '/all-ticket-partit',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketPartITCtrl'
                })
                .state('new-ticket-partit', {
                    url: '/new-ticket-partit',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketPartITCtrl'
                })
                .state('inprogress-ticket-partit', {
                    url: '/inprogress-ticket-partit',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketPartITCtrl'
                })
                .state('resolved-ticket-partit', {
                    url: '/resolved-ticket-partit',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketPartITCtrl'
                })
                .state('outofdate-ticket-partit', {
                    url: '/outofdate-ticket-partit',
                    templateUrl: 'webui/table-ticket.html',
                    controller: 'ticketPartITCtrl'
                })
                ;
    }]);
