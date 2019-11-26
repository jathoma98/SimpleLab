let current_user;

function findUser() {
    // let username = {userName : $("#userName").val()};
    // let user_json =  JSON.stringify(username);
    $.post("/forgetPage/fpFindUser", { userName : $("#userName").val()},
        function(user){
            if (user !== null) {
                current_user = user;
                $("#fpUserName").css('display','none');
                $("#fpQuestion").css('display','');
                $("#fpAnswer").css('display','');
                $(".fpSend").css('display','none');
                $(".fpSend1").css('display','');
                $("#question").val(user.question);
            } else {
                alert("User Does Not Exist");
            }
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
        url:"/forgetPage/getUserAnswer",
        type:'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: user_json,
        success: function (result) {
            if(result === true){
                $("#fpPassword").css('display','');
                $(".fpSend1").css('display','none');
                $("#fpQuestion").css('display','none');
                $("#fpAnswer").css('display','none');
            }
            else {
                alert("Wrong answer for the question");
            }

        }
    })

}


function changepassword(){
    let userNewPassword = {
        newPassword : $("#password").val(),
    }

    $.ajax({
        url: "/forgetPage/fpChangePassword",
        type: "POST",
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: course_json,
        success: function(user){
            if (user.success === "true") {

                console.log("You have changed your password")
            } else {
                console.log("User Not Exist");
            }
        }
    })

}

$(document).ready( function () {

    $("#submitbtn").on("click",findUser);
    $("#submitbtn1").on("click",checkAnswer);
    $("#savebtn").on("click",changepassword);
})