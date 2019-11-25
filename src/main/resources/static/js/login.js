$(document).ready(function () {
    $("#loginBtn").click(function(){
        $.post("/login", {userName: $("#userName").val (), password: $("#password").val()}, function (result){
            retObjHandle(result, ()=>{
                window.location.href= "/role";
            })
        })
    })
    $(".visibility").on("click",switchVisibility);
})


function switchVisibility() {
    var x = document.getElementById("password");
    if (x.type === "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
    $(".visibility").toggle();
}