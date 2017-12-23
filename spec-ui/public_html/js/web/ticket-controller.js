ngapp.controller('ticketCtrl', function ($rootScope, $state, $scope, $http, $window, Notification, authsession) {
    $scope.postTicket = function () {
        var data ={};
        data.subject = $scope.subject;
        data.priority = $scope.priority;
        data.partcode = $scope.partit;
        data.assigned_to = $scope.assigned_to;
        data.content = $('textarea#myarea').val();
        data.deadline = $scope.deadline;
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