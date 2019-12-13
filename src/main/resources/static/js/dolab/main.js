ELEM_NAME = {
    ALL_EQUIPMENT_LIST: "#all_equipment",
    LAB_EQUIPMENT_LIST: "#lab_equipment_list",
    STEP_LIST: "#step_list",
};

TEMPLATE_ID = {
    ALL_EQUIPMENT_LIST: "#allequip_list_template",
    LAB_EQUIPMENT_LIST: "#labequip_list_template",
    STEP_LIST: "#step_list_template",
};

SIDE_BAR = {
    init(){
        this.lab_equipment;
        this.steps;
        this.loadEquipments = function () {
            $.ajax({
                url: "/lab/rest/" + LAB_INFO.id + "/equipment",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, function () {
                        SIDE_BAR.lab_equipment = result.data;
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
                                //TODO: create Modal for setup value
                                //TODO: Then add equipment to workspace
                                console.log(eqm)
                            });
                    })
                }
            })
        };



        this.AddEquipToWorkSpace = function(){

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
                        SIDE_BAR.steps = data.iterable;
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
    SIDE_BAR.init();
    SIDE_BAR.loadEquipments();
    SIDE_BAR.loadSteps();
})