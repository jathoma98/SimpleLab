let COURSES_TABLE = {
    init(){
        this.course_toggle = true;
        this.btnEvents = new Array();
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
                        "#student_search_result_table tbody",
                        "#student_search_result_tbody",
                        data)
                    console.log(result)
                }
            })
        }
        this.btnEvents["#searchStudentBtn"] = this.searchStudent;



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
                    rebuildComponent('#course_list tbody', '#course_tbody', data);
                    if (COURSES_TABLE.course_toggle) {
                        setTableBodyRowEvent($("#course_list tbody"), COURSES_TABLE.tableRowEvent());
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
            rebuildComponent('#modal ul',"#modalTpl", data,  this.btnEvents);
        };


        this.btnSwitch = function () {
            $(".coursecheckcol").toggle();
            COURSES_TABLE.course_toggle = !COURSES_TABLE.course_toggle;
            if (COURSES_TABLE.course_toggle) {
                setTableBodyRowEvent($("#course_list tbody"), COURSES_TABLE.tableRowEvent);
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