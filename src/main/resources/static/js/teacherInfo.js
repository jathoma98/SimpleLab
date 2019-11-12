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
    $("#labEditBtn").on("click",labHideAndShow);
    $("#labBackBtn").on("click",labHideAndShow);
    $("#equipEditBtn").on("click",equipHideAndShow);
    $("#equipBackBtn").on("click",equipHideAndShow);
    $("#courseEditBtn").on("click",courseHideAndShow);
    $("#courseBackBtn").on("click",courseHideAndShow);
    $("#courseDeleteBtn").on("click",coursedelete);
})

function coursedelete(){

}

function courseHideAndShow() {
    $(".coursecheckcol").toggle();
}

function labHideAndShow(){
    // $(".table_check").css("display","none");
    $(".labcheckcol").toggle();
}
function equipHideAndShow(){
    // $(".table_check").css("display","none");
    $(".equipcheckcol").toggle();
}



















