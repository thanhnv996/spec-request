
//PICK SKIN FOR NAV
$(".demo-choose-skin li").click(function () {
    var theme = 'theme-' + $(this).attr('data-theme');
//    alert(theme);
    $(".demo-choose-skin li").removeClass();
    $(this).addClass("active");
    $("body").removeClass();
    $("body").addClass(theme);
});

// ACTIVE MENU
$(".list li a").click(function () {
    $(".list li a").removeClass();
    $(".list li").removeClass();
    $(this).addClass("active-element");
});

// ACTIVE MENU
$(".list li ul li").click(function () {
//    alert('click');
    $(".list li ul li a").css('color', 'black');
    $(this).children("a").css('color', '#03A9F4');
});

// LOAD ACTIVE MENU
var href = window.location.href;
var pos = href.indexOf("#!/");
href = href.substring(pos + 3, href.length);
var pos2 = href.indexOf('/');
if (pos2 > 0) {
    href = href.substring(0, pos2);
}
$(".list li a").removeClass("active-element");
$('#' + href + 's').children("a").addClass("active-element");
$('#' + href).addClass("active-element");
//alert(href);

//DISPLAY SUGGESTION SEARCH WHEN CLICK BOX-SEARCH
//SHOW
$("#input-search").click(function () {
    $(".results").show();
});
//HIDE
//$("div:not(#input-search)").click(function(){
//   $(".results").hide(); 
//});
$(".list li").click(function () {
//    alert($(this).children("a").children("span").text());
    header = $(this).children("a").children("span").text();
});