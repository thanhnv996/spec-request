ngapp.controller('purchasesCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getPurchaseCount = function () {
        httpApiGet($http, 'admin.purchases/search/'+JSON.stringify($scope.filtparams)+'/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load purchase error!");
            }
        });
    };
    
    $scope.loadPurchaseWithStage = function (begin, end) {
        httpApiGet($http, 'admin.purchases/search/' +  JSON.stringify($scope.filtparams)+'/'+begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.purchases= $response.data;   
                $scope.purchases.forEach(function (purchase) {
                    httpApiGet($http, 'users/' + purchase.userid, function (response) {
                        purchase.user = response.data;
                    });
                    httpApiGet($http, 'products/' + purchase.productid, function (response) {
                        purchase.product = response.data;
                    });
                });
                formatCreatedTime($scope.purchases);
            } else {
                alert("Load purchase error!");
            }
        });
    };
    
    $scope.currentPage = 1;
    $scope.arrayItemsPerPage = [5,10,25,50,100];
    $scope.selectedItemsPerPage = 5 ; 
    $scope.itemsPerPage = 5;
    $scope.paginationPurchase = function () {
        $scope.itemsPerPage = 5;
        $scope.maxSize = 5;

        $scope.getPurchaseCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / $scope.itemsPerPage);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
                    end = begin + $scope.itemsPerPage - 1;
            $scope.purchases = $scope.loadPurchaseWithStage(begin, end);
        });
    };
    
    $scope.changeItemsPerPage =  function(){
        $scope.itemsPerPage = $scope.selectedItemsPerPage;
        $scope.paginationPurchase();
    };

    $scope.loadPurchaseWihFilter = function () {
        $scope.paginationPurchase();
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loadPurchaseWihFilter();
    };

    $scope.deletePurchase = function (purchaseid, index) {
        httpApiDelete($http, 'admin.purchases/' + purchaseid, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.purchases.splice(index, 1);
                Notification.success({message: 'Deleted purchase success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.updatePurchase = function (purchaseid,purchase) {
        purchase.createdtime = new Date();
        purchase.id = purchaseid;
//        alert(JSON.stringify(purchase, null, 4));
        httpApiPutJson($http, 'admin.purchases/' + purchaseid, purchase, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.showEdit = false;
                $scope.paginationPurchase();
                Notification.success({message: 'Edited purchase success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.createNewPurchase = function (purchase) {
        purchase.createdtime = new Date();
//        alert(JSON.stringify(purchase, null, 4));
        httpApiPostJson($http, 'admin.purchases', purchase, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.totalItems = parseInt($scope.totalItems) + 1;
                $scope.currentPage = Math.ceil($scope.totalItems / $scope.itemsPerPage) + 1;
                Notification.success({message: 'You created a new purchase', positionY: 'bottom', positionX: 'left'});
                $scope.showAdd = false;
                $scope.cpurchase={};
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
    
    $scope.searchProductInputChange = function (searchValue) {
        httpApiGet($http, 'admin.products/searchname/' + searchValue, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.searchedProducts = $response.data;
//                alert(JSON.stringify($scope.searchedProducts, null, 4));
            }
        });
    };

    $scope.sort = 'asc';
    $scope.sortByPropertyPurchase = function (propertypurchase) {
        if ($scope.sort === 'asc') {
            $scope.purchases.sort(dynamicSortDesc(propertypurchase));
            $scope.sort = 'desc';
        } else {
            $scope.purchases.sort(dynamicSortAsc(propertypurchase));
            $scope.sort = 'asc';
        }
    };

    $scope.filtparams = {};
//    $scope.loadpurchases();
    $scope.paginationPurchase();

});