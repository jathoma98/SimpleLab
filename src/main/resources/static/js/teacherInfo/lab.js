let LABS_TABLE = {
    init() {
        this.toggle = true;
        this.btnEvents = new Array();

        /**
         * Use to onclick Event on each row of lab list.
         **/
        this.tableRowEvent = function () {
            let lab_id = $(this).find(".myIdColumn").text();
            $.ajax({
                url: "/lab/rest/lab_id",
                type: 'GET',
                success: function (lab) {
                    let data = {
                        labModal: {
                            active: "active",
                            lab: lab,
                        }
                    }
                    rebuildComponent(ElEM_ID.MODAL_UL, TEMPLATE_ID.LAB_TBODY, data);
                }
            })
        };


        /**
         * Use to pull lab list from server in teacher home page
         **/
        this.reload = function () {
            $.ajax({
                url: "/lab/rest/",
                type: "GET",
                success: function (result) {
                    let data = {
                        isEnabled: LABS_TABLE.toggle,
                        courses: result.reverse()
                    }
                    rebuildComponent(ElEM_ID.LAB_TABLE_TBODY, TEMPLATE_ID.LAB_TBODY, data);
                    if (COURSES_TABLE.toggle) {
                        setTableBodyRowEvent(ElEM_ID.LAB_TABLE_TBODY, LABS_TABLE.tableRowEvent);
                    }
                }
            })
        };


        /**
         * Get lab data from input and text area, then
         * send to the server. If success, re-build course
         * table.
         */
        this.save = function () {
            let course = {
                name: $("#lab_name").val(),
                description: $("#lab_description").val()
            }
            let course_json = JSON.stringify(course);
            $.ajax({
                url: "/lab/rest",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: course_json,
                success: function (result) {
                    if (result.success === "true") {
                        LABS_TABLE.reload();
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
        };


        /**
         * Clean data in modal. when user click on
         * add lab button.
         */
        this.create = function () {
            let data = {
                labModal:{
                    active: "active",
                    lab: true
                }
            }
            rebuildComponent(ElEM_ID.MODAL_UL,TEMPLATE_ID.MODAL, data,  LABS_TABLE.btnEvents);
        };


        this.btnSwitch = function () {
            $(".labcheckcol").toggle();
            LABS_TABLE.toggle = !LABS_TABLE.toggle;
            if (LABS_TABLE.toggle) {
                setTableBodyRowEvent(ElEM_ID.LAB_TABLE_TBODY, LABS_TABLE.tableRowEvent);
            } else {
                removeTableBodyRowEvent(ElEM_ID.LAB_TABLE_TBODY)
            }
        }
    }
}
