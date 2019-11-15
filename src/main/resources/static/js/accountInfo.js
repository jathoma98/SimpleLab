function showUserInfo(){
    $.ajax({
        url: "/user/rest/loadUserInfo",
        type: "GET",
        success: function(user){
            $("#first_name").val(user.firstname);
            $("#last_name").val(user.lastname);
            $("#institution").val(user.institution);
            $("#email").val(user.email);
        }
    })
}

function editUserInfo(){
    let user ={

        firstname : $("#first_name").val(),
        lastname : $("#last_name").val(),
        institution: $("#institution").val(),
        email: $("#email").val()
    };
    let course_json =  JSON.stringify(user);
    $.ajax({
        url: "/user/rest/restUserInfo",
        type: "POST",
        data: course_json,
        success:function (){
            alert("You have saved your information!")
        }
    })
}

$(document).ready( function () {
    $(".accountDia").on("click", showUserInfo);
    $("#saveInfoBtn").on("click",editUserInfo)
})