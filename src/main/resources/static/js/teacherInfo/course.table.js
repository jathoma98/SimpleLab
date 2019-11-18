class Course {
    course_toggle = true;
    /**
     * Use to pull course list from server in teacher home page
     **/
    reload = function () {
        $.ajax({
            url: "/course/rest/loadCourseList",
            type: "GET",
            success: function (result) {
                $('#course_list tbody').empty();
                let data = {
                    isEnabled: this.course_toggle,
                    courses: result.reverse()
                }

                $('#course_list tbody')
                    .html(Mustache.render($("#course_tbody").html(), data));
                if (this.course_toggle) {
                    setTableBodyRowEvent($("#course_list tbody"), courseTableRowEvent, "#courseModal");
                }
            }
        })
    };

    /**
     * Rebuild component base on template.
     * @Param component component id.
     * @param template template id
     * @param data
     *
     * @return html_text
     **/
    rebuildComponent  = function (component, template, data) {
        $('#modal ul').empty();
        let html_text = Mustache.render($("#modalTpl").html(), data)
        $('#modal ul').html(html_text);
        return html_text;
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
        $.ajax({
            url: "/course/rest",
            type: 'POST',
            dataTye: 'json',
            contentType: 'application/json; charset=utf-8',
            data: course_json,
            success: function (result) {
                if (result.success === "true") {
                    this.reloadCourses();
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
                this.rebuildComponent(data);
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
        $.ajax({
            url: "/course/rest/deleteCourse",
            type: 'DELETE',
            dataTye: 'json',
            contentType: 'application/json; charset=utf-8',
            data: course_json,
            success: function (result) {
                if (result.success === "true") {
                    reloadCourses();
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
        this.rebuildComponent(data);
    };


    btnSwitch = function () {
        $(".coursecheckcol").toggle();
        this.course_toggle = !course_toggle;
        if (this.course_toggle) {
            setTableBodyRowEvent($("#course_list tbody"), courseTableRowEvent, "#modal");
        } else {
            removeTableBodyRowEvent($("#course_list tbody"))
        }
    }

}


$(document).ready(function () {
    let course = new Course()
    //Course
    $("#courseAddBtn").on("click", course.create);
    $("#courseSaveBtn").on("click", course.save);
    $("#courseDeleteBtn").on("click", course.delete);
    $("#courseEditBtn").on("click", course.btnSwitch);
    $("#courseBackBtn").on("click", course.btnSwitch);
    //Set tbody row event
    // setTableBodyRowEvent($("#course_list tbody"), courseTableRowEvent);
    reloadCourses();
})