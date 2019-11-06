//Get data from text field

function createLab(){
    let course = {
        name: $("#course_name").val(),
        course_id: $("#course_code").val(),
        course_description: $("#course_description").val()
    }
    let course_json =  JSON.stringify(course)
    $.ajax({
        url:"/teacher/createlab",
        type: 'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: course_json,
        success: function(result){
            if(result.success === "true"){

            }else{
                alert(result.error)
            }
        }
    })
}

function addCreate() {
    $("#setCourseText").find('input:text').val('');
    $("#setCourseText").find('textarea').val('');
}

$(document).ready( function () {
    $("#add_course").on("click", addCreate);
    $("#save_course").on("click", createLab);
})