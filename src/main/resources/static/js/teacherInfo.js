//Get data from text field

function createLab(){
    let course = {
        name: $("#course_name").val(),
        course_id: $("#course_code").val(),
        course_decription: $("#course_decription").val()
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

function addCreate(){
    $("#setCourseText input").empty()   
}
