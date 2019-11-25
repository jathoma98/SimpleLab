let current_user;
function finduser() {
    let username = {userName : $("#userName").val()};
    let user_json =  JSON.stringify(username);
    $.ajax({
        url: "/fpFindUser",
        type: 'POST',
        // dataTye: 'json',
        // contentType: 'application/json; charset=utf-8',
        data: user_json,
        success: function(){
            console.log("123");
            // if (user.success === "true") {
            //     current_user = user;
            //     // $(".fpUserName").prop('disabled',true);
            //     // $(".fpEmail").prop('disabled',true);
            //     // $(".fpQuestion").val(user.question);
            //     // $(".fpQuestion").prop('disable',false);
            //     // $(".fpAnswer").prop('disable',false);
            //     // $(".fpSend").prop('disable',true);
            //     // $(".fpSave").prop('disable',false);
            // } else {
            //     alert("User Does Not Exist");
            // }
        }
    })
}
function checkAnswer(){
    let answer = $(".fpAnswer").val();

}


function changepassword(){
    let userNewPassword = {
        newPassword : $("#a")
    }
    $.ajax({
        url: "/user/rest/fpFindUser",
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        data: course_json,
        success: function(user){
            if (user.success === "true") {
                current_user = user;
                $(".fpUserName").prop('disabled',true);
                $(".fpEmail").prop('disabled',true);
                $(".fpQuestion").val(user.question);
                $(".fpQuestion").prop('disable',false);
                $(".fpAnswer").prop('disable',false);
                $(".fpSend").prop('disable',true);
                $(".fpSave").prop('disable',false);
            } else {
                console.log("User Not Exist");
            }
        }
    })

}

$(document).ready( function () {

    $("#submitbtn").on("click",finduser);
    $("#savebtn").on("click",changepassword);
})