let COURSES_TABLE = {
    init(){
        this.toggle = true;
        this.btnEvents = new Array();

        this.removeStendtBtnEvent = function(){
            let course = {
                course_id: $("#course_code").val(),
                usernameList : new Array()
            }
            course.usernameList.push($(this).find("td").text());

            let course_json = JSON.stringify(course);
            $.ajax({
                url: "/course/rest/deleteStudents",
                type: 'DELETE',
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
        }

        this.addStudentBtnEvent = function(){
            let course = {
                course_id: $("#course_code").val(),
                usernameList : new Array()
            }
            course.usernameList.push($(this).find("td").text());


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
                success: function (result) {
                    retObjHandle(result, function(){
                        let data = {
                            courseModal:{
                                active: "active",
                                course: result.data,
                            }
                        }
                        rebuildComponent(ElEM_ID.MODAL_UL,TEMPLATE_ID.MODAL, data,  COURSES_TABLE.btnEvents);
                        //load
                        COURSES_TABLE.reLoadStudentsList(course_json)
                    })


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
                    setTableBodyRowEvent(ElEM_ID.STUDENT_LIST_TBODY, COURSES_TABLE.removeStendtBtnEvent);
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
                    retObjHandle(result, function(){
                        let data = {
                            isEnabled: COURSES_TABLE.toggle,
                            courses: result.data.reverse()
                        }
                        rebuildComponent(ElEM_ID.COURSE_TABLE_TBODY, TEMPLATE_ID.COURSE_TBODY, data);
                        if (COURSES_TABLE.toggle) {
                            setTableBodyRowEvent(ElEM_ID.COURSE_TABLE_TBODY, COURSES_TABLE.tableRowEvent);
                        }
                    })
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
        this.btnEvents[ElEM_ID.COURSE_SAVE_BTN] = COURSES_TABLE.save;

        /**
         * Delete course.
         **/
        this.delete = function () {
            removeTableBodyRowEvent($(ElEM_ID.COURSE_TABLE_TBODY));
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
            rebuildComponent(ElEM_ID.MODAL_UL, TEMPLATE_ID.MODAL, data,  COURSES_TABLE.btnEvents);
        };


        this.btnSwitch = function () {
            $(".coursecheckcol").toggle();
            COURSES_TABLE.toggle = !COURSES_TABLE.toggle;
            if (COURSES_TABLE.toggle) {
                setTableBodyRowEvent(ElEM_ID.COURSE_TABLE_TBODY, COURSES_TABLE.tableRowEvent);
            } else {
                removeTableBodyRowEvent($("#course_list tbody"))
            }
        }
    }
}


