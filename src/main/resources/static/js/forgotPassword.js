let current_user;

function findUser() {
    // let username = {userName : $("#userName").val()};
    // let user_json =  JSON.stringify(username);

    $.post("/forgetPage/fpFindUser", { username : $("#userName").val()},
        function(user){
            if (user !== null) {
                current_user = user.data;
                let toSearch = {
                    regex: user.data.username
                };
                let toSearch_json = JSON.stringify(toSearch);
                $.ajax({
                    url: "/forgetPage/fpGetQuestion",
                    type: 'POST',
                    dataTye: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data: toSearch_json,
                    success: function (result) {
                        if(result.success) {
                            $("#fpUserName").css('display', 'none');
                            $("#fpQuestion").css('display', '');
                            $("#fpAnswer").css('display', '');
                            $(".fpSend").css('display', 'none');
                            $(".fpSend1").css('display', '');
                            $("#question").val(result.data);
                        }
                    }
                })
            } else {
                alert("User Does Not Exist");
            }
        });
}
function checkAnswer(){
    let answer = {
        answer : $("#answer").val(),
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
            if(result.success){
                $("#fpPassword").css('display','');
                $(".fpSend1").css('display','none');
                $("#fpQuestion").css('display','none');
                $("#fpAnswer").css('display','none');
                $(".fpSave").css('display','');
                alert("Please Set Your New Password");
            }
            else {
                alert("Wrong answer for the question");
            }

        }
    })

}


function changepassword(){
    let userNewPassword = {
        answer : $("#newPassword").val(),
        user : current_user
    }
    let user_json =  JSON.stringify(userNewPassword);
    $.ajax({
        url: "/forgetPage/fpChangePassword",
        type: "POST",
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: user_json,
        success: function(user){
            if (user.success) {
                window.location.href= "/";
                alert("You have changed your password");
            }
        }
    })

}

$(document).ready( function () {

    $("#submitbtn").on("click",findUser);
    $("#submitbtn1").on("click",checkAnswer);
    $("#savebtn").on("click",changepassword);
})