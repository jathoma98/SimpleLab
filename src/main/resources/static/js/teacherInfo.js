function hideAndShowLab(){
    $(".labcheckcol").toggle();
}
function hideAndShowEquip(){
    $(".equipcheckcol").toggle();
}
function signOut(){
    $.ajax({
        url: "/logout",
        type: 'GET',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: user_data_json,
        success: function(result) {
            if (result.success === "true") {
                window.location.href= "/";
            }
            else {
                alert(result.error);
            }
        }
    });
}

$(document).ready( function () {

    //Lab
    $("#labEditBtn").on("click",hideAndShowLab);
    $("#labBackBtn").on("click",hideAndShowLab);
    //Equipment
    $("#equipEditBtn").on("click",hideAndShowEquip);
    $("#equipBackBtn").on("click",hideAndShowEquip);
    $("#signOut").on("click",signOut)
})





















