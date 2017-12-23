ngapp.controller('loginCtrl', function ($scope, $http, $window, $state, authsession, Notification) {
    $(".sidebar").hide();
    $(".navbar").hide();
    $("body").css('background-color', '#00BCD4');
    $scope.login = function (login_id, password) {
        httpApiPost($http, 'login', {'login_id': login_id, 'password': password}, function ($response) {
//            alert($response.status);
//            alert($response.data);
            if ($response.status === HTTP_ACCEPTED) {
                Notification.success({message: 'Login success', positionY: 'bottom', positionX: 'right'});
//                $state.go('users');
                httpApiGet($http, 'ticket/me/requested', function ($response) {
                    if ($response.status === HTTP_OK) {
                        $(".sidebar").show();
                        $(".navbar").show();
                        $("body").css('background-color', '#e9e9e9');
                        $state.go('me-requested-all');                 
//                        setTimeout(window.location.reload.bind(window.location), 100);
                        authsession.setUser($response.data);
                    }
                });
            } else if ($response.status === HTTP_INTERNAL_SERVER_ERROR) {
                Notification.error({message: "Lỗi máy chủ nội bộ", positionY: 'bottom', positionX: 'right'});
            }else {
                Notification.error({message: $response.data, positionY: 'bottom', positionX: 'right'});
            }
        });
    };
    $scope.showRegister = function () {
        $state.go('register');
    };



    (function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id))
            return;
        js = d.createElement(s);
        js.id = id;
        js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.7&appId=293862311077754";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));


    window.fbAsyncInit = function () {
        FB.init({
            appId: '293862311077754',
            cookie: true,
            xfbml: true,
            version: 'v2.5'
        });

        FB.getLoginStatus(function (response) {
            statusChangeCallback(response);
        });

    };

    function statusChangeCallback(response) {
        if (response.status === 'connected') {
            console.log(response);
            $scope.stateLoginFacebook = 'true';
        }
        else if (response.status === 'not_authorized') {
            console.log(response);
        }
        else {
            console.log(response);
        }
    }

    $scope.RequestLoginFB = function () {
        FB.login(function (response) {
            GetInfo();
        }, {scope: 'public_profile,email'});
    }

    //người dùng đã đăng nhập facebook
    function GetInfo() {
        FB.api('/me?fields=id,name,email', function (response) {
            var user = {};
            user.password = 'a' + response.id;
            user.name = response.name;
            if (response.email)
                user.email = response.email;
            else
                user.email = response.id + '_nullmail@gmail.com';
            user.userid = response.name.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a").replace(/\ /g, '').replace(/đ/g, "d").replace(/đ/g, "d").replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y").replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u").replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o").replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e").replace(/ì|í|ị|ỉ|ĩ/g, "i").replace(/'/g, "").toLowerCase();
            user.externaluserid = response.id;
            httpApiPost($http, 'users', user, function ($response) {
                if ($response.status) {
                    $scope.login(user.externaluserid, user.password);
                }
            });

        });
    }
});

ngapp.controller('registerCtrl', function ($scope, $http, $window, $state, Notification) {
    $scope.registerUser = function (userid, email, name, password, password2) {
        if (password !== password2) {
            $scope.loginError = "Passwords not match!";
            return;
        }
        httpApiPost($http, 'users', {'userid': userid, 'name': name, 'password': password, 'email': email}, function ($response) {
            if ($response.status === HTTP_CREATED) {
                $scope.loginError = "Registered user id:" + $response.data.id;
                Notification.success({message: 'You created an app', positionY: 'bottom', positionX: 'right'});
                $state.go('login');
            } else {
                Notification.error({message: 'Email or username registed', positionY: 'bottom', positionX: 'right'});
                $scope.loginError = "Error " + $response.data;
            }
        });
    };
});
