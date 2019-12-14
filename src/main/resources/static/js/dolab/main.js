ELEM_NAME = {
    OPEERATION_AREA: "#operation_area",
    ALL_EQUIPMENT_LIST: "#all_equipment",
    LAB_EQUIPMENT_LIST: "#lab_equipment_list",
    STEP_LIST: "#step_list",
    ADD_MODAL: "#add_modal",
    ADD_TO_WKSP_BTN: "#add_to_wksp",
    WKSP_EQM_LIST: "#wksp_eqm_list",
};

TEMPLATE_ID = {
    ALL_EQUIPMENT_LIST: "#allequip_list_template",
    LAB_EQUIPMENT_LIST: "#labequip_list_template",
    WORKSPACE_EQM_ELEM: "#wksp_eqm_template",
    STEP_LIST: "#step_list_template",
};


class WorkSpaceEqmInfo {
    constructor(id, equipment, curr_val, html_elem, li_elem) {
        this.id = id; //equipment id on the work space.
        this.equipment = equipment; //equipment default info.
        this.curr_val = curr_val; //equipment current value on work space
        this.drag_elem = html_elem; //Jquery html element object (drag)
        this.li_elem = li_elem; //Jquery html element object (li)
    }
};




TYPE = {
    LIQUID: "liquid",
    SOLID: "solid",
    MACHINE: "machine",
}

EQUIPMENT_DRAG_EVENT = {
    start: function (event, ui, eqm_wksp_obj) {
        console.log(eqm_wksp_obj);
    },
    stop: function (event, ui, eqm_wksp_obj) {
        console.log(ui);
    },
    drop: function (event, ui, eqm_wksp_obj) {
        console.log(event);
    },
}


WORK_SPACE = {
    init() {
        this.lab_equipment;
        this.steps;
        this.equips_in_workspace = [];
        this.equip_counter = 0;
        this.loadEquipments = function () {
            $.ajax({
                url: "/lab/rest/" + LAB_INFO.id + "/equipment",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, function () {
                        WORK_SPACE.lab_equipment = result.data;
                        let data = {
                            iterable: result.data,
                        }
                        data.iterable.forEach((eqm) => {
                            eqm[eqm.type] = true;
                            equipmentPropsToKeyValue(eqm);
                        });
                        rebuildRepeatComponent(ELEM_NAME.LAB_EQUIPMENT_LIST, TEMPLATE_ID.LAB_EQUIPMENT_LIST,
                            "<li/>", undefined, data, "click",
                            (eqm) => {
                                let max_value = 0;
                                switch (eqm.type) {
                                    case TYPE.LIQUID:
                                        max_value = eqm.properties.max_volume;
                                        break;
                                    case TYPE.SOLID:
                                        max_value = eqm.properties.max_weight;
                                        break;
                                }
                                //TODO: create Modal for setup value
                                $(ELEM_NAME.ADD_MODAL).find("input").attr('max', max_value);
                                $(ELEM_NAME.ADD_MODAL).find("input").val(0)
                                $(ELEM_NAME.ADD_TO_WKSP_BTN).off("click");
                                $(ELEM_NAME.ADD_TO_WKSP_BTN).on("click", (event) => {
                                    WORK_SPACE.AddEquipToWorkSpace(eqm, $(ELEM_NAME.ADD_MODAL).find("input").val());
                                })
                            });
                    })
                }
            })
        };


        this.AddEquipToWorkSpace = function (eqm, curr_val) {
            let template = Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM,
                {
                    eqm_wksp_id: WORK_SPACE.last_equips_in_workspace_id,
                    name: eqm.name,
                });
            let template_li = Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {curr_val: curr_val, equipment:eqm});

            let eqm_wksp_obj = new WorkSpaceEqmInfo(WORK_SPACE.equip_counter, eqm, curr_val, $(template), $(template_li));
            WORK_SPACE.equips_in_workspace.push(eqm_wksp_obj);
            //drag elem set up
            eqm_wksp_obj.drag_elem.draggable({
                containment: ELEM_NAME.OPEERATION_AREA,
                start: (event, ui) => {
                    EQUIPMENT_DRAG_EVENT.start(event, ui, eqm_wksp_obj)
                },
                stop: (event, ui) => {
                    EQUIPMENT_DRAG_EVENT.stop(event, ui, eqm_wksp_obj)
                },
            });
            eqm_wksp_obj.drag_elem.droppable({
                drop: (event, ui) => {
                    EQUIPMENT_DRAG_EVENT.drop(event, ui, eqm_wksp_obj)
                },
            });
            eqm_wksp_obj.drag_elem.appendTo(ELEM_NAME.OPEERATION_AREA);
            eqm_wksp_obj.drag_elem.offset({top: 300, left: 500});
            //li elem set up
            eqm_wksp_obj.li_elem.on("click", (event)=>{
                console.log(eqm_wksp_obj);
                //TODO: highlight the li element and drag element, move z-index to the top
            })
            eqm_wksp_obj.li_elem.appendTo(ELEM_NAME.WKSP_EQM_LIST);
        };


        this.loadSteps = function () {
            $.ajax({
                url: "/lab/rest/" + LAB_INFO.id + "/step",
                type: 'GET',
                success: function (result) {
                    retObjHandle(result, () => {
                        if (result.data == undefined) return;
                        result.data = result.data.sort((a, b) => a.stepNum - b.stepNum);
                        result.data.forEach((step) => {
                            step.targetObject[step.targetObject.type] = true;
                            equipmentPropsToKeyValue(step.targetObject);
                        });
                        let data = {iterable: result.data};
                        WORK_SPACE.steps = data.iterable;
                        rebuildRepeatComponent(ELEM_NAME.STEP_LIST, TEMPLATE_ID.STEP_LIST,
                            "<li/>", undefined, data, "click",
                            (step, event) => {
                                //TODO:
                            });
                    })
                }
            })
        }
    }
}


$(document).ready(() => {
    WORK_SPACE.init();
    WORK_SPACE.loadEquipments();
    WORK_SPACE.loadSteps();
})