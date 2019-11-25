let current_user;

function findUser() {
    // let username = {userName : $("#userName").val()};
    // let user_json =  JSON.stringify(username);
    $.post("/fpFindUser", { userName : $("#userName").val()},
        function(){
            console.log("123");
        });

    // $.ajax({
    //     url: "/fpFindUser",
    //     type: 'POST',
    //     data: jQuery.param({ userName: $("#userName").val()}),
    //     contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
    //     success: function(){
    //         console.log("123");
    //         // if (user.success === "true") {
    //         //     current_user = user;
    //         //     // $(".fpUserName").prop('disabled',true);
    //         //     // $(".fpEmail").prop('disabled',true);
    //         //     // $(".fpQuestion").val(user.question);
    //         //     // $(".fpQuestion").prop('disable',false);
    //         //     // $(".fpAnswer").prop('disable',false);
    //         //     // $(".fpSend").prop('disable',true);
    //         //     // $(".fpSave").prop('disable',false);
    //         // } else {
    //         //     alert("User Does Not Exist");
    //         // }
    //     }
    // })
}
function checkAnswer(){
    let answer = {
        answer : $(".fpAnswer").val(),
        user : current_user
    };
    let user_json =  JSON.stringify(answer);
    $.ajax({
        url:"rest/user/getUserAnswer",
        type:'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: user_json,
        success: function (answer) {


        }
    })

}


function changepassword(){
    let userNewPassword = {
        newPassword : $("#a")
    }
    $.ajax({
        url: "/user/rest/fpFindUser",
        type: "POST",
        dataTye: 'json',
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

    $("#submitbtn").on("click",findUser);
    $("#savebtn").on("click",changepassword);
})