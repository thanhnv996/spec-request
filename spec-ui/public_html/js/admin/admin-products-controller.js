ngapp.controller('productsCtrl', function ($scope, $http, $window, $state, Notification) {

    $scope.getProductCount = function () {
        httpApiGet($http, 'admin.products/search/'+JSON.stringify($scope.filtparams)+'/count', function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.totalItems = $response.data;
            } else {
                alert("Load product error!");
            }
        });
    };
    
    $scope.loadProductWithStage = function (begin, end) {
        httpApiGet($http, 'admin.products/search/' +  JSON.stringify($scope.filtparams)+'/'+begin + '/' + end, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK) {
                $scope.products= $response.data;   
                $scope.products.forEach(function (product) {
                    httpApiGet($http, 'users/' + product.userid, function (response) {
                        product.user = response.data;
                    });
                    httpApiGet($http, 'apps/' + product.appcode, function (response) {
                        product.app = response.data;
                    });
                });
                formatCreatedTime($scope.products);
            } else {
                alert("Load product error!");
            }
        });
    };
    
    $scope.currentPage = 1;
    $scope.arrayItemsPerPage = [5,10,25,50,100];
    $scope.selectedItemsPerPage = 5 ; 
    $scope.itemsPerPage = 5;
    $scope.paginationProduct = function () {
        $scope.itemsPerPage = 5;
        $scope.maxSize = 5;

        $scope.getProductCount();
        $scope.pageCount = function () {
            return Math.ceil($scope.totalItems / $scope.itemsPerPage);
        };

        $scope.$watch('currentPage + itemsPerPage', function () {
            var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
                    end = begin + $scope.itemsPerPage - 1;
            $scope.products = $scope.loadProductWithStage(begin, end);
        });
    };
    
    $scope.changeItemsPerPage =  function(){
        $scope.itemsPerPage = $scope.selectedItemsPerPage;
        $scope.paginationProduct();
    };

    $scope.loadProductWihFilter = function () {
        $scope.paginationProduct();
    };

    $scope.clearFilter = function () {
        $scope.filtparams = {};
        $scope.loadProductWihFilter();
    };

    $scope.deleteProduct = function (productid, index) {
        httpApiDelete($http, 'admin.products/' + productid, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.products.splice(index, 1);
                Notification.success({message: 'Deleted product success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.updateProduct = function (productid,product) {
        product.createdtime = new Date();
        product.id = productid;
//        alert(JSON.stringify(product, null, 4));
        httpApiPutJson($http, 'admin.products/' + productid, product, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.showEdit = false;
                $scope.paginationProduct();
                Notification.success({message: 'Edited product success', positionY: 'bottom', positionX: 'left'});
            } else {
                var actionStatus = "Error " + $response.statusText;
                Notification.error({message: actionStatus, positionY: 'bottom', positionX: 'left'});
            }
        });
    };

    $scope.createNewProduct = function (product) {
        product.createdtime = new Date();
//        alert(JSON.stringify(product, null, 4));
        httpApiPostJson($http, 'admin.products', product, function ($response) {
            console.log($response);
            if ($response.status === HTTP_OK || $response.status === HTTP_NO_CONTENT) {
                $scope.totalItems = parseInt($scope.totalItems) + 1;
                $scope.currentPage = Math.ceil($scope.totalItems / $scope.itemsPerPage) + 1;
                Notification.success({message: 'You created a new product', positionY: 'bottom', positionX: 'left'});
                $scope.showAdd = false;
                $scope.cproduct={};
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
    $scope.sortByPropertyproduct = function (propertyproduct) {
        if ($scope.sort === 'asc') {
            $scope.products.sort(dynamicSortDesc(propertyproduct));
            $scope.sort = 'desc';
        } else {
            $scope.products.sort(dynamicSortAsc(propertyproduct));
            $scope.sort = 'asc';
        }
    };
    

    $scope.filtparams = {};
//    $scope.loadproducts();
    $scope.paginationProduct();

});