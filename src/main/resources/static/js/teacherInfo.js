/*
* reload list of course
*/
function reloadCourses(){
    $.ajax({
        url: "/course/rest/loadInfo",
        type: "GET",
        success: function(result){
            $('#course_list tbody').empty();
            let len = result.length;
            for(let r = len-1; r >= 0; r--){
                $('#course_list tbody').append(
                    "<tr>" +
                    "<td class=\"coursecheckcol center mycheckbox\" ><label><input type=\"checkbox\"/><span></span></label></td>" +
                    "<td class = \"myIdColumn\">" +
                    result[r].course_id +
                    "</td>" +
                    "<td>" +
                    result[r].name +
                    "</td>" +
                    "<td>" +
                    result[r].createdDate +
                    "</td>" +
                    "</tr>"
                )
            }
        }
    })
}

/**
 * Get course data from input and text area, then
 * send to the server. If success, re-build course
 * table.
 */
function saveCourse(){
    let course = {
        name: $("#course_name").val(),
        course_id: $("#course_code").val(),
        description: $("#course_description").val()
    }
    let course_json =  JSON.stringify(course);
    $.ajax({
        url:"/course/rest",
        type: 'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: course_json,
        success: function(result){
            if(result.success === "true"){
                reloadCourses();
            }else{
                alert(result.error);
            }
        }
    })
}

function displayCourseInfo( event ){
    $.ajax({
        url:"/course/rest",
        type: 'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: course_json,
        success: function(result){
            if(result.success === "true"){
                reloadCourses();
            }else{
                alert(result.error);
            }
        }
    })
}




function deleteCourse(){
    let course = [];
    $("#course_list tbody tr").each(function(i,row){
        if ($(row).find('input[type="checkbox"]').is(':checked')) {
            course.push({
                name: null,
                course_id: ($(row).find(".myIdColumn").text()),
                description: null
            });
        }
    });
    let course_json =  JSON.stringify(course);
    $.ajax({
        url:"/course/rest/deleteCourse",
        type: 'DELETE',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: course_json,
        success:function(result){
            if(result.success === "true"){
                reloadCourses();
            }else{
                alert(result.error);
            }
        }
    })
}


/**
 * Clean data in pop div. when user click on
 * add course button.
 */

function addCreate() {
    $("#setCourseText").find('input:text').val('');
    $("#setCourseText").find('textarea').val('');
}

function hideAndShowCourse() {
    $(".coursecheckcol").toggle();
}

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
    //Course
    $("#courseAddBtn").on("click", addCreate);
    $("#courseSaveBtn").on("click", saveCourse);
    $("#courseDeleteBtn").on("click",deleteCourse);
    $("#courseEditBtn").on("click",hideAndShowCourse);
    $("#courseBackBtn").on("click",hideAndShowCourse);
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





















