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
STEP_COUNTER = {
    wrong_step_count: 0,
    wrong_step_warning: (str) => {
        STEP_COUNTER.wrong_step_count++;
        alert(str);
        //TODO:set grade board
    }
}


class WorkSpaceEqmInfo {
    constructor(id, equipment, curr_val, curr_temp, html_elem, li_elem) {
        this.id = id; //equipment id on the work space.
        this.equipment = equipment; //equipment default info.
        this.curr_val = curr_val == undefined ? undefined : curr_val * 1; //equipment current value on work space
        this.curr_temp = curr_val == undefined ? undefined : curr_temp * 1;
        this.drag_elem = html_elem; //Jquery html element object (drag)
        this.li_elem = li_elem; //Jquery html element object (li)
        this.mix_list = []; //{equipment, value}
        this.purity = curr_val == undefined ? undefined : 100;
    }

    isOverflow() {
        let type = this.equipment.type;
        if (this.curr_val > (type == "liquid" ? this.equipment.max_volume : this.max_weight)) {
            let cutoff = this.equipment.max_volume - this.curr_val;
            let remain = this.curr_val - cutoff;
            let ratio_remain = remain / this.curr_val;
            this.mix_list.forEach(m => m.value * ratio_remain);
            this.curr_val = this.equipment.max_volume;
            STEP_COUNTER.wrong_step_warning("Equipment reach the max " + type + ", extra will be wast.")
        }
    }

