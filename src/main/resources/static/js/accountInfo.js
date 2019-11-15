function showUserInfo(){
    $.ajax({
        url: "/user/rest/loadUserInfo",
        type: "GET",
        success: function(result){
            $("#first_name").val(result.firstname);
            $("#last_name").val(result.lastname);
            $("#institution").val(result.institution);
            $("#email").val(result.email);
        }
    })
}

function editUserInfo(){
    $.ajax({
        url: "/user/rest/restUserInfo",
        type: "POST",
        success:function () {

        }
    })
}

$(document).ready( function () {
    $(".accountDia").on("click", showUserInfo);
    $("#changeName").on("click",editUserInfo)
})