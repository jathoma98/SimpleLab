function signupbtnEvent(){
    let user_role = $("#role").val;
    let password = $("#sp_password").val(), repassword = $("#sp_re_password").val();
    user_data = {
        userName : $("#userName").val(),
        email: $("#email").val(),
        // institution: $("#institution").val(),
        password: password,
        repassword: repassword,
        question: $("#question").val(),
        answer: $("#answer").val(),
        identity: user_role
    }
    console.log(user_data);
    $.post("/signup/userdata", user_data, function (result) {
        if (result.success === "true") {
            window.location.href= "/login";
        }
        else {
            if (result.error === "password does not match"){
                console.log("The password fields do not match. Please re-enter your passwords");
            }
            else if (result.error === "username taken"){
                console.log("The username is already taken");
            }
            else{
                console.log(result.error);
            }
        }
    })
}


$(document).ready( function () {
    $("#signupbtn").on("click", signupbtnEvent);
})