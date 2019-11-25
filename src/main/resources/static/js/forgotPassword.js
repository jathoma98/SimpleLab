let current_user;

function findUser() {
    // let username = {userName : $("#userName").val()};
    // let user_json =  JSON.stringify(username);
    let data = { username : $("#userName").val()};
    $.post("/fpFindUser", data,
        function(user){
            // if (user !== null) {
            //     current_user = user;
            //     $("#fpUserName").css('display','none');
            //     $("#fpQuestion").css('display','');
            //     $("#fpAnswer").css('display','');
            //     $(".fpSend").css('display','none');
            //     $(".fpSend1").css('display','');
            //     $("#question").val(user.question);
            //
            // } else {
            //     alert("User Does Not Exist");
            // }
            console.log("abc")
        });
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

    $("#submitbtn").on("click", findUser);
    $("#savebtn").on("click",changepassword);
})