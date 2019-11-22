function hideAndShowLab(){
    $(".labcheckcol").toggle();
}
function hideAndShowEquip(){
    $(".equipcheckcol").toggle();
}


$(document).ready( function () {

    //Lab
    $("#labEditBtn").on("click",hideAndShowLab);
    $("#labBackBtn").on("click",hideAndShowLab);
    //Equipment
    $("#equipEditBtn").on("click",hideAndShowEquip);
    $("#equipBackBtn").on("click",hideAndShowEquip);

})





















