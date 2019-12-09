

$(document).ready( function () {
    $("#courseEditBtn").on("click",courseHideAndShow);
    $("#courseBackBtn").on("click",courseHideAndShow);
    $("#labEditBtn").on("click",labHideAndShow);
    $("#labBackBtn").on("click",labHideAndShow);
    $("#editInfoBtn").on("click",editInfo);
    $("#courseSearchBtn").on("click",searchCourse);
    $("#student_search_course").on("click",'tbody tr',searchInvite)



})

function searchInvite() {
        $(this).find(".add_course").on("click",
            checkInvite($(this).find("#invite_code").val(),
                $(this).find(".searched_name").text()));
}

function checkInvite(input,course_name) {
    let toMatch = {
        regex: course_name
    };
    let toMatch_json = JSON.stringify(toMatch);
    $.ajax({
        url: "/course/rest/checkInviteCode",
        type: 'POST',
        dataTye: 'json',
        contentType: 'application/json; charset=utf-8',
        data: toMatch_json,
        success: function (result) {
            retObjHandle(result,function(){
                if (result.data.invite_code === input){}
                let courseTable = ''
                courseTable = '<tr><td>' + result.data.course_id +'</td>'+
                               '<td>' + result.data.name + '</td>' +
                                '<td>'+ result.data.createdDate + '</td></tr>';
                $("#studentCourse tbody").append(courseTable);

                $.ajax({
                    url: "/user/rest/loadUserInfo",
                    type: "GET",
                    success: function (data) {
                        retObjHandle(data, function () {
                            let course ={
                                course_id : result.data.course_id,
                                username : data.data.username
                            };
                            let course_json = JSON.stringify(course);
                            $.ajax({
                                url: "/course/rest/addStudent",
                                type: 'POST',
                                dataTye: 'json',
                                contentType: 'application/json; charset=utf-8',
                                data: course_json,
                                success: function (result) {
                                    //set the student into the class
                                }
                            })
                        })
                    }
                })

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
                    searchCourseTable += '<tr><td class="searched_name">' + result.data[f].name + '</td>' +
                        '<td class="valign-wrapper"><input id="invite_code" type="text" placeholder="invite code" class="col s4 offset-s7">' +
                        '<a href="#" class="right modal-close add_course">add</a></td>' +
                        '</tr>'
                }
                $('#student_search_course tbody').html(searchCourseTable);
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
