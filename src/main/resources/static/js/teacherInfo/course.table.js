class Course {
    course_toggle = true;
    /**
     * Use to pull course list from server in teacher home page
     **/
    reload = function (this_course) {
        let this_course_toggle;
        let this_tableRowEvent;
        if (this_course != undefined){
            this_course_toggle = this_course.course_toggle;
            this_tableRowEvent = this_course.tableRowEvent;
        }else{
            this_course_toggle = this.course_toggle;
            this_tableRowEvent = this.tableRowEvent;
        }
        $.ajax({
            url: "/course/rest/loadCourseList",
            type: "GET",
            success: function (result) {
                let data = {
                    isEnabled: this_course_toggle,
                    courses: result.reverse()
                }
                rebuildComponent('#course_list tbody', '#course_tbody', data);
                if (this_course_toggle) {
                    setTableBodyRowEvent($("#course_list tbody"), this_tableRowEvent, "#courseModal");
                }
            }
        })
    };

    /**
     * Get course data from input and text area, then
     * send to the server. If success, re-build course
     * table.
     */
    save = function () {

        let course = {
            name: $("#course_name").val(),
            course_id: $("#course_code").val(),
            description: $("#course_description").val()
        }
        let course_json = JSON.stringify(course);
        let this_reload = this.reload;
        let this_course = this;
        $.ajax({
            url: "/course/rest",
            type: 'POST',
            dataTye: 'json',
            contentType: 'application/json; charset=utf-8',
            data: course_json,
            success: function (result) {
                if (result.success === "true") {
                    this_reload(this_course);
                } else {
                    alert(result.error);
                }
            }
        })
    };

    /**
     * Use to onclick Event on each row of Course list.
     **/
    tableRowEvent = function () {
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
                course["active"] = "active";
                let data = {
                    courseInfo: course
                }
                rebuildComponent('#modal ul',"#modalTpl", data);
            }
        })
    };

    /**
     * Delete course.
     **/
    delete = function () {
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
        let this_reload = this.reload;
        let this_course = this;
        $.ajax({
            url: "/course/rest/deleteCourse",
            type: 'DELETE',
            dataTye: 'json',
            contentType: 'application/json; charset=utf-8',
            data: course_json,
            success: function (result) {
                if (result.success === "true") {
                    this_reload(this_course);
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
    create = function () {
        let data = {
            courseInfo: {
                active: "active"
            },
            studentList: true,
            labList: true
        }
        rebuildComponent('#modal ul',"#modalTpl", data);
    };


    btnSwitch = function () {
        $(".coursecheckcol").toggle();
        this.course_toggle = !this.course_toggle;
        if (this.course_toggle) {
            setTableBodyRowEvent($("#course_list tbody"), this.tableRowEvent, "#modal");
        } else {
            removeTableBodyRowEvent($("#course_list tbody"))
        }
    }

}


$(document).ready(function () {
    let course = new Course()
    //Course
    $("#courseAddBtn").on("click", ()=>{course.create()});
    $("#courseSaveBtn").on("click", ()=>{course.save()});
    $("#courseDeleteBtn").on("click", ()=>{course.delete()});
    $("#courseEditBtn").on("click", ()=>{course.btnSwitch()});
    $("#courseBackBtn").on("click", ()=>{course.btnSwitch()});
    //Set tbody row event
    course.reload();
})