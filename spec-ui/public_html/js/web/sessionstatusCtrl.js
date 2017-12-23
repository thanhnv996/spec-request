/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


ngapp.controller('sessionstatusCtrl', function ($scope, $http, $state, authsession, Notification) {
    $scope.$watch(function () {
        return authsession.getUser();
    },
            function (value) {
                $scope.user = value;
            }
    );

    $scope.showLogin = function () {
        $state.go('login');
    };

    $scope.showRegister = function () {
        $state.go('register');
    };

    $scope.showAdd = function () {
        $state.go('add-ticket');
    };

    $scope.showTicket = function (id) {

//        $scope.loadUser();
        $scope.getComment(id);
        $scope.getTicket1(id);
        $scope.getRelaters(id);

    };
    $scope.getTicket1 = function (id) {
        httpApiGet($http, 'ticket/id/' + id, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.ticketShow = $response.data;
                $scope.ticketShow.createdAt = formatDate($scope.ticketShow.createdAt);
                $scope.ticketShow.deadline = formatDate($scope.ticketShow.deadline);
                console.log($scope.ticketShow);
            } else {
                Notification.error({message: $response.statusText, positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.markRead = function (ticket_id, status) {
        var data = {};
        data.ticket_id = ticket_id;
        data.status = status;
        httpApiPost($http, 'ticket/mark', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                Notification.success({message: "success", positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message: $response.statusText, positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.getRelaters = function (id) {
        $scope.strRelater = "";
        httpApiGet($http, 'ticket/id/' + id + '/relaters', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                if ($response.data.ticketRelaters.length === 1) {
                    $scope.relaters = [];
                    $scope.relaters.push($response.data.ticketRelaters);
                } else {
                    $scope.relaters = $response.data.ticketRelaters;
                }
//                $scope.ticketShow.createdAt = formatDate($scope.ticketShow.createdAt);
//                $scope.ticketShow.deadline = formatDate($scope.ticketShow.deadline);
                for (var i = 0; i < $scope.relaters.length; i++) {
                    $scope.relaters[i].createdAt = formatDate($scope.relaters[i].createdAt);
                    $scope.strRelater += "," + $scope.relaters[i].employeeId.id;
                }
                console.log("relaters:" + $scope.relaters);
            } else {
                Notification.error({message: $response.statusText, positionY: 'bottom', positionX: 'right'});
            }
        });
    }

    $scope.getComment = function (id) {
        httpApiGet($http, 'ticket/' + id + '/comment', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                if ($response.data.ticketThread.length === 1) {
                    $scope.comments = [];
                    $scope.comments.push($response.data.ticketThread);
                } else {
                    $scope.comments = $response.data.ticketThread;
                }

//                $scope.ticketShow.createdAt = formatDate($scope.ticketShow.createdAt);
//                $scope.ticketShow.deadline = formatDate($scope.ticketShow.deadline);
                for (var i = 0; i < $scope.comments.length; i++) {
                    $scope.comments[i].createdAt = formatDate($scope.comments[i].createdAt);
                    $scope.comments[i].content = strip($scope.comments[i].content);
                }
                console.log($scope.comments);
            } else {
                Notification.error({message: $response.statusText, positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.addComment = function () {
        var dataPost = {};
        dataPost.ticket_id = $scope.ticketShow.id;
        dataPost.content = $('textarea#contentComment').val();
        httpApiPost($http, 'comment', dataPost, function ($response) {
            if ($response.status === HTTP_OK) {
                $scope.getComment(dataPost.ticket_id);
            } else {
                Notification.error({message: $response.statusText, positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.loadUser = function () {
        httpApiGet($http, 'employee', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.users = $response.data.employees;
            } else {
                $state.go('login');
            }
        });
    };

    $scope.putTicket = function () {
//        alert($("select#rating").val());
//        alert($("textarea#comment_rating").val());
//alert($("select#partcode").val());
//console.log($("select#partcode").val());

        var data = {};
        data.ticket_id = $scope.ticketShow.id;
//        data.subject = $scope.subject;

        if ("? undefined:undefined ?" !== $("select#priority").val())
            data.priority = $("select#priority").val();

        if ($("textarea#reason_change_priority").val() !== "")
            data.reason_change_priority = $("textarea#reason_change_priority").val();

        if ("? undefined:undefined ?" !== $("select#partcode").val())
            data.partcode = $("select#partcode").val();

//        if($("textarea#reason_change_priority").val()!=="") 
//        data.assigned_to = $scope.assigned_to;
//        data.content = $('textarea#myarea').val();
        if ($("input#deadline").val() !== "")
            data.deadline = $("input#deadline").val();

        if ($("textarea#reason_change_deadline").val() !== "")
            data.reason_change_deadline = $("textarea#reason_change_deadline").val();

        if ("? undefined:undefined ?" !== $("select#rating").val() && $("select#rating").val() != null)
            data.rating = $("select#rating").val();
        else
            data.rating = -1;
        alert(data.rating);

        if ($("textarea#comment_rating").val() !== "")
            data.comment_rating = $("textarea#comment_rating").val();

        console.log(data);
        console.log();
        httpApiPut($http, 'ticket/update', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.getTicket1($scope.ticketShow.id);
                $scope.getComment($scope.ticketShow.id);
//                $('#modal_rating').modal('hide');
//                $('#modal_priority').modal('hide');
//                $('#modal_parit').modal('hide');
//                $('#modal_deadline').modal('hide');
//                $('#modal_relater').modal('hide');
//                $('#modal_assign').modal('hide');
//                $("select#priority").val("").change();
//                $("textarea#reason_change_priority").val("").change();
//                $("select#partcode").val("");
//                $("input#deadline").val("");
//                $("textarea#reason_change_deadline").val("");
                $("select#rating").val("");
                $("textarea#comment_rating").val("");

                Notification.success({message: 'Sửa công việc thành công', positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message: 'Lỗi', positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.putRating = function () {

        var data = {};
        data.ticket_id = $scope.ticketShow.id;
        if ("? undefined:undefined ?" !== $("select#rating").val() && $("select#rating").val() != null)
            data.rating = $("select#rating").val();
        else
            data.rating = -1;

        if ($("textarea#comment_rating").val() !== "")
            data.comment_rating = $("textarea#comment_rating").val();

        console.log(data);
        console.log();
        httpApiPut($http, 'ticket/update/rating', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.getTicket1($scope.ticketShow.id);
                $scope.getComment($scope.ticketShow.id);
                Notification.success({message: 'Sửa công việc thành công', positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message: 'Lỗi', positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.putDeadline = function () {

        var data = {};
        data.ticket_id = $scope.ticketShow.id;


        if ($("input#deadline").val() !== "")
            data.deadline = $("input#deadline").val();

        if ($("textarea#reason_change_deadline").val() !== "")
            data.reason_change_deadline = $("textarea#reason_change_deadline").val();


        console.log(data);
        console.log();
        httpApiPut($http, 'ticket/update/deadline', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.getTicket1($scope.ticketShow.id);
                $scope.getComment($scope.ticketShow.id);
                Notification.success({message: 'Sửa công việc thành công', positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message: 'Lỗi', positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.putPriority = function () {

        var data = {};
        data.ticket_id = $scope.ticketShow.id;

        if ("? undefined:undefined ?" !== $("select#priority").val())
            data.priority = $("select#priority").val();

        if ($("textarea#reason_change_priority").val() !== "")
            data.reason_change_priority = $("textarea#reason_change_priority").val();

        console.log(data);
        console.log();
        httpApiPut($http, 'ticket/update/priority', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.getTicket1($scope.ticketShow.id);
                $scope.getComment($scope.ticketShow.id);
                Notification.success({message: 'Sửa công việc thành công', positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message: 'Lỗi', positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.putAssignto = function () {

        var assigned_to = 0;
        for (var i = 0; i < $scope.users.length; i++) {
            var id = "input#radio_" + i;
            console.log($(id));
            var checkList = $(id);
//            console.log("combobox"+checkList);
            var checked = checkList["0"].checked;
            var value = checkList["0"].attributes["0"].value;

            if (checked == true) {
                assigned_to = value;
            }
//            console.log(checked);
//            console.log(value);
        }
//        console.log(list_relater_id);
//        list_relater_id = list_relater_id.substr(1);
        var data = {};
        data.ticket_id = $scope.ticketShow.id;
        data.assigned_to = assigned_to;
//        console.log(data);
//
        httpApiPut($http, 'ticket/update/assignto', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.getTicket1($scope.ticketShow.id);
                $scope.getComment($scope.ticketShow.id);
                Notification.success({message: 'Sửa công việc thành công', positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message: $response.data, positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.putRelater = function () {
        var list_relater_id = "";
        for (var i = 0; i < $scope.users.length; i++) {
            var id = "input#md_checkbox_" + i;
            var checkList = $(id);
            var checked = checkList["0"].checked;
            var value = checkList["0"].attributes["1"].value;

            if (checked == true) {
                list_relater_id += "," + value;
            }
            console.log(checked);
            console.log(value);
        }
        console.log(list_relater_id);
        list_relater_id = list_relater_id.substr(1);
        var data = {};
        data.ticket_id = $scope.ticketShow.id;
        data.list_relater_id = list_relater_id;

        httpApiPut($http, 'ticket/update/relater', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
//                $scope.getTicket1($scope.ticketShow.id);
//                $scope.getComment($scope.ticketShow.id);  
                $scope.showTicket($scope.ticketShow.id);
                Notification.success({message: 'Sửa công việc thành công', positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message: $response.data, positionY: 'bottom', positionX: 'right'});
            }
        });
    };

    $scope.putPartIt = function () {

        var data = {};
        data.ticket_id = $scope.ticketShow.id;
        data.subject = null;
        if ("? undefined:undefined ?" !== $("select#partcode").val())
            data.partcode = $("select#partcode").val();
        data.content = null;
        console.log(data);
        console.log();
        httpApiPut($http, 'ticket/update/sub-attribute', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.getTicket1($scope.ticketShow.id);
                $scope.getComment($scope.ticketShow.id);
                Notification.success({message: 'Sửa công việc thành công', positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message: 'Lỗi', positionY: 'bottom', positionX: 'right'});
            }
        });
    };


    $scope.putStatus = function () {

        var data = {};
        data.ticket_id = $scope.ticketShow.id;
        if ("? undefined:undefined ?" !== $("select#status").val())
            data.status = $("select#status").val();
        console.log(data);
        console.log();
        httpApiPut($http, 'ticket/update/status', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_ACCEPTED) {
                $scope.getTicket1($scope.ticketShow.id);
                $scope.getComment($scope.ticketShow.id);
                Notification.success({message: 'Sửa công việc thành công', positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message: $response.data, positionY: 'bottom', positionX: 'right'});
            }
        });
    };

//    $scope.getMe = function () {
//        httpApiGet($http, 'me/info', function ($response) {
//            console.log($response);
//            if ($response.status === HTTP_OK) {
//                $scope.me = $response.data;
//                $scope.me.urlProfilePic = base_api + 'me/profilepic';
//                authsession.setUser($response.data);
//            } else {
//                $state.go('login');
//                authsession.setUser({});
//            }
//        });
//    };
    $scope.logout = function () {
        httpApiDelete($http, 'login', function ($response) {
            if ($response.status === HTTP_GONE) {
                authsession.setUser({});
                $state.go('login');
            }
        });
    };
    $scope.searchInputChange = function (searchValue) {
        httpApiGet($http, 'users/find/' + searchValue + '/0/3', function ($response) {
//            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.searchedUsers = $response.data;
                $scope.searchedUsers.forEach(function (user) {
                    user.urlProfilePic = base_api + 'users/' + user.id + '/profilepic';
                });
            }
        });
        httpApiGet($http, 'apps/find/' + searchValue + '/0/3', function ($response) {
            if ($response.status === HTTP_OK) {
                $scope.searchedApps = $response.data;
            }
        });
        httpApiGet($http, 'products/find/' + searchValue + '/0/3 ', function ($response) {
            if ($response.status === HTTP_OK) {
                $scope.searchedProducts = $response.data;
            }
        });
    };

    $scope.showUser = function ($userid, $value) {
//        alert('click');
        $(".results").hide();
        $scope.keyword = $value;
        $state.go('user', {'userid': $userid});
    };
    $scope.showApp = function ($appcode, $value) {
        $(".results").hide();
        $scope.keyword = $value;
        $state.go('app', {'appcode': $appcode});
    };
//    $scope.getMe();
});
