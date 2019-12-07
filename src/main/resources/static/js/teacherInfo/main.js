
ElEM_ID = {
    MODAL_UL: "#modal ul",
    SEARCH_STUDENT_BTN : "#searchStudentBtn",
    COURSE_TABLE_TBODY: "#course_list tbody",
    LAB_TABLE_TBODY: "#lab_list tbody",
    EQUIPMENT_TABLE_TBODY: "#equipment_list tbody",
    STUDENT_SEARCH_TBODY: "#student_search_result_table tbody",
    STUDENT_LIST_TBODY: "#student_list_table tbody",
    COURSE_SAVE_BTN: "#courseSaveBtn",
    COURSE_EDIT_BTN: "#courseEditBtn",
    LAB_SAVE_BTN: "#labSaveBtn",
    LAB_EDIT_BTN: "#labEditBtn",
    EQUIPMENT_SAVE_BTN: "#equipmentSaveBtn"
}

TEMPLATE_ID = {
    STUDENTS_TBODY: "#student_search_tbody",
    LAB_TBODY: "#lab_tbody",
    COURSE_TBODY: "#course_tbody",
    EQUIPMENT_TBODY: "#equip_tbody",
    MODAL: "#modalTpl",
    MODAL_EQUIP: "#modalEquip"
}

function hideAndShowEquip(){
    $(".equipcheckcol").toggle();
}


$(document).ready(function () {
    COURSES_TABLE.init();
    //Course
    $("#courseAddBtn").on("click", ()=>{COURSES_TABLE.create()});
    $("#courseDeleteBtn").on("click", ()=>{COURSES_TABLE.delete()});
    $("#courseEditBtn").on("click", ()=>{COURSES_TABLE.btnSwitch()});
    $("#courseBackBtn").on("click", ()=>{COURSES_TABLE.btnSwitch()});
    //load course table
    COURSES_TABLE.reload();


    //Lab
    LABS_TABLE.init();
    $("#labAddBtn").on("click", ()=>{LABS_TABLE.create()});
    $("#labDeleteBtn").on("click", ()=>{LABS_TABLE.delete()});
    $("#labEditBtn").on("click",()=>{LABS_TABLE.btnSwitch()});
    $("#labBackBtn").on("click",()=>{LABS_TABLE.btnSwitch()});
    $("#labDeleteBtn").on("click", ()=>{LABS_TABLE.delete()})
    //load lab table
    LABS_TABLE.reload();

    //Equipment
    EQUIPMENT_TABLE.init();
    $("#equipEditBtn").on("click",()=>{EQUIPMENT_TABLE.btnSwitch()});
    $("#equipBackBtn").on("click",()=>{EQUIPMENT_TABLE.btnSwitch()});
    $("#equipAddBtn").on("click", ()=>{EQUIPMENT_TABLE.create()});
    //load equipment table
    EQUIPMENT_TABLE.reload();

})



















