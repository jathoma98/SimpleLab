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
                // TODO: pull the course data and rebuild course list
                $.ajax({
                    url: "/course/rest/loadInfo",
                    type: "GET",
                    success: function(result){
                        //TODO: Clear table
                        //TODO: build table
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
            }else{
                alert(result.error);
            }
        }
    })
}

function saveLab(){
    let course = {
        name: $("#lab_name").val(),
        course_id: $("#lab_code").val(),
        description: $("#lab_description").val()
    }
    let lab_json =  JSON.stringify(lab)
    $.ajax({
        url:"/lab/rest",
        type: 'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: lab_json,
        success: function(result){
            if(result.success === "true"){
                // TODO: pull the course data and rebuild course list
                $.ajax({
                    url: "/lab/rest/loadInfo",
                    type: "GET",
                    success: function(result){
                        //TODO: Clear table
                        //TODO: build table
                        $('#lab_list tbody').empty();
                        let len = result.length;
                        for(let r = len-1; r >= 0; r--){
                            $('#lab_list tbody').append(
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

$(document).ready( function () {
    $("#add_course").on("click", addCreate);
    $("#save_course").on("click", saveCourse);
});


