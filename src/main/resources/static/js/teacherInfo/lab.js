let LABS_TABLE = {
    init() {
        this.toggle = true;
        this.btnEvents = new Array();
        this.lab_info = undefined



        this.searchLab = function () {
            let toSearch = {
                regex: $("#labSearchLab").val()
            };
            let toSearch_json = JSON.stringify(toSearch);
            $.ajax({
                url: "/lab/rest/searchLab",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: toSearch_json,
                success: function (result) {
                    retObjHandle(result, (labs) => {
                        let data = {
                            students: labs,
                            search: true,
                        }
                        rebuildComponent(
                            ElEM_ID.LAB_SEARCH_RESULT_TBODY,
                            TEMPLATE_ID.LAB_SEARCH_TBODY,
                            data);
                    // setTableBodyRowBtnEvent(ElEM_ID.STUDENT_SEARCH_TBODY,
                    //     ".add_student",
                    //     "click",
                    //     COURSES_TABLE.addStudentBtnEvent)
                    })
                }
            })
        }
        this.btnEvents[ElEM_ID.LAB_SEARCH_LAB_BTN] = LABS_TABLE.searchLab();
        /**
         * Use to onclick Event on each row of lab list.
         **/
        this.tableRowEvent = function () {
            let lab_id = $(this).find(".myIdColumn").text();
            $.ajax({
                url: "/lab/rest/" + lab_id,
                type: 'GET',
                success: function (result) {
                    retObjHandle(result, (lab)=>{
                        let data = {
                            labModal: {
                                active: "active",
                                create: false,
                                lab: lab
                            }
                        }
                        LABS_TABLE.lab_info = lab;
                        rebuildComponent(ElEM_ID.MODAL_UL, TEMPLATE_ID.MODAL, data, LABS_TABLE.btnEvents);
                    })
                }
            })
        };

        /**
         * Use to pull lab list from server in teacher home page
         **/
        this.reload = function () {
            $.ajax({
                url: "/lab/rest/loadLabList",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, (labs)=>{
                        let data = {
                            isEnabled: LABS_TABLE.toggle,
                            labs: labs.reverse()
                        }
                        rebuildComponent(ElEM_ID.LAB_TABLE_TBODY, TEMPLATE_ID.LAB_TBODY, data);
                        if (LABS_TABLE.toggle) {
                            setTableBodyRowEvent(ElEM_ID.LAB_TABLE_TBODY, LABS_TABLE.tableRowEvent);
                        }
                    })

                }
            })
        };


        this.edit = function () {
            let labUpdata = {
                lab_id_old: LABS_TABLE.lab_info.id,
                newLabInfo: {
                    name: $("#lab_name").val(),
                    description: $("#lab_description").val()
                }
            }
            let lab_json = JSON.stringify(labUpdata);
            $.ajax({
                url: "/lab/rest/updateLab",
                type: 'PATCH',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: lab_json,
                success: function (result) {
                    retObjHandle(result, COURSES_TABLE.reload);
                }
            })
        };
        this.btnEvents[ElEM_ID.LAB_EDIT_BTN] = LABS_TABLE.edit;


        /**
         * Get lab data from input and text area, then
         * send to the server. If success, re-build course
         * table.
         */
        this.save = function () {
            let lab = {
                name: $("#lab_name").val(),
                description: $("#lab_description").val()
            }
            let lab_json = JSON.stringify(lab);
            $.ajax({
                url: "/lab/rest",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: lab_json,
                success: function (result) {
                    retObjHandle(result, LABS_TABLE.reload)

                }
            })
        };
        this.btnEvents[ElEM_ID.LAB_SAVE_BTN] = LABS_TABLE.save;

        /**
         * Delete LAB.
         **/
        this.delete = function () {
            let lids = [];
            $(ElEM_ID.LAB_TABLE_TBODY).find("tr").each(function (i, row) {
                if ($(row).find('input[type="checkbox"]').is(':checked')) {
                    lids.push(($(row).find(".myIdColumn").text()));
                }
            });
            let data = {lids:lids};
            let data_json = JSON.stringify(data);
            $.ajax({
                url: "/lab/rest/deleteLab",
                type: 'DELETE',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: data_json,
                success: function (result) {
                    retObjHandle(result, LABS_TABLE.reload);
                }
            })
        };


        /**
         * Clean data in modal. when user click on
         * add lab button.
         */
        this.create = function () {
            let data = {
                labModal:{
                    active: "active",
                    create: true,
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
