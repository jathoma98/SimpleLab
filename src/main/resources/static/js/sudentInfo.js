

$(document).ready( function () {
    $("#courseEditBtn").on("click",courseHideAndShow);
    $("#courseBackBtn").on("click",courseHideAndShow);
    $("#labEditBtn").on("click",labHideAndShow);
    $("#labBackBtn").on("click",labHideAndShow);
    $("#editInfoBtn").on("click",editInfo);
    $("#courseSearchBtn").on("click",searchCourse);
    loadCourse();

})



function deleteCourse(){
    removeTableBodyRowEvent($("#studentCourse tbody"));
    let course = [];
    $("#studentCourse tbody tr").each(function (i, row) {
        if ($(row).find('input[type="checkbox"]').is(':checked')) {
            course.push({
                name: null,
                course_id: ($(row).find(".studentIdColumn").text()),
                description: null
            });
        }
    });
    let course_json = JSON.stringify(course);
    $.ajax({
        url: "/course/rest/deleteCourse",
        type: 'DELETE',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: course_json,
        success: function (result) {
            retObjHandle(result, loadCourse)
        }
    })
}

function loadCourse() {
    $.ajax({
        url: "/course/rest/loadCourseList",
        type: 'GET',
        success: function (result) {
            retObjHandle(result, function () {
                let courseTable = '';
                for (let f = 0; f < result.data.length; f++) {
                    courseTable += '<tr courseid='+result.data[f].course_id+'>' +
                        '<td class = "coursecheckcol mycheckbox myhide center"' +
                        '<label> <input type="checkbox"><span></span>' + '</label></td>' +
                        '<td class="studentIdColumn">' + result.data[f].course_id + '</td>' +
                        '<td>' + result.data[f].name + '</td>' +
                        '<td>' + result.data[f].createdDate + '</td></tr>';

                }
                $("#studentCourse tbody").html(courseTable);
                $("#studentCourse tbody").find("tr").each(function () {
                    $(this).on("click", jumptoCourseLab);
                    $(this).find(".coursecheckcol").off("click");
                })
            })
        }
    })
}
function jumptoCourseLab() {
    let toSearch = {
        course_id: $(this)[0].getAttribute("courseid")
    };
    let toSearch_json = JSON.stringify(toSearch);
    $.ajax({
        url: "/course/rest/getLabs",
        type: 'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: toSearch_json,
        success: function (result) {
            console.log(result);
            if(result.data.length!=0){
                $('#studentCourseModal').modal('open');
                retObjHandle(result, function () {
                    result.data.sort();
                    let courseTable = '';
                    for (let f = 0; f < result.data.length; f++) {
                        courseTable +='<li>\n' +
                            '                    <div class="collapsible-header"><i class="material-icons">whatshot</i>'+result.data[f].name+'</div>\n' +
                            '                    <div class="collapsible-body labheight">\n' +
                            '                        <div>\n' +
                            '                            <div class="">\n' +
                            '                                <p>Lab Name:  '+result.data[f].name+'</p>\n' +
                            '                            </div>\n' +
                            '                            <p class="labdescriptionheight"> '+result.data[f].description+'</p>\n' +
                            '                            <a href="student/dolab/'+result.data[f].id+'"  class="waves-effect waves-light btn right startlab my_red">Start<i class="material-icons right">send</i></a>\n' +
                            '                        </div>\n' +
                            '                    </div>\n' +
                            '                </li>'

                    }
                    $("#studentCourseModal ul").html(courseTable);
                })
            }else{
                M.toast({html: 'There is no lab in this course!!!'})
            }

        }
    })
}

function checkInvite() {
    let course = {
        invite_code:$(this).parent().find('#inviteCode').val(),
        course_id: this.id,
        usernameList: new Array()
    }
    let toMatch_json = JSON.stringify(course);
    $.ajax({
        url: "/course/rest/addStudent",
        type: 'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: toMatch_json,
        success: function (result) {
            retObjHandle(result,function(){
                //add student into the course
                loadCourse();
            })
        }
    })
}

// function searchLab() {
//     let labToSearch = {
//         regex: $("#searchStudentLab").val()
//     }
//     let toSearch_json = JSON.stringify(labToSearch);
//     $.ajax({
//         url: "/lab/rest/searchLab",
//         type: 'POST',
//         dataTye: 'json',
//         contentType: 'application/json; charset=utf-8',
//         data: toSearch_json,
//         success: function (result) {
//             let searchLabTable = '';
//             for (let f=0;f<result.data.length;f++){
//                 searchLabTable += '<tr><td>' + result.data[f].name + '</td>' +
//                     '<td><input id="invatecode" type="text" class="validate"><a href="#" class="right modal-close addlab">add</a></td>' +
//                     '</tr>'
//             }
//             $('#student_search_lab tbody').html(searchLabTable);
//         }
//     })
//
// }

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
                    searchCourseTable +=
                        '<tr><td class="searched_name">' + result.data[f].course_id + '</td>' +
                        '<td class="valign-wrapper"><input id="inviteCode" type="text" placeholder="invite code" class="col s4 offset-s7">' +
                        '<a id='+result.data[f].course_id+' href="#" class="right modal-close add_course">add</a></td>' +
                        '</tr>'
                }
                $('#student_search_course tbody').html(searchCourseTable);
                $(".add_course").on("click",checkInvite);
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
