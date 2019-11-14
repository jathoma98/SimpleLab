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
    let course_json =  JSON.stringify(course)
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
                        "<td>" +
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

function deleteCourse(){
    let checkValue = [];
    $("#course_list tbody tr").each(function(i,row){
        if ($(row).find('input[type="checkbox"]').is(':checked')){
            checkValue.push($(row).find(".myIdColumn").text());
        }
        else{
        }
    });
    $.ajax({
        url:"/course/rest/deleteCourse",
        type: 'DELETE',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify({checkValue}),
        success:function(){
            reloadCourses();
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

$(document).ready( function () {
    $("#add_course").on("click", addCreate);
    $("#save_course").on("click", saveCourse);
    $("#courseDeleteBtn").on("click",deleteCourse);
    $("#labEditBtn").on("click",hideAndShowLab);
    $("#labBackBtn").on("click",hideAndShowLab);
    $("#equipEditBtn").on("click",hideAndShowEquip);
    $("#equipBackBtn").on("click",hideAndShowEquip);
    $("#courseEditBtn").on("click",hideAndShowCourse);
    $("#courseBackBtn").on("click",hideAndShowCourse);
})


function hideAndShowCourse() {
    $(".coursecheckcol").toggle();
}

function hideAndShowLab(){
    // $(".table_check").css("display","none");
    $(".labcheckcol").toggle();
}
function hideAndShowEquip(){
    // $(".table_check").css("display","none");
    $(".equipcheckcol").toggle();
}



















