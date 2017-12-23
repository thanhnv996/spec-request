/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//giảm dần
function dynamicSortDesc(property) {
    var sortOrder = 1;
    if (property[0] === "-") {
        sortOrder = -1;
        property = property.substr(1);
    }
    return function (b, a) {
        var result = (a[property] < b[property]) ? -1 : (a[property] > b[property]) ? 1 : 0;
        return result * sortOrder;
    };
}
// tăng dần
function dynamicSortAsc(property) {
    var sortOrder = 1;
    if (property[0] === "-") {
        sortOrder = -1;
        property = property.substr(1);
    }
    return function (a, b) {
        var result = (a[property] < b[property]) ? -1 : (a[property] > b[property]) ? 1 : 0;
        return result * sortOrder;
    };
}

// format time
Number.prototype.padLeft = function (base, chr) {
    var len = (String(base || 10).length - String(this).length) + 1;
    return len > 0 ? new Array(len).join(chr || '0') + this : this;
};






// created token
String.prototype.splice = function (idx, rem, str) {
    return this.slice(0, idx) + str + this.slice(idx + Math.abs(rem));
};
createTokenValue = function () {
    var rand = URL.createObjectURL(new Blob([])).slice(-36).replace(/-/g, "");
    var rand = rand.splice(8, 0, "-");
    var rand = rand.splice(13, 0, "-");
    var rand = rand.splice(18, 0, "-");
    var rand = rand.splice(23, 0, "-");
    return rand;
};


// animate number counter from 0 to value
runCounter = function (length, totalruntime, divid) {
    var count = 0,
            speed = totalruntime / length;

    setTimeout(function () {
        time = speed;
    }, 2000);

    function timeout() {
        setTimeout(function () {
            count += 1;
            $("#" + divid).html(count);
            if (count === length) {
                return false;
            } else {
                timeout();
            }
        }, speed);
    }
    ;
    timeout();
}

formatDate = function (d) {
    var d = new Date(d);
    var dformat = [d.getHours().padLeft(),
        d.getMinutes().padLeft(),
        d.getSeconds().padLeft()].join(':')
            + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0' +
            [d.getDate().padLeft(),
                (d.getMonth() + 1).padLeft(),
                d.getFullYear()].join('-')

            ;
    return dformat;
};

formatCreatedTime = function (arrayObject) {
    for (var i = 0; i < arrayObject.length; i++) {
        arrayObject[i].createdtime = formatDate(arrayObject[i].createdtime);
    }
};

formatTime = function (arrayObject) {
    for (var i = 0; i < arrayObject; i++) {
        arrayObject[i].createdAt = formatDate(arrayObject[i].createdAt);
        arrayObject[i].deadline = formatDate(arrayObject[i].deadline);
        alert(arrayObject[i].deadline);
    }
};

//$(".active-element").click(function () {
//    alert(this.text());
//});
function strip(html)
{
   var tmp = document.createElement("DIV");
   tmp.innerHTML = html;
   return tmp.textContent||tmp.innerText;
}