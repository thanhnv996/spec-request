ngapp.controller('paymentsCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getPaymentCount = function () {
        httpApiGet($http, 'admin.payments/search/'+JSON.stringify($scope.filtparams)+'/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load payment error!");
            }
        });
    };
    
    $scope.loadPaymentWithStage = function (begin, end) {
        httpApiGet($http, 'admin.payments/search/' +  JSON.stringify($scope.filtparams)+'/'+begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.payments= $response.data;   
                $scope.payments.forEach(function (payment) {
                    httpApiGet($http, 'users/' + payment.userid, function (response) {
                        payment.user = response.data;
                    });
                    httpApiGet($http, 'users/' + payment.userid2, function (response) {
                        payment.user2 = response.data;
                    });
                });
                formatCreatedTime($scope.payments);
            } else {
                alert("Load payment error!");
            }
        });
    };
    
    $scope.currentPage = 1;
    $scope.arrayItemsPerPage = [5,10,25,50,100];
    $scope.selectedItemsPerPage = 5 ; 
    $scope.itemsPerPage = 5;
    $scope.paginationPayment = function () {
        $scope.itemsPerPage = 5;
        $scope.maxSize = 5;

        $scope.getPaymentCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / $scope.itemsPerPage);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
                    end = begin + $scope.itemsPerPage - 1;
            $scope.payments = $scope.loadPaymentWithStage(begin, end);
        });
    };
    
    $scope.changeItemsPerPage =  function(){
        $scope.itemsPerPage = $scope.selectedItemsPerPage;
        $scope.paginationpayment();
    };

    $scope.loadPaymentWihFilter = function () {
        $scope.paginationPayment();
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loadPaymentWihFilter();
    };

    $scope.deletePayment = function (paymentid, index) {
        httpApiDelete($http, 'admin.payments/' + paymentid, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.payments.splice(index, 1);
                Notification.success({message: 'Deleted payment success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.updatePayment = function (paymentid,payment) {
        payment.createdtime = new Date();
        payment.id = paymentid;
//        alert(JSON.stringify(payment, null, 4));
        httpApiPutJson($http, 'admin.payments/' + paymentid, payment, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.showEdit = false;
                $scope.paginationPayment();
                Notification.success({message: 'Edited payment success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.createNewPayment = function (payment) {
        payment.createdtime = new Date();
//        alert(JSON.stringify(payment, null, 4));
        httpApiPostJson($http, 'admin.payments', payment, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.totalItems = parseInt($scope.totalItems) + 1;
                $scope.currentPage = Math.ceil($scope.totalItems / $scope.itemsPerPage) + 1;
                Notification.success({message: 'You created a new payment', positionY: 'bottom', positionX: 'left'});
                $scope.showAdd = false;
                $scope.cpayment={};
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
//                alert(JSON.stringify($scope.searchedUsers, null, 4));
            }
        });
    };
    
    $scope.searchAppInputChange = function (searchValue) {
        httpApiGet($http, 'admin.apps/searchname/' + searchValue, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.searchedApps = $response.data;
//                alert(JSON.stringify($scope.searchedApps, null, 4));
            }
        });
    };

    $scope.sort = 'asc';
    $scope.sortByPropertyPayment = function (propertypayment) {
        if ($scope.sort === 'asc') {
            $scope.payments.sort(dynamicSortDesc(propertypayment));
            $scope.sort = 'desc';
        } else {
            $scope.payments.sort(dynamicSortAsc(propertypayment));
            $scope.sort = 'asc';
        }
    };

    $scope.filtparams = {};
//    $scope.loadpayments();
    $scope.paginationPayment();

});