    change(val_1, complex, val_2, eqm_2) {
        if (arguments.length == 1) {
            this.curr_val = val_1;
            this.mix_list = [];
            this.setPurity()
            this.isOverflow();
        }
        if (arguments.length == 2) {
            this.curr_val = val_1
            this.equipment = complex;
            this.setPurity()
            this.isOverflow();
            $(this.li_elem).empty();
            $(this.li_elem).append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {
                curr_val: this.curr_val,
                equipment: complex
            }))
            this.purity = 100;
            $(this.drag_elem).find("p").text(this.equipment.name + " (" + this.purity + ")%");
        }
        if (arguments.length == 4) {
            this.mix_list = [];
            this.curr_val = val_1 + val_2;
            if (val_1 > val_2) {
                this.equipment = complex;
                $(this.li_elem).empty();
                $(this.li_elem).append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {
                    curr_val: this.curr_val,
                    equipment: complex
                }))
                if (eqm_2 != null) {
                    this.mix_list.push({equipment: eqm_2, value: val_2});
                }
            } else if (val_1 < val_2) {
                this.equipment = eqm_2;
                $(this.li_elem).empty();
                $(this.li_elem).append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {
                    curr_val: this.curr_val,
                    equipment: eqm_2
                }))
                if (complex != null) {
                    this.mix_list.push({equipment: complex, value: val_1});
                }
            } else {
                val_2 = Math.round(val_2);
                this.equipment = complex;
                $(this.li_elem).empty();
                $(this.li_elem).append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {
                    curr_val: this.curr_val,
                    equipment: complex
                }))
                if (eqm_2 != null) {
                    this.mix_list.push({equipment: eqm_2, value: val_2});
                }
            }
            this.setPurity();
            this.isOverflow();
            $(this.drag_elem).find("p").text(this.equipment.name + " (" + this.purity + ")%");
        }
        //this function use to change image.
        this.setImage();
        if (this.purity != 100) return;
        WORK_SPACE.checkStepsWith(this)
    }

    getMaxValue() {
        switch (this.equipment.type) {
            case TYPE.LIQUID:
                return this.equipment.props.max_volume;
            case TYPE.SOLID:
                return this.equipment.props.max_weight;
        }
        return undefined;
    }

    setImage() {
        // make sure image is right image
        this.li_elem.find("img").attr("src", "../../../image/rest/" + this.equipment.id);
        this.li_elem.find("img").attr("src", "../../../image/rest/" + this.equipment.id);
        // If is machine we don't change image
        if (this.equipment.type == TYPE.MACHINE) {
            //remove old css
            this.li_elem.find("").removeClass("");
            this.drag_elem.find("").removeClass("");
            //add new css
            this.li_elem.find("").addClass("");
            this.drag_elem.find("").addClass("");
            return;
        }
        // If not a machine we change image
        let perc = this.curr_val / this.getMaxValue();
        let cssName = ""
        if (perc <= 0) {
            cssName = " cropimg ";
        } else if (perc <= 0.25) {
            cssName = " cropimg2 ";
        } else if (perc <= 0.50) {
            cssName = " cropimg3 ";
        } else if (perc <= 0.75) {
            cssName = " cropimg4 ";
        } else if (perc <= 1 || perc > 1) {
            cssName = " cropimg5 ";
        }
        //remove old css
        this.li_elem.find("img").removeClass();
        this.drag_elem.find("img").removeClass();
        //add new css
        this.li_elem.find("img").addClass(cssName);
        this.drag_elem.find("img").addClass(cssName);
        return;
    }

    setPurity() {
        let sum_mix = 0;
        this.mix_list.forEach(m => sum_mix += m.value);
        this.purity = Math.floor((1 - sum_mix / this.curr_val) * 100);
        if (this.purity > 97) {
            this.purity == 100;
            this.mix_list = [];
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
    select: function (event, ui, eqm_wksp_obj) {
        // if($(event.currentTarget).hasClass("draggable_item")){
        if (eqm_wksp_obj == WORK_SPACE.selected) return;
        if (WORK_SPACE.selected != undefined) {
            $(WORK_SPACE.selected.drag_elem).removeClass("selected");
            $(WORK_SPACE.selected.li_elem).removeClass("selected");
        }
        if (eqm_wksp_obj != undefined) {
            $(eqm_wksp_obj.drag_elem).addClass("selected");
            $(eqm_wksp_obj.li_elem).addClass("selected");
            WORK_SPACE.selected = eqm_wksp_obj;
            EQUIPMENT_DRAG_EVENT.showInfo(eqm_wksp_obj);
        } else {
            WORK_SPACE.selected = undefined;
            if ($("#infobar").is(":visible")) {
                $("#infobar").hide("slide", {direction: "right"}, 400);
            }
        }
    },
    showInfo: function (eqm_wksp_obj) {
        let eqm = eqm_wksp_obj.equipment;
        if (!$("#infobar").is(":visible")) {
            $("#infobar").show("slide", {direction: "right"}, 400);
        }
        switch (eqm.type) {
            case "liquid":
                $("#current_value_title").text("Current Volume(mL): ");
                $("#max_value_title").text("Max Volume(mL): ");
                $("#max_value").text(eqm.props.max_volume);
                $("#current_value").text(eqm_wksp_obj.curr_val)
                $("#current_temperature").text(eqm_wksp_obj.curr_temp)
                break;
            case "solid":
                $("#current_value_title").text("Current Weight(kg): ");
                $("#max_value_title").text("Max Weight(mL): ");
                $("#max_value").text(eqm.props.max_weight);
                $("#current_value").text(eqm_wksp_obj.curr_val)
                $("#current_temperature").text(eqm_wksp_obj.curr_temp)
                break;
            default:
                $("#current_value_title").text("");
                $("#max_value_title").text("");
                $("#max_value").text("");
                $("#current_value").text("")
                $("#current_temperature").text("")
                break;
        }
        $("#infobar ul").find(".mix_eqm").remove();
        eqm_wksp_obj.mix_list.forEach(m => {
            let mix_eqm_li = $('<li class="collection-item row mix_eqm"></li>');
            mix_eqm_li.append('<div class="col s6 mix_eqm_title"></div><div class="col s6 mix_eqm_value"></div>');
            mix_eqm_li.find(".mix_eqm_title").text(m.equipment.name);
            mix_eqm_li.find(".mix_eqm_value").text(m.value + (eqm.type == "liquid" ? " mL" : " kg"));
            $("#infobar ul").append(mix_eqm_li);
        })
        $("#name").text(eqm.name);
    },
    setInteractionModal: function (drag_eqm_wksp_obj, drop_eqm_wksp_obj) {
        if (drag_eqm_wksp_obj.equipment == TYPE.MACHINE && drop_eqm_wksp_obj.equipment == TYPE.MACHINE) {
            STEP_COUNTER.wrong_step_warning("Unknow Recipe machine to machine");
            return;
        }
        let title = "";
        let btnTitle = "";
        let max = "";
        switch (drag_eqm_wksp_obj.equipment.type) {
            case TYPE.LIQUID:
                title += "Pour From " + drag_eqm_wksp_obj.equipment.name + " to " + drop_eqm_wksp_obj.equipment.name + " (L)"
                btnTitle = "Pour";
                max = drag_eqm_wksp_obj.curr_val;
                break;
            case TYPE.SOLID:
                title += "Take From " + drag_eqm_wksp_obj.equipment.name + " to " + drop_eqm_wksp_obj.equipment.name + " (kg)"
                btnTitle = "Take";
                max = drag_eqm_wksp_obj.curr_val;
                break;
            case TYPE.MACHINE:
                title += "Take From " + drop_eqm_wksp_obj.equipment.name + " to " + drag_eqm_wksp_obj.equipment.name + " (Machine)";
                btnTitle = "Take";
                max = drop_eqm_wksp_obj.curr_val;
                break;
        }
        //TODO: create Modal for setup value
        let btn = $(ELEM_NAME.INTERACTION_MODAL).find("a")
        $(ELEM_NAME.INTERACTION_MODAL).find("input").attr('max', max);
        $(ELEM_NAME.INTERACTION_MODAL).find("input").val(0);
        $(ELEM_NAME.INTERACTION_MODAL).find(".popup_dialog_header").text(title);
        btn.text(btnTitle);
        btn.off("click");
        btn.on("click", () => {
            EQUIPMENT_DRAG_EVENT.interacting(drag_eqm_wksp_obj, drop_eqm_wksp_obj,
                $(ELEM_NAME.INTERACTION_MODAL).find("input").val());
        });
    },
    interacting(drag_eqm_wksp_obj, drop_eqm_wksp_obj, tranVal) {
        tranVal = tranVal * 1;
        let one = drag_eqm_wksp_obj.equipment;
        let two = drop_eqm_wksp_obj.equipment;

        //Interaction With same equipment.
        if (one.id == two.id && one.type != "machine") {
            drag_eqm_wksp_obj.change(drag_eqm_wksp_obj.curr_val - tranVal)
            let c = drop_eqm_wksp_obj.curr_val = drop_eqm_wksp_obj.curr_val * 1 + tranVal;
            drop_eqm_wksp_obj.change(c);
            return;
        }

        //Interacting With two differnt equipment.
        if (one.type != "machine" && two.type != "machine") {
            let a = 0, b = 0;
            if (drop_eqm_wksp_obj.purity == 100) {
                a = tranVal * 1, b = drop_eqm_wksp_obj.curr_val * 1;
                let recipe = WORK_SPACE.recipes.find(r => {
                    return r.equipmentOne.id == one.id && r.equipmentTwo.id == two.id;
                })
                if (recipe == null) {
                    STEP_COUNTER.wrong_step_warning("Unknow Recipe")
                    return
                }
                ;
                let ratio_recipe = recipe.ratioOne / recipe.ratioTwo;
                let ratio_new = a / b;
                if (ratio_new > ratio_recipe) {
                    let req_a = b * ratio_recipe;
                    let lef_a = a - req_a;
                    let c = req_a + b;
                    drag_eqm_wksp_obj.change(drag_eqm_wksp_obj.curr_val -= tranVal);
                    return drop_eqm_wksp_obj.change(Math.round(c), recipe.result, Math.round(lef_a), one)
                } else if (ratio_new < ratio_recipe) {
                    let req_b = a * (1 / ratio_recipe);
                    let lef_b = b - req_b;
                    let c = a + req_b;
                    drag_eqm_wksp_obj.change(drag_eqm_wksp_obj.curr_val -= tranVal);
                    return drop_eqm_wksp_obj.change(Math.round(c), recipe.result, Math.round(lef_b), two);
                } else {
                    c = a + b;
                    drag_eqm_wksp_obj.change(drag_eqm_wksp_obj.curr_val -= tranVal);
                    return drop_eqm_wksp_obj.change(c, recipe.result,)
                }
            } else {
                //TODO: when purity not equal to 100, change two to mix[0].equipment
                two = drop_eqm_wksp_obj.mix_list[0].equipment;
                a = tranVal * 1, b = drop_eqm_wksp_obj.mix_list[0].value * 1;
                let recipe = WORK_SPACE.recipes.find(r => {
                    return r.equipmentOne.id == one.id && r.equipmentTwo.id == two.id;
                })
                if (recipe == null) {
                    STEP_COUNTER.wrong_step_warning("Unknow Recipe");
                    return
                }
                if (drop_eqm_wksp_obj.equipment.id != recipe.result.id) {
                    STEP_COUNTER.wrong_step_warning("Unknow Recipe");
                    return;
                } else {
                    let ratio_recipe = recipe.ratioOne / recipe.ratioTwo;
                    let ratio_new = a / b;
                    if (ratio_new > ratio_recipe) {
                        let req_a = b * ratio_recipe;
                        let lef_a = a - req_a;
                        let c = req_a + b;
                        drag_eqm_wksp_obj.change(drag_eqm_wksp_obj.curr_val -= tranVal);
                        drop_eqm_wksp_obj.change(drop_eqm_wksp_obj.curr_val + Math.round(c), recipe.result, Math.round(lef_a), one)
                        return
                    } else if (ratio_new < ratio_recipe) {
                        let req_b = a * (1 / ratio_recipe);
                        let lef_b = b - req_b;
                        let c = a + req_b;
                        drag_eqm_wksp_obj.change(drag_eqm_wksp_obj.curr_val -= tranVal);
                        return drop_eqm_wksp_obj.change(drop_eqm_wksp_obj.curr_val + Math.round(c), recipe.result, Math.round(lef_b), two);
                    } else {
                        c = a + b;
                        drag_eqm_wksp_obj.curr_val -= tranVal;
                        return drop_eqm_wksp_obj.change(drop_eqm_wksp_obj.curr_val + c)
                    }
                }
            }
        }

        //Interacting With Machine
        if (one.type != "machine" && two.type == "machine") {
            if (drag_eqm_wksp_obj.purity !== 100) {
                STEP_COUNTER.wrong_step_warning("Unknow Recipe");
                return
            }
            ;
            let recipe = WORK_SPACE.recipes.find(r => {
                return r.equipmentOne.id == one.id && r.equipmentTwo.id == two.id;
            })
            if (recipe == null) {
                STEP_COUNTER.wrong_step_warning("Unknow Recipe");
                return
            }
            ;
            WORK_SPACE.AddEquipToWorkSpace(recipe.result, drag_eqm_wksp_obj.curr_val / (recipe.ratioOne / recipe.ratioThree));
            drag_eqm_wksp_obj.change(drag_eqm_wksp_obj.curr_val -= tranVal);
        }
        if (one.type == "machine" && two.type != "machine") {
            if (drop_eqm_wksp_obj.purity !== 100) {
                STEP_COUNTER.wrong_step_warning("Unknow Recipe");
                return
            }
            ;
            let recipe = WORK_SPACE.recipes.find(r => {
                return r.equipmentOne.id == one.id && r.equipmentTwo.id == two.id;
            })
            if (recipe == null) {
                STEP_COUNTER.wrong_step_warning("Unknow Recipe");
                return
            }
            ;
            drop_eqm_wksp_obj.change(drop_eqm_wksp_obj.curr_val -= tranVal);
            drag_eqm_wksp_obj.change(Math.round(drop_eqm_wksp_obj.curr_val / (recipe.ratioTwo / recipe.ratioThree)), recipe.result)
            EQUIPMENT_DRAG_EVENT.showInfo(drag_eqm_wksp_obj)
        }
        if (one.type == "machine" && two.type == "machine") {
            STEP_COUNTER.wrong_step_warning("Unknow Recipe (machine to machine)");
            return;
        }
    }
}

WORK_SPACE = {
    init() {
        STEP_COUNTER.wrong_step_count = 0;
        this.selected_eqm;
        this.lab_equipment;
        this.steps;
        this.equips_in_workspace = [];
        this.equip_counter = 0;
        this.last_drag;
        this.recipes;
        this.finish = false;
        this.complete_count = 0;

        this.setCompleteStep = function (step) {
            if(step.isComplete == true) return;
            step.isComplete = true;
            M.toast({html: '<h5>Step(' + step.stepNum + '): ' + step.targetName + ' is completed</h5>'})
            WORK_SPACE.save();
            WORK_SPACE.complete_count++;
            if (WORK_SPACE.complete_count == WORK_SPACE.steps.length) {
                alert("Congratulation! \nYou Completed this lab.")
                WORK_SPACE.finish = true;
            }
        }
        this.checkStepsWith= function (wksp_eqm) {
            WORK_SPACE.steps.forEach(s => {
                if (s.targetObject.id == wksp_eqm.equipment.id) {
                    let targetVal = 0;
                    let check = 2;
                    switch (s.targetObject.type) {
                        case TYPE.LIQUID:
                            targetVal = s.targetVolume * 1;
                            break;
                        case TYPE.SOLID:
                            targetVal = s.targetWeight * 1;
                            break;
                    }
                    if (targetVal <= wksp_eqm.curr_val * 1) {
                        check--;
                    }
                    if (s.targetTemperature == wksp_eqm.curr_temp * 1) {
                        check--;
                    } else if (s.targetTemperature == "" || s.targetObject == undefined) {
                        check--;
                    }
                    if (check > 0) {
                        s["isComplete"] = false;
                        return;
                    } else {
                        s.html_li.find(".check_star").removeClass("my_color_gray");
                        s.html_li.find(".check_star").addClass("my_color_red");
                        WORK_SPACE.setCompleteStep(s);
                    }
                }
            })
        }

        this.loadRecipe = function () {
            $.ajax({
                url: "/recipe/rest/loadRecipe/" + LAB_INFO.creator_id,
                type: 'GET',
                success: function (result) {
                    retObjHandle(result, () => {
                        WORK_SPACE.recipes = result.data;
                        WORK_SPACE.recipes.forEach(r => {
                                equipmentPropsDolab(r.equipmentOne);
                                equipmentPropsDolab(r.equipmentTwo);
                                equipmentPropsDolab(r.result);
                            }
                        );
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
                            equipmentPropsDolab(eqm);
                        });
                        rebuildRepeatComponent(ELEM_NAME.LAB_EQUIPMENT_LIST, TEMPLATE_ID.LAB_EQUIPMENT_LIST,
                            "<li/>", undefined, data, "click",
                            (eqm) => {
                                let max_value = 0;
                                let title = "Enter Contain "
                                switch (eqm.type) {
                                    case TYPE.LIQUID:
                                        max_value = eqm.props.max_volume;
                                        title += "Volume (L)"
                                        break;
                                    case TYPE.SOLID:
                                        max_value = eqm.props.max_weight;
                                        title += "Weight (kg)"
                                        break;
                                }
                                //TODO: create Modal for setup value
                                if (eqm.type != "machine") {
                                    $(ELEM_NAME.ADD_MODAL).find("input").attr('max', max_value);
                                    $(ELEM_NAME.ADD_MODAL).find("input").val(0)
                                    $(ELEM_NAME.ADD_MODAL).find(".popup_dialog_header").text(title)
                                    $(ELEM_NAME.ADD_TO_WKSP_BTN).off("click");
                                    $(ELEM_NAME.ADD_TO_WKSP_BTN).on("click", (event) => {
                                        WORK_SPACE.AddEquipToWorkSpace(eqm, $(ELEM_NAME.ADD_MODAL).find("input").val());
                                    })
                                } else {
                                    WORK_SPACE.AddEquipToWorkSpace(eqm);
                                }
                            });
                    })
                }
            })
        };

        this.AddEquipToWorkSpace = function (eqm, curr_val) {
            let eqm_wksp_obj = undefined;
            if (arguments.length == 1) {
                let template = Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM,
                    {
                        eqm_wksp_id: WORK_SPACE.last_equips_in_workspace_id++,
                        id: eqm.id,
                        name: eqm.name + " (Machine)",
                    });
                let template_li = $("<li/>");
                template_li.append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {equipment: eqm}));

                eqm_wksp_obj = new WorkSpaceEqmInfo(WORK_SPACE.equip_counter, eqm,
                    undefined, eqm.props.max_temperature, $(template), $(template_li));
                WORK_SPACE.equips_in_workspace.push(eqm_wksp_obj);
                WORK_SPACE.addDrag_elem(eqm_wksp_obj, {top: 300, left: 500});
                //li elem set up
                WORK_SPACE.addLi_elem(eqm_wksp_obj);
            }
            if (arguments.length == 2) {
                let template = Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM,
                    {
                        eqm_wksp_id: WORK_SPACE.last_equips_in_workspace_id++,
                        id: eqm.id,
                        name: eqm.name + " (100)%",
                    });
                let template_li = $("<li/>");
                template_li.append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {
                    curr_val: curr_val,
                    equipment: eqm
                }));
                eqm_wksp_obj = new WorkSpaceEqmInfo(WORK_SPACE.equip_counter, eqm,
                    curr_val, eqm.props.max_temperature, $(template), $(template_li));
                WORK_SPACE.equips_in_workspace.push(eqm_wksp_obj);
                WORK_SPACE.addDrag_elem(eqm_wksp_obj, {top: 300, left: 500});
                WORK_SPACE.addLi_elem(eqm_wksp_obj);
            }
            eqm_wksp_obj.change(eqm_wksp_obj.curr_val);
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
                            step.isComplete = false;
                            step.targetObject[step.targetObject.type] = true;
                            equipmentPropsDolab(step.targetObject);
                        });
                        WORK_SPACE.buildStepSideBar(result.data);

                    })
                }
            })
        }

        this.buildStepSideBar = function (steps) {
            // let data = {iterable: result.data};
            $(ELEM_NAME.STEP_LIST).empty();
            WORK_SPACE.steps = steps

            WORK_SPACE.steps.forEach(step => {
                equipmentPropsDolab(step.targetObject);
                let template_li = $("<li/>");
                template_li.append(Mustache.render($(TEMPLATE_ID.STEP_LIST).html(), step));
                template_li.appendTo(ELEM_NAME.STEP_LIST);
                step["html_li"] = template_li;
                if (step.isComplete == true) {
                    step.html_li.find(".check_star").removeClass("my_color_gray");
                    step.html_li.find(".check_star").addClass("my_color_red");
                    M.toast({html: '<h5>Step(' + step.stepNum + '): ' + step.targetName + ' is completed</h5>'});
                }

            })
        };

        this.addLi_elem = function (eqm_wksp_obj) {
            //li elem set up
            eqm_wksp_obj.li_elem.on("click", (event, ui) => {
                event.stopPropagation();
                EQUIPMENT_DRAG_EVENT.select(event, ui, eqm_wksp_obj);
            });
            eqm_wksp_obj.li_elem.appendTo(ELEM_NAME.WKSP_EQM_LIST);
        }
        this.addDrag_elem = function (eqm_wksp_obj, offset) {
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
            eqm_wksp_obj.drag_elem.on("click", (event, ui) => {
                event.stopPropagation();
                EQUIPMENT_DRAG_EVENT.select(event, ui, eqm_wksp_obj);
            });
            eqm_wksp_obj.drag_elem.appendTo(ELEM_NAME.OPEERATION_AREA);
            eqm_wksp_obj.drag_elem.offset(offset);
        }

        this.save = function () {
            let docs = [];
            WORK_SPACE.equips_in_workspace.forEach(wseqm => {
                let d = new savedoc(
                    wseqm.id,
                    wseqm.equipment,
                    wseqm.curr_val,
                    wseqm.curr_temp,
                    wseqm.drag_elem.html(),
                    wseqm.li_elem.html(),
                    wseqm.mix_list,
                    wseqm.purity);
                docs.push(d)
            })
            let data = {
                equipments: docs,
                step: WORK_SPACE.steps,
                labFinished: WORK_SPACE.finish,
            }
            let data_json = JSON.stringify(data);
            $.ajax({
                url: "/student/doLab/" + LAB_INFO.id + "/saveState",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: data_json,
                success: function (result) {
                    retObjHandle(result, undefined);
                }
            })
        }

        this.continue = function () {
            $.ajax({
                url: "/student/doLab/" + LAB_INFO.id,
                type: 'GET',
                success: function (result) {
                    retObjHandle(result, function () {
                        WORK_SPACE.buildStepSideBar(result.data.step);
                        result.data.equipment_instances.forEach(e => {
                            equipmentPropsDolab(e.equipment);
                            let template;
                            let template_li;
                            switch (e.type){
                                case TYPE.MACHINE:
                                    template = Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM,
                                        {
                                            eqm_wksp_id: WORK_SPACE.last_equips_in_workspace_id++,
                                            id: e.equipment.id,
                                            name: e.equipment.name + " (Machine)",
                                        });
                                    template_li = $("<li/>");
                                    template_li.append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {equipment: e.equipment}));
                                    break;
                                default:
                                    template = Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM,
                                        {
                                            eqm_wksp_id: WORK_SPACE.last_equips_in_workspace_id++,
                                            id:  e.equipment.id,
                                            name:  e.equipment.name + " (100)%",
                                        });
                                    template_li = $("<li/>");
                                    template_li.append(Mustache.render(TEMPLATE.WORKSPACE_EQM_ELEM_BAR, {
                                        curr_val: e.curr_val,
                                        equipment: e.equipment
                                    }));

                            }
                            let wsobj = new WorkSpaceEqmInfo(e.id, e.equipment, e.curr_val, e.curr_temp,
                                $(template), $(template_li), e.mix_list, e.purity)
                            WORK_SPACE.addDrag_elem(wsobj,{top: 300, left: 500});
                            WORK_SPACE.addLi_elem(wsobj);
                            wsobj.change(wsobj.curr_val);
                            WORK_SPACE.equips_in_workspace.push(wsobj);
                        })
                    });
                }
            })
        }
    }
}


