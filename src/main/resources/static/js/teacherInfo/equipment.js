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
        this.modal_info = undefined;

        /**
         * Use to onclick Event on each row of equipment list.
         **/
        this.tableRowEvent = function () {
            let equip_id = $(this).find(".myIdColumn").text();
            $.ajax({
                url: "/equipment/rest/" + equip_id,
                type: 'GET',
                success: function (result) {
                    retObjHandle(result, (equipment)=>{
                        let props = equipment.properties;
                        equipment.properties = [];
                        props.forEach(p=>equipment.properties[p.propertyKey] = p.propertyValue);
                        let data = {
                            equipmentModal: {
                                active: "active",
                                create: false,
                                equipment: equipment,
                            }
                        }
                        data.equipmentModal[equipment.type] = " checked";
                        EQUIPMENT_TABLE.modal_info = equipment;
                        rebuildComponent(ElEM_ID.MODAL_UL, TEMPLATE_ID.MODAL_EQUIP, data, EQUIPMENT_TABLE.btnEvents);
                    })
                }
            })
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
                            equip: result.data.reverse()
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
            let data = {
                equipment_id_old: EQUIPMENT_TABLE.modal_info.id,
                newEquipmentInfo: {
                    name: $(EQUIPMENT_TABLE.MODAL_ID.EQUIPMENT_NAME).val(),
                    type: $(EQUIPMENT_TABLE.MODAL_ID.EQUIPMENT_TYPE).val(),
                    properties: [
                        {propertyKey: "max_temperature", propertyValue: $(EQUIPMENT_TABLE.MODAL_ID.MAX_TEMPERATURE).val()},
                        {propertyKey: "min_temperature", propertyValue: $(EQUIPMENT_TABLE.MODAL_ID.MIN_TEMPERATURE).val()},
                        {propertyKey:"max_volume", propertyValue: $(EQUIPMENT_TABLE.MODAL_ID.MAX_VOLUME).val()},
                        {propertyKey:"max_weight", propertyValue: $(EQUIPMENT_TABLE.MODAL_ID.MAX_WEIGHT).val()}
                    ]
                }
            }
            let data_json = JSON.stringify(data);
            $.ajax({
                url: "/equipment/rest/updateEquipment",
                type: 'PATCH',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: data_json,
                success: function (result) {
                    retObjHandle(result, COURSES_TABLE.reload);
                }
            })
        };
        this.btnEvents[ElEM_ID.EQUIPMENT_EDIT_BTN] = EQUIPMENT_TABLE.edit;


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
            let ids = [];
            $(ElEM_ID.EQUIPMENT_TABLE_TBODY).find("tr").each(function (i, row) {
                if ($(row).find('input[type="checkbox"]').is(':checked')) {
                    ids.push(($(row).find(".myIdColumn").text()));
                }
            });
            let ids_json = JSON.stringify(ids);
            $.ajax({
                url: "/equipment/rest/deleteEquipment",
                type: 'DELETE',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: ids_json,
                success: function (result) {
                    retObjHandle(result, EQUIPMENT_TABLE.reload);
                }
            })
        };


        /**
         * Clean data in modal. when user click on
         * add lab button.
         */
        this.create = function () {
            let data = {
                equipmentModal: {
                    active: "active",
                    create: true,
                    equipment: true
                }
            }
            rebuildComponent(ElEM_ID.MODAL_UL, TEMPLATE_ID.MODAL_EQUIP, data, "click", EQUIPMENT_TABLE.btnEvents);
    };


        this.btnSwitch = function () {
            $(".equipcheckcol").toggle();
            EQUIPMENT_TABLE.toggle = !EQUIPMENT_TABLE.toggle;
            if (EQUIPMENT_TABLE.toggle) {
                setTableBodyRowEvent(ElEM_ID.EQUIPMENT_TABLE_TBODY, EQUIPMENT_TABLE.tableRowEvent);
            } else {
                removeTableBodyRowEvent(ElEM_ID.EQUIPMENT_TABLE_TBODY)
            }
        }
    }
}