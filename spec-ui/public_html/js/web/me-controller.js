ngapp.controller('meCtrl', function ($rootScope, $state, $scope, $http, $window, Notification, authsession) {
//    $scope.getMe = function () {
//        httpApiGet($http, 'me/info', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.user = $response.data;
//                authsession.setUser($scope.user);
//            } else {
//                $state.go('login');
//                authsession.setUser({});
//            }
//        });
//    };

    $scope.logout = function () {
        httpApiDelete($http, 'login', function ($response) {
            console.log($response);
            if ($response.status === HTTP_GONE) {
                $state.go('login');
                authsession.setUser({});
            } else {
                // alert($response.data);
                //$scope.loginError = $response.data;
            }
        });
    };

});

ngapp.controller('meCreateCtrl', function ($rootScope, $state, $scope, $http, $window, Notification, authsession) {

//    alert($scope.statusSelect);
    $scope.getMeTickets = function () {
        httpApiGet($http, 'ticket/me/created', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {

                console.log($response.data.tickets);

                $scope.tickets = $response.data.tickets;
//                formatTime($scope.tickets);
                for (var i = 0; i < $scope.tickets.length; i++) {
                    $scope.tickets[i].createdAt = formatDate($scope.tickets[i].createdAt);
                    $scope.tickets[i].deadline = formatDate($scope.tickets[i].deadline);
                }

            } else if ($response.status === HTTP_UNAUTHORIZED) {
                $state.go("login");
            }
            else {
                alert("Error");
            }
        });
    };
    $scope.getMeTickets();
    
    $scope.getReader = function(){
        $scope.strReader="";
        httpApiGet($http, 'ticket/me/read', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                if ($response.data.reader.length === 1) {
                    $scope.readers = [];
                    $scope.readers.push($response.data.reader);
                } else {
                    $scope.readers = $response.data.reader;
                }
//                $scope.ticketShow.createdAt = formatDate($scope.ticketShow.createdAt);
//                $scope.ticketShow.deadline = formatDate($scope.ticketShow.deadline);
                for (var i = 0; i < $scope.readers.length; i++) {
                    $scope.strReader +=","+$scope.readers[i].ticketId;
                }
                console.log($scope.strReader);
            } else {
                Notification.error({message: $response.statusText, positionY: 'bottom', positionX: 'right'});
            }
        });
    };
    $scope.getReader();
    

    //** cài đặt trạng thái công việc trong tab
    var href = window.location.href;
    if (href.indexOf('all') !== -1)
        $scope.statusSelect = 'all';
    if (href.indexOf('new') !== -1)
        $scope.statusSelect = 'new';
    if (href.indexOf('inprogress') !== -1)
        $scope.statusSelect = 'inprogress';
    if (href.indexOf('resolved') !== -1)
        $scope.statusSelect = 'resolved';
    if (href.indexOf('outofdate') !== -1)
        $scope.statusSelect = 'outofdate';

    $("a").click(function () {
        var href = $(this).text().toLowerCase();
        if (href.indexOf('all') !== -1)
            status = 'all';
        if (href.indexOf('new') !== -1)
            status = 'new';
        if (href.indexOf('inprogress') !== -1)
            status = 'inprogress';
        if (href.indexOf('resolved') !== -1)
            status = 'resolved';
        if (href.indexOf('outofdate') !== -1)
            status = 'outofdate';
    });
    console.log("click" + status);
    if (status !== 'null')
        $scope.statusSelect = status;

    //** cài đặt tiêu đề cho tab
    if (href.indexOf('me-create') !== -1)
        $scope.header = 'Công việc tôi yêu cầu';
    if (href.indexOf('me-relate') !== -1)
        $scope.header = 'Công việc liên quan';
    if (href.indexOf('me-requested') !== -1)
        $scope.header = 'Việc tôi được giao';
    if (href.indexOf('team') !== -1)
        $scope.header = 'Công việc của team';
    if (href.indexOf('partit') !== -1)
    {
        $scope.header = 'Công việc của bộ phận IT';
        $scope.owner = 'partit';
    }
//    alert($scope.header);
    if ($scope.header == null)
        $scope.header = header;
});


