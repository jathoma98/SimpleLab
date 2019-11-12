
$.get("/info/get_user", function(user) {
    console.log(user.firstname)
    $(document).ready(function () {
    });
});




$(document).ready( function () {
    $("#courseEditBtn").on("click",courseHideAndShow);
    $("#courseSaveBtn").on("click",courseHideAndShow);
    $("#labEditBtn").on("click",labHideAndShow);
    $("#labSaveBtn").on("click",labHideAndShow);


})

function labHideAndShow(){
    // $(".table_check").css("display","none");
    $(".labcheckcol").toggle();
}
function courseHideAndShow(){
    // $(".table_check").css("display","none");
    $(".coursecheckcol").toggle();
}