function signupbtnEvent(){
    let user_role = $("#role").val();
    let password = $("#sp_password").val(), repassword = $("#sp_re_password").val();
    if (password !== repassword){
        console.log("pw != rpw")
        return "error"
    }
    user_data = {
        userName : $("#userName").val(),
        email: $("#email").val(),
        firstname: $("#firstname").val(),
        lastname: $("#lastname").val(),
        institution: $("#institution").val(),
        password: password,
        repassword: repassword,
        question: $("#question").val(),
        answer: $("#answer").val(),
        identity: user_role
    }
    for(let key in user_data){
        if(user_data[key] == undefined || user_data[key].length == 0){
            console.log("There is a empty field: " + key);
            return "error";
        }
    }
    let user_data_json = JSON.stringify(user_data)
    console.log(user_data_json);
    $.post("/signup/userdata", user_data_json, function (result) {
        if (result.success === "true") {
            window.location.href= "/login";
        }
        else {
            if (result.reason === "password does not match"){
                console.log("The password fields do not match. Please re-enter your passwords");
            }
            else if (result.reason === "username taken"){
                console.log("The username is already taken");
            }
        }
    })
}


$(document).ready( function () {
    $("#signupbtn").on("click", signupbtnEvent);
})