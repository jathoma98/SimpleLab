function reloadCourses(){
    $.ajax({
        url: "/course/rest/loadCourseList",
        type: "GET",
        success: function(result){
            $('#course_list tbody').empty();
            let data = {
                isEnabled: course_toggle,
                courses: result.reverse()
            }
            $.get("/js/teacherInfo/tbody.mustache",
                function(template){
                    $('#course_list tbody')
                        .html(Mustache.render(template, data));
                    if(course_toggle){
                        setTableBodyRowEvent($("#course_list tbody"), courseTableRowEvent, "#courseModal");
                    }
            })
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



function courseTableRowEvent( event ){
    let course = {
        course_id:$(this).find(".myIdColumn").text()
    }
    let course_json =  JSON.stringify(course);
    $.ajax({
        url:"/course/rest/loadCourseInfo",
        type: 'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: course_json,
        success: function(course){
            $("#course_name").val(course.name);
            $("#course_code").val(course.course_id);
            $("#course_description").val(course.description);
        }
    })
}

function deleteCourse(){
    removeTableBodyRowEvent($("#course_list tbody"));
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



let course_toggle = true;
function hideAndShowCourse() {
    $(".coursecheckcol").toggle();
    course_toggle = !course_toggle;
    if(course_toggle ){
        setTableBodyRowEvent($("#course_list tbody"), courseTableRowEvent, "#courseModal");
    }else{
        removeTableBodyRowEvent($("#course_list tbody"))
    }

}

$(document).ready(function () {
    //Course
    $("#courseAddBtn").on("click", addCreate);
    $("#courseSaveBtn").on("click", saveCourse);
    $("#courseDeleteBtn").on("click", deleteCourse);
    $("#courseEditBtn").on("click", hideAndShowCourse);
    $("#courseBackBtn").on("click", hideAndShowCourse);
    //Set tbody row event
    setTableBodyRowEvent($("#course_list tbody"), courseTableRowEvent);
    reloadCourses();
})