ELEM_NAME = {
    OPEERATION_AREA: "#operation_area",
    ALL_EQUIPMENT_LIST: "#all_equipment",
    LAB_EQUIPMENT_LIST: "#lab_equipment_list",
    STEP_LIST: "#step_list",
    ADD_MODAL: "#add_modal",
    ADD_TO_WKSP_BTN: "#add_to_wksp",
    WKSP_EQM_LIST: "#wksp_eqm_list",
    INTERACTION_MODAL: "#interaction_modal",
};

TEMPLATE_ID = {
    ALL_EQUIPMENT_LIST: "#allequip_list_template",
    LAB_EQUIPMENT_LIST: "#labequip_list_template",
    WORKSPACE_EQM_ELEM: "#wksp_eqm_template",
    STEP_LIST: "#step_list_template",
};


class WorkSpaceEqmInfo {
    constructor(id, equipment, curr_val, curr_temp, html_elem, li_elem) {
        this.id = id; //equipment id on the work space.
        this.equipment = equipment; //equipment default info.
        this.curr_val = curr_val; //equipment current value on work space
        this.curr_temp = curr_temp;
        this.drag_elem = html_elem; //Jquery html element object (drag)
        this.li_elem = li_elem; //Jquery html element object (li)
        this.mix_list = []; //{equipment, value}
    }
    change(equipment, curr_val, mix){
        this.equipment = equipment;
        this.curr_val = curr_val;
        $(this.drag_elem).find("p").text(equipment.name);
        $(this.li_elem).empty();
        $(this.li_elem).append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {curr_val: curr_val, equipment:equipment}))
        if(mix != null){
            this.mix_list.push(mix);
        }
    }

};




TYPE = {
    LIQUID: "liquid",
    SOLID: "solid",
    MACHINE: "machine",
}

EQUIPMENT_DRAG_EVENT = {
    start: function (event, ui, eqm_wksp_obj) {
        EQUIPMENT_DRAG_EVENT.select(event, ui, eqm_wksp_obj);
        WORK_SPACE.last_drag = eqm_wksp_obj;
        console.log(eqm_wksp_obj);
    },
    stop: function (event, ui, eqm_wksp_obj) {
        WORK_SPACE.last_drag = eqm_wksp_obj;
    },
    drop: function (event, ui, eqm_wksp_obj) {
        let draggable = WORK_SPACE.last_drag;
        let droppable = eqm_wksp_obj;
        EQUIPMENT_DRAG_EVENT.setInteractionModal(draggable, droppable)
        $(ELEM_NAME.INTERACTION_MODAL).modal("open");
    },
    select: function (event, ui, eqm_wksp_obj){
        // if($(event.currentTarget).hasClass("draggable_item")){
        if(eqm_wksp_obj == WORK_SPACE.selected) return;
        if(WORK_SPACE.selected != undefined){
            $(WORK_SPACE.selected.drag_elem).removeClass("selected");
            $(WORK_SPACE.selected.li_elem).removeClass("selected");
        }
        if(eqm_wksp_obj != undefined ){
            $(eqm_wksp_obj.drag_elem).addClass("selected");
            $(eqm_wksp_obj.li_elem).addClass("selected");
            WORK_SPACE.selected = eqm_wksp_obj;
            EQUIPMENT_DRAG_EVENT.showInfo(eqm_wksp_obj);
        }else{
            WORK_SPACE.selected = undefined;
            if ($("#infobar").is(":visible")){
                $("#infobar").hide("slide", { direction: "right" }, 400);
            }
        }
    },
    showInfo:function(eqm_wksp_obj){
        let eqm = eqm_wksp_obj.equipment;
        if(!$("#infobar").is(":visible")){
            $("#infobar").show("slide", { direction: "right" }, 400);
        }
        switch (eqm.type) {
            case "liquid":
                $("#current_value_title").text("Current Volume(L): ");
                $("#max_value_title").text("Max Volume(L): ");
                $("#max_value").text(eqm.properties.max_volume);
                $("#current_value").text(eqm_wksp_obj.curr_val)
                $("#current_temperature").text(eqm.curr_temp)
                break;
            case "solid":
                $("#current_value_title").text("Current Weight(kg): ");
                $("#max_value_title").text("Max Weight(L): ");
                $("#max_value").text(eqm.properties.max_weight);
                $("#current_value").text(eqm_wksp_obj.curr_val)
                $("#current_temperature").text(eqm.curr_temp)
                break;
            default:
                $("#current_value_title").text("");
                $("#max_value_title").text("");
                $("#max_value").text("");
                $("#current_value").text("")
                $("#current_temperature").text("")
                break;
        }
        $("#name").text(eqm.name);
    },
    setInteractionModal:function(drag_eqm_wksp_obj, drop_eqm_wksp_obj){
        let title = ""
        let btnTitle = ""
        switch (drag_eqm_wksp_obj.equipment.type) {
            case TYPE.LIQUID:
                title += "Pour From " + drag_eqm_wksp_obj.equipment.name + " to " +drop_eqm_wksp_obj.equipment.name+ " (L)"
                btnTitle= "Pour";
                break;
            case TYPE.SOLID:
                title += "Take From " + drag_eqm_wksp_obj.equipment.name + " to " +drop_eqm_wksp_obj.equipment.name+ " (kg)"
                btnTitle= "Take";
                break;
        }
        //TODO: create Modal for setup value
        let btn = $(ELEM_NAME.INTERACTION_MODAL).find("a")
        $(ELEM_NAME.INTERACTION_MODAL).find("input").attr('max', drag_eqm_wksp_obj.curr_val);
        $(ELEM_NAME.INTERACTION_MODAL).find("input").val(0);
        $(ELEM_NAME.INTERACTION_MODAL).find(".popup_dialog_header").text(title);
        btn.text(btnTitle);
        btn.off("click");
        btn.on("click", ()=>{
            EQUIPMENT_DRAG_EVENT.interacting(drag_eqm_wksp_obj, drop_eqm_wksp_obj,
                $(ELEM_NAME.INTERACTION_MODAL).find("input").val());});
    },
    interacting(drag_eqm_wksp_obj, drop_eqm_wksp_obj, tranVal) {
        let one = drag_eqm_wksp_obj.equipment;
        let two = drop_eqm_wksp_obj.equipment;
        //Interaction With same value.
        if (one.id = one.id){
            drag_eqm_wksp_obj.curr_val -= tranVal;
        }
        //Interacting With two differnt iteam.
        let a = Number(tranVal) , b = Number(drop_eqm_wksp_obj.curr_val);
        let recipes = WORK_SPACE.recipes.find(r => {
            return r.equipmentOne.id == one.id && r.equipmentTwo.id == two.id;
        })
        let ratio_recipe = recipes.ratioOne / recipes.ratioTwo;
        let ratio_new = a / b;
        if (ratio_new > ratio_recipe) {
            let req_a = b * ratio_recipe;
            let lef_a = a - req_a;
            let c = req_a + b;
            drag_eqm_wksp_obj.curr_val -= tranVal;
            return drop_eqm_wksp_obj.change(recipes.result, c, {equipemt: one, value:lef_a })
        } else if (ratio_new < ratio_recipe) {
            let req_b = a * (1 / ratio_recipe);
            let lef_b = b - req_b;
            let c = a + req_b;
            drag_eqm_wksp_obj.curr_val -= tranVal;
            return drop_eqm_wksp_obj.change(recipes.result, c, {equipemt: two, value:lef_b })
        } else {
            c = a + b;
            drag_eqm_wksp_obj.curr_val -= tranVal;
            return drop_eqm_wksp_obj.change(recipes.result, c, undefined)
        }
        console.log(recipes);
    }
}

