
ElEM_ID = {
    SEARCH_STUDENT_BTN : "#searchStudentBtn",
    COURSE_TABLE_TBODY: "#course_list tbody",
    LAB_TABLE_TBODY: "#lab_list tbody",
    STUDENT_SEARCH_TBODY: "#student_search_result_table tbody",
    STUDENT_LIST_TBODY: "#student_list_table tbody",
    MODAL_UL: "#modal ul",
    COURSE_SAVE_BTN: "#courseSaveBtn",
    LAB_SAVE_BTN: "#labSaveBtn",
    LAB_EDIT_BTN: "#labEditBtn",
    COURSE_EDIT_BTN: "#courseEditBtn"
}

TEMPLATE_ID = {
    STUDENTS_TBODY: "#student_search_tbody",
    LAB_TBODY: "#lab_tbody",
    COURSE_TBODY: "#course_tbody",
    MODAL: "#modalTpl"
}

function hideAndShowEquip(){
    $(".equipcheckcol").toggle();
}


$(document).ready(function () {
    COURSES_TABLE.init();
    //Course
    $("#courseAddBtn").on("click", ()=>{COURSES_TABLE.create()});
    // $("#courseSaveBtn").on("click", ()=>{COURSES_TABLE.save()});
    $("#courseDeleteBtn").on("click", ()=>{COURSES_TABLE.delete()});
    $("#courseEditBtn").on("click", ()=>{COURSES_TABLE.btnSwitch()});
    $("#courseBackBtn").on("click", ()=>{COURSES_TABLE.btnSwitch()});
    //Set tbody row event
    COURSES_TABLE.reload();




    //Lab
    LABS_TABLE.init();
    $("#labAddBtn").on("click", ()=>{LABS_TABLE.create()});
    $("#labEditBtn").on("click",()=>{LABS_TABLE.btnSwitch()});
    $("#labBackBtn").on("click",()=>{LABS_TABLE.btnSwitch()});
    //load lab table
    LABS_TABLE.reload();

    //Equipment
    $("#equipEditBtn").on("click",hideAndShowEquip);
    $("#equipBackBtn").on("click",hideAndShowEquip);
})



















