$(document).ready(function () {
    $(".userName").load("/studentInfo", function(){
        $(".userName").text("Hi, " + $(".userName").text());
    })
});

$(document).ready(function (){
    $("#newGame").click(function(){
        window.location.href= "game.html";
    })
});