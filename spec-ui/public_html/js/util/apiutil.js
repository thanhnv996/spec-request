function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
        vars[key] = value;
    });
    return vars;
}

function httpApiGet($http, $uri, runFunc) {
    $http({
        method: 'GET',
        url: base_api + $uri
    }).then(function (response) {
        runFunc && runFunc(response);
    }, function (response) {
        runFunc && runFunc(response);
    });
}


function httpApiPost($http, $uri, $params, runFunc) {
    $http({
        method: 'POST',
        url: base_api + $uri,
        data: $.param($params),
        headers: {'Content-Type': 'application/x-www-form-urlencoded',
        'Access-Control-Allow-Origin':' *'}
    }).then(function (response) {
        runFunc && runFunc(response);
    }, function (response) {
        runFunc && runFunc(response);
    });
}

function httpApiPostJson($http, $uri, $json, runFunc) {
    $http({
        method: 'POST',
        url: base_api + $uri,
        data: $json,
        headers: {'Content-Type': 'application/json; charset=UTF-8'}
    }).then(function (response) {
        runFunc && runFunc(response);
    }, function (response) {
        runFunc && runFunc(response);
    });
}

function httpApiPut($http, $uri, $params, runFunc) {
    $http({
        method: 'PUT',
        url: base_api + $uri,
        data: $.param($params),
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).then(function (response) {
        runFunc && runFunc(response);
    }, function (response) {
        runFunc && runFunc(response);
    });
}

function httpApiPutJson($http, $uri, $json, runFunc) {
    $http({
        method: 'PUT',
        url: base_api + $uri,
        data: $json,
        headers: {'Content-Type': 'application/json; charset=UTF-8'}
    }).then(function (response) {
        runFunc && runFunc(response);
    }, function (response) {
        runFunc && runFunc(response);
    });
}

function httpApiDelete($http, $uri, runFunc) {
    $http({
        method: 'DELETE',
        url: base_api + $uri
    }).then(function (response) {
        runFunc && runFunc(response);
    }, function (response) {
        runFunc && runFunc(response);
    });
}


