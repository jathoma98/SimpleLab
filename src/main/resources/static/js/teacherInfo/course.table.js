ElEM_ID = {
    SEARCH_STUDENT_BTN : "#searchStudentBtn",
    COURSE_TABLE_TBODY: "#course_list tbody",
    STUDENT_SEARCH_TBODY: "#student_search_result_table tbody",
    STUDENT_LIST_TBODY: "#student_list_table tbody"
}

TEMPLATE_ID = {
    STUDENTS_TBODY: "#student_search_tbody"
}

let COURSES_TABLE = {
    init(){
        this.course_toggle = true;
        this.btnEvents = new Array();

        this.addStudentBtnEvent = function(){
            let user = {
                username: $(this).find("td").text()
            }
            let course = {
                course_id: $("#course_code").val(),
            }
            course.users = new Array();
            course.users.push(user);

            let course_json = JSON.stringify(course);

            $.ajax({
                url: "/course/rest/addStudent",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    if (result.success === "true") {
                        COURSES_TABLE.reLoadStudentsList(course_json)
                    } else {
                        console.log(result.error);
                    }
                }
            })
        };

        this.searchStudent = function (){
            let toSearch = {
                regex : $("#searchStudent").val()
            };
            let toSearch_json = JSON.stringify(toSearch);
            $.ajax({
                url: "/user/rest/searchUser",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: toSearch_json,
                success: function (result) {
                    let data = {
                        result : result
                    }
                    rebuildComponent(
                        ElEM_ID.STUDENT_SEARCH_TBODY,
                        TEMPLATE_ID.STUDENTS_TBODY,
                        data);
                    setTableBodyRowEvent(ElEM_ID.STUDENT_SEARCH_TBODY, COURSES_TABLE.addStudentBtnEvent)
                    console.log(result)
                }
            })
        }
        this.btnEvents[ElEM_ID.SEARCH_STUDENT_BTN] = this.searchStudent;


        /**
         * Use to onclick Event on each row of Course list.
         **/
        this.tableRowEvent = function () {
            let course = {
                course_id: $(this).find(".myIdColumn").text()
            }
            let course_json = JSON.stringify(course);
            $.ajax({
                url: "/course/rest/loadCourseInfo",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (course) {
                    let data = {
                        courseModal:{
                            active: "active",
                            course: course,
                        }
                    }
                    rebuildComponent('#modal ul',"#modalTpl", data,  COURSES_TABLE.btnEvents);
                    //load
                    COURSES_TABLE.reLoadStudentsList(course_json)
                }
            })
        };


        this.reLoadStudentsList = function (course_json){
            $.ajax({
                url: "/course/rest/getStudents",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    let data = {
                        result: result
                    }
                    rebuildComponent(
                        ElEM_ID.STUDENT_LIST_TBODY,
                        TEMPLATE_ID.STUDENTS_TBODY,
                        data)
                }
            })
        };


        /**
         * Use to pull course list from server in teacher home page
         **/
        this.reload = function () {
            $.ajax({
                url: "/course/rest/loadCourseList",
                type: "GET",
                success: function (result) {
                    let data = {
                        isEnabled: COURSES_TABLE.course_toggle,
                        courses: result.reverse()
                    }
                    rebuildComponent(ElEM_ID.COURSE_TABLE_TBODY, '#course_tbody', data);
                    if (COURSES_TABLE.course_toggle) {
                        setTableBodyRowEvent(ElEM_ID.COURSE_TABLE_TBODY, COURSES_TABLE.tableRowEvent);
                    }
                }
            })
        };


        /**
         * Get course data from input and text area, then
         * send to the server. If success, re-build course
         * table.
         */
        this.save = function () {
            let course = {
                name: $("#course_name").val(),
                course_id: $("#course_code").val(),
                description: $("#course_description").val()
            }
            let course_json = JSON.stringify(course);
            $.ajax({
                url: "/course/rest",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    if (result.success === "true") {
                        COURSES_TABLE.reload();
                    } else {
                        alert(result.error);
                    }
                }
            })
        };

        /**
         * Delete course.
         **/
        this.delete = function () {
            removeTableBodyRowEvent($("#course_list tbody"));
            let course = [];
            $("#course_list tbody tr").each(function (i, row) {
                if ($(row).find('input[type="checkbox"]').is(':checked')) {
                    course.push({
                        name: null,
                        course_id: ($(row).find(".myIdColumn").text()),
                        description: null
                    });
                }
            });
            let course_json = JSON.stringify(course);
            $.ajax({
                url: "/course/rest/deleteCourse",
                //type: 'POST',
                 type: 'DELETE',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    if (result.success === "true") {
                        COURSES_TABLE.reload();
                    } else {
                        alert(result.error);
                    }
                }
            })
        };


        /**
         * Clean data in modal. when user click on
         * add course button.
         */
        this.create = function () {
            let data = {
                courseModal:{
                    active: "active",
                    course: true,
                }
            }
            rebuildComponent('#modal ul',"#modalTpl", data,  COURSES_TABLE.btnEvents);
        };


        this.btnSwitch = function () {
            $(".coursecheckcol").toggle();
            COURSES_TABLE.course_toggle = !COURSES_TABLE.course_toggle;
            if (COURSES_TABLE.course_toggle) {
                setTableBodyRowEvent(ElEM_ID.COURSE_TABLE_TBODY, COURSES_TABLE.tableRowEvent);
            } else {
                removeTableBodyRowEvent($("#course_list tbody"))
            }
        }
    }
}


$(document).ready(function () {
    COURSES_TABLE.init();
    //Course
    $("#courseAddBtn").on("click", ()=>{COURSES_TABLE.create()});
    $("#courseSaveBtn").on("click", ()=>{COURSES_TABLE.save()});
    $("#courseDeleteBtn").on("click", ()=>{COURSES_TABLE.delete()});
    $("#courseEditBtn").on("click", ()=>{COURSES_TABLE.btnSwitch()});
    $("#courseBackBtn").on("click", ()=>{COURSES_TABLE.btnSwitch()});
    //Set tbody row event
    COURSES_TABLE.reload();
})