

$(document).ready( function () {
    $("#courseEditBtn").on("click",courseHideAndShow);
    $("#courseBackBtn").on("click",courseHideAndShow);
    $("#labEditBtn").on("click",labHideAndShow);
    $("#labBackBtn").on("click",labHideAndShow);
    $("#editInfoBtn").on("click",editInfo);
    $("#courseSearchBtn").on("click",searchCourse);

    // $("#searchStudentLabBtn").on("click",searchLab)

})

function addCourse() {
    var invitecodeid="invitecode"+this.id;
    let course = {
        invite_code:$("#"+invitecodeid).val(),
        course_id: this.id,
        usernameList: new Array()
    }
    let course_json = JSON.stringify(course);
    $.ajax({
        url: "/course/rest/addStudent",
        type: 'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: course_json,
        success: function (result) {
            console.log("success");
            // retObjHandle(result,
            //     () => {
            //     COURSES_TABLE.reLoadStudentsList(course_json)
            // })
        }
    })
}

function searchCourse() {
        let toSearch = {
            regex: $("#searchCourse").val()
        };
        let toSearch_json = JSON.stringify(toSearch);
        $.ajax({
            url: "/course/rest/searchCourse",
            type: 'POST',
            dataTye: 'json',
            contentType: 'application/json; charset=utf-8',
            data: toSearch_json,
            success: function (result) {
                let searchCourseTable = '';
                for (let f=0;f<result.data.length;f++){
                    searchCourseTable += '<tr><td>' + result.data[f].name + '</td>' +
                        '<td class="valign-wrapper"><input id="invitecode'+result.data[f].course_id+'"  type="text" placeholder="enter invate code here" class="col s4 offset-s7">' +
                        '<a id='+result.data[f].course_id+' href="#" class="right addcourse">add</a></td>' +
                        '</tr>'
                }
                $('#student_search_course tbody').html(searchCourseTable);
                $(".addcourse").on("click",addCourse);
            }
        })
}
function labHideAndShow(){
    // $(".table_check").css("display","none");
    $(".labcheckcol").toggle();
}
function courseHideAndShow(){
    // $(".table_check").css("display","none");
    $(".coursecheckcol").toggle();
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