class savedoc {
    constructor(id, equipment, curr_val, curr_temp, drag_elem, li_elem, mix_list, purity) {
        this.id = id; //equipment id on the work space.
        this.equipment = equipment; //equipment default info.
        this.curr_val = curr_val;
        this.curr_temp = curr_temp;
        this.drag_elem = drag_elem; //Jquery html element object (drag)
        this.li_elem = li_elem; //Jquery html element object (li)
        this.mix_list = []; //{equipment, value}
        this.purity = purity;
    }
}


$(document).ready(() => {
    WORK_SPACE.init();
    WORK_SPACE.loadEquipments();
    WORK_SPACE.loadSteps();
    WORK_SPACE.loadRecipe();
    $(ELEM_NAME.OPEERATION_AREA).on("click", (event) => {
        EQUIPMENT_DRAG_EVENT.select(event);
    })
    WORK_SPACE.continue()
    $("#restart").on("click", ()=>{
        WORK_SPACE.init();
        WORK_SPACE.loadEquipments();
        WORK_SPACE.loadSteps();
        WORK_SPACE.loadRecipe();
        $("#wksp_eqm_list").empty();
        $(".draggable_item").remove();
        M.toast({html: '<h5>"Restart Lab"</h5>'});

    })
})

function setSameValue(value) {
    $(".rangeval").val(value);
    $(".inputval").val(value);
}