WORK_SPACE = {
    init() {
        this.selected_eqm;
        this.lab_equipment;
        this.steps;
        this.equips_in_workspace = [];
        this.equip_counter = 0;
        this.last_drag;
        this.recipes;
        this.loadRecipe = function (){
            $.ajax({
                url: "/recipe/rest/loadRecipe/" +  LAB_INFO.creator_id,
                type: 'GET',
                success: function (result) {
                    retObjHandle(result, () => {
                        WORK_SPACE.recipes = result.data;
                    });
                }
            })
        }
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
                                let title = "Enter Contain "
                                switch (eqm.type) {
                                    case TYPE.LIQUID:
                                        max_value = eqm.properties.max_volume;
                                        title += "Volume (L)"
                                        break;
                                    case TYPE.SOLID:
                                        max_value = eqm.properties.max_weight;
                                        title += "Weight (kg)"
                                        break;
                                }
                                //TODO: create Modal for setup value
                                $(ELEM_NAME.ADD_MODAL).find("input").attr('max', max_value);
                                $(ELEM_NAME.ADD_MODAL).find("input").val(0)
                                $(ELEM_NAME.ADD_MODAL).find(".popup_dialog_header").text(title)
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
            let template_li = $("<li/>");
            template_li.append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {curr_val: curr_val, equipment:eqm}));

            let eqm_wksp_obj = new WorkSpaceEqmInfo(WORK_SPACE.equip_counter, eqm,
                curr_val, eqm.properties.max_temperature, $(template), $(template_li));
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
            eqm_wksp_obj.drag_elem.on("click", (event, ui)=>{
                event.stopPropagation();
                EQUIPMENT_DRAG_EVENT.select(event, ui, eqm_wksp_obj);
            });
            eqm_wksp_obj.drag_elem.appendTo(ELEM_NAME.OPEERATION_AREA);
            eqm_wksp_obj.drag_elem.offset({top: 300, left: 500});
            //li elem set up
            eqm_wksp_obj.li_elem.on("click", (event, ui)=>{
                event.stopPropagation();
                EQUIPMENT_DRAG_EVENT.select(event, ui, eqm_wksp_obj);
            });
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
    WORK_SPACE.loadRecipe();
    $(ELEM_NAME.OPEERATION_AREA).on("click", (event)=>{EQUIPMENT_DRAG_EVENT.select(event);})


})