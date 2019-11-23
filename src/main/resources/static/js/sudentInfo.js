
// $.get("/info/get_user", function(user) {
//     console.log(user.firstname)
//     $(document).ready(function () {
//     });
// });


$(document).ready( function () {
    $("#courseEditBtn").on("click",courseHideAndShow);
    $("#courseBackBtn").on("click",courseHideAndShow);
    $("#labEditBtn").on("click",labHideAndShow);
    $("#labBackBtn").on("click",labHideAndShow);
    $("#editInfoBtn").on("click",editInfo);
    // $("#saveInfoBtn").on("click",saveInfo);
    // $("#cancelBtn").on("click",cancelEdit);

})

function labHideAndShow(){
    // $(".table_check").css("display","none");
    $(".labcheckcol").toggle();
}
function courseHideAndShow(){
    // $(".table_check").css("display","none");
    $(".coursecheckcol").toggle();
}

function editInfo(){
    console.log("editInfoBtn is pressed");
    $("#first_name").prop("readonly", false);
    $("#last_name").prop("readonly", false);
    $("#email").prop("readonly", false);
    $("#institution").prop("readonly", false);
    $("#password").prop("readonly", false);
    $("#editInfoBtn").toggle();
    $("#saveInfoBtn").toggle();
    $("#cancelBtn").toggle();
}
//
// function saveInfo(){
//     $("#first_name").prop("readonly", true);
//     $("#last_name").prop("readonly", true);
//     $("#email").prop("readonly", true);
//     $("#institution").prop("readonly", true);
//     $("#password").prop("readonly", true);
//     $("#editInfoBtn").toggle();
//     $("#saveInfoBtn").toggle();
//     $("#cancelBtn").toggle();
// }
// function cancelEdit(){
//     $("#first_name").prop("readonly", true);
//     $("#last_name").prop("readonly", true);
//     $("#email").prop("readonly", true);
//     $("#institution").prop("readonly", true);
//     $("#password").prop("readonly", true);
//     $("#editInfoBtn").toggle();
//     $("#saveInfoBtn").toggle();
//     $("#cancelBtn").toggle();
// }
