function signupbtnEvent(){
    let user_role = $("#role input[type='radio']:checked").val();
    let password = $("#sp_password").val(), repassword = $("#sp_re_password").val();
    user_data = {
        userName : $("#userName").val(),
        email: $("#email").val(),
        firstname: $("#firstname").val(),
        lastname: $("#lastname").val(),
        institution: $("#institution").val(),
        sp_password: password,
        sp_re_password: repassword,
        question: $("#question").val(),
        answer: $("#answer").val(),
        identity: user_role
    }
    for(let key in user_data){
        if(user_data[key] == undefined || user_data[key].length == 0){
            console.log("There is a empty field: " + key);
            //return "error";
        }
    }
    let user_data_json = JSON.stringify(user_data)
    //console.log(user_data_json);

    $.ajax({
        url: "/signup/submit",
        type: 'POST',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: user_data_json,
        success: function(result) {
            retObjHandle(result, ()=>{
                window.location.href= "/";
            })
        }
    });
}


$(document).ready( function () {
    $("#signupbtn").on("click", signupbtnEvent);
})