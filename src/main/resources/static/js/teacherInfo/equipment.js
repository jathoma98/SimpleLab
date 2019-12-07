let EQUIPMENT_TABLE = {
    MODAL_ID: {
        EQUIPMENT_NAME: "#equip_name",
        EQUIPMENT_TYPE: "#equip_type input[type='radio']:checked",
        ITEM_NAME: "#item_name",
        MAX_VOLUME: "#max_volume",
        MAX_WEIGHT: "#max_weight",
        MIN_TEMPERATURE: "#min_temperature",
        MAX_TEMPERATURE: "#max_temperature"
    },

    init() {
        this.toggle = true;
        this.btnEvents = new Array();
        this.modal_info = undefined

        /**
         * Use to onclick Event on each row of lab list.
         **/
        this.tableRowEvent = function () {
        };


        /**
         * Use to pull lab list from server in teacher home page
         **/
        this.reload = function () {
            $.ajax({
                url: "/equipment/rest/loadEquipmentList",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, function () {
                        let data = {
                            isEnabled: EQUIPMENT_TABLE.toggle,
                            courses: result.data.reverse()
                        }
                        rebuildComponent(ElEM_ID.EQUIPMENT_TABLE_TBODY, TEMPLATE_ID.EQUIPMENT_TBODY, data);
                        if (EQUIPMENT_TABLE.toggle) {
                            setTableBodyRowEvent(ElEM_ID.EQUIPMENT_TABLE_TBODY, EQUIPMENT_TABLE.tableRowEvent);
                        }
                    })
                }
            })
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
            let validator = {
                name: $(EQUIPMENT_TABLE.MODAL_ID.EQUIPMENT_NAME).val(),
                type: $(EQUIPMENT_TABLE.MODAL_ID.EQUIPMENT_TYPE).val(),
                properties: [
                    {propertyKey: "max_temperature", propertyValue: $(EQUIPMENT_TABLE.MODAL_ID.MAX_TEMPERATURE).val()},
                    {propertyKey: "min_temperature", propertyValue: $(EQUIPMENT_TABLE.MODAL_ID.MIN_TEMPERATURE).val()},
                    {propertyKey:"max_volume", propertyValue: $(EQUIPMENT_TABLE.MODAL_ID.MAX_VOLUME).val()},
                    {propertyKey:"max_weight", propertyValue: $(EQUIPMENT_TABLE.MODAL_ID.MAX_WEIGHT).val()}
                ]
            }
            let validator_json = JSON.stringify(validator);
            $.ajax({
                url: "/equipment/rest",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: validator_json,
                success: function (result) {
                    retObjHandle(result, EQUIPMENT_TABLE.reload)
                }
            })
        };
        this.btnEvents[ElEM_ID.EQUIPMENT_SAVE_BTN] = EQUIPMENT_TABLE.save;


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
                EquipmentModal: {
                    active: "active",
                    create: "false",
                    equipment: true
                }
            }
            rebuildComponent(ElEM_ID.MODAL_UL, TEMPLATE_ID.MODAL_EQUIP, data, EQUIPMENT_TABLE.btnEvents);
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