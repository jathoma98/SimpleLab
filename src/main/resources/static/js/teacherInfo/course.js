let COURSES_TABLE = {
    init() {
        this.toggle = true;
        this.btnEvents = new Array();
        this.course_info = undefined;

        this.removeStendtBtnEvent = function () {
            let course = {
                course_id: $("#course_code").val(),
                usernameList: new Array()
            }
            course.usernameList.push($(this).parent().find("span").text());

            let course_json = JSON.stringify(course);
            $.ajax({
                url: "/course/rest/deleteStudents",
                type: 'DELETE',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    retObjHandle(result,
                        () => {
                            COURSES_TABLE.reLoadStudentsList(course_json)
                        })
                }
            })
        }

        this.addStudentBtnEvent = function () {
            let course = {
                course_id: $("#course_code").val(),
                usernameList: new Array()
            }
            course.usernameList.push($(this).parent().find("span").text());


            let course_json = JSON.stringify(course);

            $.ajax({
                url: "/course/rest/addStudent",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    retObjHandle(result,
                        () => {
                            COURSES_TABLE.reLoadStudentsList(course_json);
                        })
                }
            })
        };

        this.searchStudent = function () {
            let toSearch = {
                regex: $("#searchStudent").val()
            };
            let toSearch_json = JSON.stringify(toSearch);
            $.ajax({
                url: "/user/rest/searchUser",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: toSearch_json,
                success: function (result) {
                    retObjHandle(result, (students) => {
                        let data = {
                            students: students,
                            search: true,
                        }
                        rebuildComponent(
                            ElEM_ID.STUDENT_SEARCH_TBODY,
                            TEMPLATE_ID.STUDENTS_TBODY,
                            data);
                        setTableBodyRowBtnEvent(ElEM_ID.STUDENT_SEARCH_TBODY,
                            ".add_student",
                            "click",
                            COURSES_TABLE.addStudentBtnEvent)
                        $('.tooltipped').tooltip();
                    })
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
                    retObjHandle(result, function () {
                        let data = {
                            courseModal: {
                                active: "active",
                                create: false,
                                course: result.data,
                            }
                        }
                        COURSES_TABLE.course_info = result.data;
                        rebuildComponent(ElEM_ID.MODAL_UL, TEMPLATE_ID.MODAL, data, "click", COURSES_TABLE.btnEvents);
                        //load
                        COURSES_TABLE.reLoadStudentsList(course_json)
                        COURSES_TABLE.reLoadLabsList(course_json);
                        COURSES_TABLE.searchLab();
                    })
                }
            })
        };


        this.reLoadStudentsList = function (course_json) {
            $.ajax({
                url: "/course/rest/getStudents",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    retObjHandle(result, (students) => {
                        let data = {
                            students: students,
                            search: false,
                        }
                        rebuildComponent(
                            ElEM_ID.STUDENT_LIST_TBODY,
                            TEMPLATE_ID.STUDENTS_TBODY,
                            data)

                        setTableBodyRowBtnEvent(ElEM_ID.STUDENT_LIST_TBODY,
                            ".del_student",
                            "click",
                            COURSES_TABLE.removeStendtBtnEvent)
                        $('.tooltipped').tooltip();
                    })
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
                    retObjHandle(result, function () {
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
                invite_code: $("#course_invitecode").val(),
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
                    retObjHandle(result, COURSES_TABLE.reload);
                }
            })
        };
        this.btnEvents[ElEM_ID.COURSE_SAVE_BTN] = COURSES_TABLE.save;

        this.edit = function () {
            let courseUpdata = {
                course_id_old: COURSES_TABLE.course_info.course_id,
                newCourseInfo: {
                    name: $("#course_name").val(),
                    course_id: $("#course_code").val(),
                    description: $("#course_description").val(),
                    invite_code: $("#course_invitecode").val()
                }

            }
            let course_json = JSON.stringify(courseUpdata);
            $.ajax({
                url: "/course/rest/updateCourse",
                type: 'PATCH',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    retObjHandle(result, COURSES_TABLE.reload);
                }
            })
        };
        this.btnEvents[ElEM_ID.COURSE_EDIT_BTN] = COURSES_TABLE.edit;

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
                    retObjHandle(result, COURSES_TABLE.reload)
                }
            })
        };


        /**
         * Clean data in modal. when user click on
         * add course button.
         */
        this.create = function () {
            let data = {
                courseModal: {
                    active: "active",
                    create: true,
                    course: true,
                }
            }
            rebuildComponent(ElEM_ID.MODAL_UL, TEMPLATE_ID.MODAL, data, "click", COURSES_TABLE.btnEvents);
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

        //**********************************search lab**********************************
        //**********************************search lab**********************************
        //**********************************search lab**********************************
        this.searchLab = function () {
            $.ajax({
                url: "/lab/rest/loadLabList",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, (labs) => {
                        let data = {
                            iterable: labs.reverse(),
                        }
                        labs.forEach(e=>e["search"]=true);
                        rebuildRepeatComponent(ElEM_ID.COURSE_SEARCH_LAB_LIST_TBODY, TEMPLATE_ID.COURSE_LAB_TBODY,
                            "<tr/>", ".add", data, "click", (lab)=>{
                                COURSES_TABLE.addLabBtnEvent(lab)
                            });
                        $('.tooltipped').tooltip();
                    })
                }
            })
        }


        this.addLabBtnEvent = function (lab) {
            let course = {
                course_id: $("#course_code").val(),
                lab_ids: new Array()
            }
            course.lab_ids.push(lab.id);
            let course_json = JSON.stringify(course);
            $.ajax({
                url: "/course/rest/addLab",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    retObjHandle(result,
                        () => {
                            COURSES_TABLE.reLoadLabsList(course_json)
                        })
                }
            })
        };


        this.reLoadLabsList = function (course_json) {
            $.ajax({
                url: "/course/rest/getLabs",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    retObjHandle(result, (labs) => {
                        let data = {
                            iterable: labs,
                        }
                        data.iterable.forEach(e=>e["search"]=false);
                        rebuildRepeatComponent(ElEM_ID.COURSE_LAB_LIST_TBODY, TEMPLATE_ID.COURSE_LAB_TBODY,
                            "<tr/>", ".del", data, "click", (lab)=>{
                            COURSES_TABLE.delLabBtnEvent(lab)
                            });
                        $('.tooltipped').tooltip();
                    })
                }
            })
        };


        this.delLabBtnEvent = function (lab) {
            let course = {
                course_id: $("#course_code").val(),
                lab_ids: new Array()
            }
            course.lab_ids.push(lab.id);
            console.log(course)
            let course_json = JSON.stringify(course);
            $.ajax({
                url: "/course/rest/deleteLab",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    retObjHandle(result,
                        () => {
                        COURSES_TABLE.reLoadLabsList(course_json)
                    })
                }
            })
        };

    }
}


