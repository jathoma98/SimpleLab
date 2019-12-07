let EQUIPMENT_TABLE = {
    init() {
        this.toggle = true;
        this.btnEvents = new Array();
        this.lab_info = undefined

        /**
         * Use to onclick Event on each row of lab list.
         **/
        this.tableRowEvent = function () {
        };


        /**
         * Use to pull lab list from server in teacher home page
         **/
        this.reload = function () {
        };


        this.edit = function () {
        };
        this.btnEvents[ElEM_ID.LAB_EDIT_BTN] = LABS_TABLE.edit;


        /**
         * Get lab data from input and text area, then
         * send to the server. If success, re-build course
         * table.
         */
        this.save = function () {
        };
        this.btnEvents[ElEM_ID.LAB_SAVE_BTN] = LABS_TABLE.save;

        /**
         * Delete LAB.
         **/
        this.delete = function () {
        };


        /**
         * Clean data in modal. when user click on
         * add lab button.
         */
        this.create = function () {
            let data = {
                EquipmentModal:{
                    active: "active",
                    create: "false",
                    equipment: true
                }
            }
            rebuildComponent(ElEM_ID.MODAL_UL,TEMPLATE_ID.MODAL_EQUIP, data);
        };


        this.btnSwitch = function () {
            $(".equipcheckcol").toggle();
            EQUIPMENT_TABLE.toggle = !EQUIPMENT_TABLE.toggle;
            if (EQUIPMENT_TABLE.toggle) {
                // setTableBodyRowEvent(ElEM_ID.LAB_TABLE_TBODY, LABS_TABLE.tableRowEvent);
            } else {
                // removeTableBodyRowEvent(ElEM_ID.LAB_TABLE_TBODY)
            }
        }
    }
}