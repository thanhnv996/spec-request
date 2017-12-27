ngapp.controller('ticketCtrl', function ($rootScope, $state, $scope, $http, $window, Notification, authsession) {
    $scope.postTicket = function () {
        var list_relater_id = "";
        for (var i = 0; i < $scope.users.length; i++) {
            var id = "input#create_md_checkbox_" + i;
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


        var data ={};
        data.subject = $scope.subject;
        data.priority = $scope.priority;
        data.partcode = $scope.partit;
        data.assigned_to = $scope.assigned_to;
        data.content = $('textarea#myarea').val();
        data.deadline = $scope.deadline;
        data.stringListRelaterId = list_relater_id;
        console.log(data);
        console.log();
         httpApiPost($http, 'ticket', data, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                Notification.success({message: 'Tạo công việc thành công', positionY: 'bottom', positionX: 'right'});
            } else {
                Notification.error({message:'Thông tin không chính xác' , positionY: 'bottom', positionX: 'right'});
            }
        });
    };
    
    $scope.getAllUser = function () {
        httpApiGet($http, 'employee', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.users = $response.data.employees;
            } else {
                $state.go('login');
            }
        });
    };
    
    $scope.getAllUser();
});