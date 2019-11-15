



function hideAndShowLab(){
    $(".labcheckcol").toggle();
}
function hideAndShowEquip(){
    $(".equipcheckcol").toggle();
}

function editInfo(){
    console.log("editInfoBtn is pressed");
    $("#first_name").prop("readonly", false);
    $("#last_name").prop("readonly", false);
    $("#email").prop("readonly", false);
    $("#institution").prop("readonly", false);
    $("#password").prop("readonly", false);
    $("#editInfoBtn").toggle();
    $("#saveInfoBtn").toggle();
    $("#cancelBtn").toggle();
}
function saveInfo(){
    $("#first_name").prop("readonly", true);
    $("#last_name").prop("readonly", true);
    $("#email").prop("readonly", true);
    $("#institution").prop("readonly", true);
    $("#password").prop("readonly", true);
    $("#editInfoBtn").toggle();
    $("#saveInfoBtn").toggle();
    $("#cancelBtn").toggle();
}

function cancelEdit(){
    $("#first_name").prop("readonly", true);
    $("#last_name").prop("readonly", true);
    $("#email").prop("readonly", true);
    $("#institution").prop("readonly", true);
    $("#password").prop("readonly", true);
    $("#editInfoBtn").toggle();
    $("#saveInfoBtn").toggle();
    $("#cancelBtn").toggle();
}
$(document).ready( function () {

    //Lab
    $("#labEditBtn").on("click",hideAndShowLab);
    $("#labBackBtn").on("click",hideAndShowLab);
    //Equipment
    $("#equipEditBtn").on("click",hideAndShowEquip);
    $("#equipBackBtn").on("click",hideAndShowEquip);


    $("#editInfoBtn").on("click",editInfo);
    $("#saveInfoBtn").on("click",saveInfo);
    $("#cancelBtn").on("click",cancelEdit);
})





