ngapp.controller('meRelateCtrl', function ($rootScope, $state, $scope, $http, $window, Notification, authsession) {

//    alert($scope.statusSelect);
    $scope.getMeTickets = function () {
        httpApiGet($http, 'ticket/me/related', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {

                var relaters = $response.data.ticketRelaters;
                $scope.tickets = {};
                for (var i = 0; i < relaters.length; i++) {
                    $scope.tickets[i] = relaters[i].ticketId;
                    $scope.tickets[i].deadline = formatDate($scope.tickets[i].deadline);
                    console.log($scope.tickets[i].deadline);
                }

                console.log($scope.tickets);


            } else if ($response.status === HTTP_UNAUTHORIZED) {
                $state.go("login");
            }
            else {
                alert("Error");
            }
        });
    };
    $scope.getMeTickets();
    
    $scope.getReader = function(){
        $scope.strReader="";
        httpApiGet($http, 'ticket/me/read', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                if ($response.data.reader.length === 1) {
                    $scope.readers = [];
                    $scope.readers.push($response.data.reader);
                } else {
                    $scope.readers = $response.data.reader;
                }
//                $scope.ticketShow.createdAt = formatDate($scope.ticketShow.createdAt);
//                $scope.ticketShow.deadline = formatDate($scope.ticketShow.deadline);
                for (var i = 0; i < $scope.readers.length; i++) {
                    $scope.strReader +=","+$scope.readers[i].ticketId;
                }
                console.log($scope.strReader);
            } else {
                Notification.error({message: $response.statusText, positionY: 'bottom', positionX: 'right'});
            }
        });
    };
    $scope.getReader();

    var href = window.location.href;
    if (href.indexOf('all') !== -1)
        $scope.statusSelect = 'all';
    if (href.indexOf('new') !== -1)
        $scope.statusSelect = 'new';
    if (href.indexOf('inprogress') !== -1)
        $scope.statusSelect = 'inprogress';
    if (href.indexOf('resolved') !== -1)
        $scope.statusSelect = 'resolved';
    if (href.indexOf('outofdate') !== -1)
        $scope.statusSelect = 'outofdate';

    $("a").click(function () {
        var href = $(this).text().toLowerCase();
        if (href.indexOf('all') !== -1)
            status = 'all';
        if (href.indexOf('new') !== -1)
            status = 'new';
        if (href.indexOf('inprogress') !== -1)
            status = 'inprogress';
        if (href.indexOf('resolved') !== -1)
            status = 'resolved';
        if (href.indexOf('outofdate') !== -1)
            status = 'outofdate';
    });
    console.log("click" + status);
    if (status !== 'null')
        $scope.statusSelect = status;


    //** cài đặt tiêu đề cho tab
    if (href.indexOf('me-create') !== -1)
        $scope.header = 'Công việc tôi yêu cầu';
    if (href.indexOf('me-relate') !== -1)
        $scope.header = 'Công việc liên quan';
    if (href.indexOf('me-requested') !== -1)
        $scope.header = 'Việc tôi được giao';
    if (href.indexOf('team') !== -1)
        $scope.header = 'Công việc của team';
    if (href.indexOf('partit') !== -1)
    {
        $scope.header = 'Công việc của bộ phận IT';
        $scope.owner = 'partit';
    }
//    alert($scope.header);
    if ($scope.header == null)
        $scope.header = header;
});


ngapp.controller('meRequestCtrl', function ($rootScope, $state, $scope, $http, $window, Notification, authsession) {
    var href = window.location.href;
    if (href.indexOf('all') !== -1)
        $scope.statusSelect = 'all';
    if (href.indexOf('new') !== -1)
        $scope.statusSelect = 'new';
    if (href.indexOf('inprogress') !== -1)
        $scope.statusSelect = 'inprogress';
    if (href.indexOf('resolved') !== -1)
        $scope.statusSelect = 'resolved';
    if (href.indexOf('outofdate') !== -1)
        $scope.statusSelect = 'outofdate';
//    alert($scope.statusSelect);
    $scope.getMeTickets = function () {
        httpApiGet($http, 'ticket/me/requested', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {

                console.log($response.data.tickets.length);
                if($response.data.tickets.length==undefined){
//                    alert("1");
                    $scope.tickets=[];
                    $scope.tickets.push($response.data.tickets);
                }else{
                    $scope.tickets = $response.data.tickets;
                }
                
//                formatTime($scope.tickets);
                for (var i = 0; i < $scope.tickets.length; i++) {
                    $scope.tickets[i].createdAt = formatDate($scope.tickets[i].createdAt);
                    $scope.tickets[i].deadline = formatDate($scope.tickets[i].deadline);
                }
                console.log($scope.tickets);

            } else if ($response.status === HTTP_UNAUTHORIZED) {
                $state.go("login");
            }
            else {
                alert("Error");
            }
        });
    };
    $scope.getMeTickets();
    
    $scope.getReader = function(){
        $scope.strReader="";
        httpApiGet($http, 'ticket/me/read', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                if ($response.data.reader.length === 1) {
                    $scope.readers = [];
                    $scope.readers.push($response.data.reader);
                } else {
                    $scope.readers = $response.data.reader;
                }
//                $scope.ticketShow.createdAt = formatDate($scope.ticketShow.createdAt);
//                $scope.ticketShow.deadline = formatDate($scope.ticketShow.deadline);
                for (var i = 0; i < $scope.readers.length; i++) {
                    $scope.strReader +=","+$scope.readers[i].ticketId;
                }
                console.log($scope.strReader);
            } else {
                Notification.error({message: $response.statusText, positionY: 'bottom', positionX: 'right'});
            }
        });
    };
    $scope.getReader();

    $("a").click(function () {
        var href = $(this).text().toLowerCase();
        if (href.indexOf('all') !== -1)
            status = 'all';
        if (href.indexOf('new') !== -1)
            status = 'new';
        if (href.indexOf('inprogress') !== -1)
            status = 'inprogress';
        if (href.indexOf('resolved') !== -1)
            status = 'resolved';
        if (href.indexOf('outofdate') !== -1)
            status = 'outofdate';
    });
    console.log("click" + status);
    if (status !== 'null')
        $scope.statusSelect = status;


    //** cài đặt tiêu đề cho tab
    if (href.indexOf('me-create') !== -1)
        $scope.header = 'Công việc tôi yêu cầu';
    if (href.indexOf('me-relate') !== -1)
        $scope.header = 'Công việc liên quan';
    if (href.indexOf('me-requested') !== -1)
        $scope.header = 'Việc tôi được giao';
    if (href.indexOf('team') !== -1)
        $scope.header = 'Công việc của team';
    if (href.indexOf('partit') !== -1)
    {
        $scope.header = 'Công việc của bộ phận IT';
        $scope.owner = 'partit';
    }
//    alert($scope.header);
    if ($scope.header == null)
        $scope.header = header;
});

