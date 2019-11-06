$(document).ready(function () {
    $("#loginBtn").click(function(){
        $.post("/login", {userName: $("#userName").val (), password: $("#password").val()}, function (result){
            if(result.success=="true"){
                window.location.href= "/role";
            }else{
                alert("Please check your password!")
            }
        })
    })
})