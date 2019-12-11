ELEM_NAME = {
    ALL_EQUIPMENT_LIST: "#all_equipment",
    LAB_EQUIPMENT_LIST: "#lab_equipment_list",
    RECIPE_CARDS: ".recipe_equips",
    RECIPE_CARD_ONE: "#equip_1",
    RECIPE_CARD_TWO: "#equip_2",
    RECIPE_CARD_RESULT: "#equip_r",
    RECIPE_EQUIP_LIST: "#recipe_equip_list",
    RECIPE_LIST: "#recipe_list",
    RECIPE_SAVE_BTN: "#recipe_save_btn",
    RECIPE_NEW_BTN: "#recipe_new_btn",
    STEP_EQUIP_LIST: "#step_equip_list",
    STEP_LIST: "#step_list",
    STEP_CARD: "#step_card",
    STEP_I_VOLUME: "#step_volume",
    STEP_I_WEIGHT: "#step_weight",
    STEP_I_TEMPERATURE: "#step_temperature",
    STEP_SAVE_BTN: ".stepsavebtn",
};

TEMPLATE_ID = {
    ALL_EQUIPMENT_LIST: "#allequip_list_template",
    LAB_EQUIPMENT_LIST: "#labequip_list_template",
    RECIPE_EQUIPMENT_LIST: "#recequip_list_template",
    RECIPE_LIST: "#recipe_template",
    STEP_EQUIPMENT_LIST: "#stepequip_list_template",
    STEP_LIST: "#step_list_template",
};

EQUIPMENT = {
    init() {
        this.all_equipment;
        this.lab_equipment;
        this.loadAll = function () {
            $.ajax({
                url: "/equipment/rest/loadEquipmentObjList",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, function () {
                        EQUIPMENT.all_equipment = result.data;
                        let data = {
                            iterable: result.data,
                        }
                        data.iterable.forEach((eqm) => {
                            eqm[eqm.type] = true;
                            equipmentPropsToKeyValue(eqm);
                        });
                        rebuildRepeatComponent(ELEM_NAME.ALL_EQUIPMENT_LIST, TEMPLATE_ID.ALL_EQUIPMENT_LIST,
                            "<li/>", "a", data, "click",
                            (eqm) => {
                                EQUIPMENT.addToLab(eqm);
                            });
                        STEP.buildEquipmentList();
                    })
                }
            })
        };

        this.addToLab = function (eqm) {
            let data = [];
            data.push(eqm.id);
            let data_json = JSON.stringify(data);
            $.ajax({
                url: "/lab/rest/" + LAB_INFO.id + "/equipment",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: data_json,
                success: function (result) {
                    retObjHandle(result, EQUIPMENT.loadInLab);
                }
            })
        }

        this.loadInLab = function () {
            $.ajax({
                url: "/lab/rest/" + LAB_INFO.id + "/equipment",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, function () {
                        EQUIPMENT.lab_equipment = result.data;
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
                            });
                    })
                }
            })
        }
    }
}

RECIPE = {
    selected: "",
    recipe: {
        equipmentOne: "",
        equipmentTwo: "",
        result: "",
        ratioOne: "",
        ratioTwo: "",
    },
    init() {
        this.setCard = function (eqm) {

        };
        this.selectCard = function (event) {
            let target = $(event.target);
            let isClick_ignore = target.hasClass("click_ignore");
            if (isClick_ignore) return;
            $(ELEM_NAME.RECIPE_CARDS).removeClass("card_selected")
            $(this).addClass("card_selected")
            let select_id = "#" + event.currentTarget.id;
            RECIPE.selected = select_id;
            let data = {iterable: EQUIPMENT.lab_equipment}
            switch (select_id) {
                case ELEM_NAME.RECIPE_CARD_ONE:
                    rebuildRepeatComponent(ELEM_NAME.RECIPE_EQUIP_LIST, TEMPLATE_ID.RECIPE_EQUIPMENT_LIST,
                        "<li/>", undefined, data, "click",
                        (eqm) => {
                            RECIPE.recipe.equipmentOne = eqm.id;
                            $(ELEM_NAME.RECIPE_CARD_ONE).find("p").text("Equipment 1:" + eqm.name);
                        });
                    break;
                case ELEM_NAME.RECIPE_CARD_TWO:
                    rebuildRepeatComponent(ELEM_NAME.RECIPE_EQUIP_LIST, TEMPLATE_ID.RECIPE_EQUIPMENT_LIST,
                        "<li/>", undefined, data, "click",
                        (eqm) => {
                            RECIPE.recipe.equipmentTwo = eqm.id;
                            $(ELEM_NAME.RECIPE_CARD_TWO).find("p").text("Equipment 2:" + eqm.name);
                        });
                    break;
                case ELEM_NAME.RECIPE_CARD_RESULT:
                    data.iterable = EQUIPMENT.all_equipment;
                    rebuildRepeatComponent(ELEM_NAME.RECIPE_EQUIP_LIST, TEMPLATE_ID.RECIPE_EQUIPMENT_LIST,
                        "<li/>", undefined, data, "click",
                        (eqm) => {
                            RECIPE.recipe.result = eqm.id;
                            $(ELEM_NAME.RECIPE_CARD_RESULT).find("p").text("Result:" + eqm.name);
                        });
                    break;
            }
        };
        this.load = function () {
            $.ajax({
                url: "/recipe/rest/loadRecipe",
                type: 'GET',
                success: function (result) {
                    retObjHandle(result, () => {
                        let data = {
                            iterable: result.data,
                        }
                        rebuildRepeatComponent(ELEM_NAME.RECIPE_LIST, TEMPLATE_ID.RECIPE_LIST,
                            "<li/>", "a", data, "click",
                            (recipe) => {
                                $.ajax({
                                    url: "/recipe/rest/" + recipe.id,
                                    type: 'DELETE',
                                    success: function (result) {
                                        retObjHandle(result, RECIPE.load)
                                    }
                                })

                                // $(ELEM_NAME.RECIPE_CARDS).removeClass("card_selected");
                                // RECIPE.selected = "";
                                // $(ELEM_NAME.RECIPE_EQUIP_LIST).empty();
                                // $(ELEM_NAME.RECIPE_CARD_ONE).find("p")
                                //     .text("Equipment 1:" + recipe.equipmentOne.name);
                                // $(ELEM_NAME.RECIPE_CARD_ONE).find("input")
                                //     .val(recipe.ratioOne);
                                // $(ELEM_NAME.RECIPE_CARD_TWO).find("p")
                                //     .text("Equipment 2:" + recipe.equipmentTwo.name);
                                // $(ELEM_NAME.RECIPE_CARD_TWO).find("input")
                                //     .val(recipe.ratioTwo);
                                // $(ELEM_NAME.RECIPE_CARD_RESULT).find("p")
                                //     .text("Result:" + recipe.result.name);
                            });
                    });
                }
            })
        }
        this.save = function () {
            RECIPE.recipe.ratioOne = $(ELEM_NAME.RECIPE_CARD_ONE).find("input").val();
            RECIPE.recipe.ratioTwo = $(ELEM_NAME.RECIPE_CARD_TWO).find("input").val();
            let data_json = JSON.stringify(RECIPE.recipe);
            $.ajax({
                url: "/recipe/rest",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: data_json,
                success: function (result) {
                    retObjHandle(result, RECIPE.load);
                }
            })
        };
        this.new_recipe = function () {
            $(ELEM_NAME.RECIPE_CARDS).removeClass("card_selected");
            RECIPE.selected = "";
            $(ELEM_NAME.RECIPE_EQUIP_LIST).empty();
            $(ELEM_NAME.RECIPE_CARD_ONE).find("p")
                .text("Equipment 1");
            $(ELEM_NAME.RECIPE_CARD_ONE).find("input")
                .val("");
            $(ELEM_NAME.RECIPE_CARD_TWO).find("p")
                .text("Equipment 2");
            $(ELEM_NAME.RECIPE_CARD_TWO).find("input")
                .val("");
            $(ELEM_NAME.RECIPE_CARD_RESULT).find("p")
                .text("Result");
        }
    },
    onclickInit() {
        $(ELEM_NAME.RECIPE_CARDS).on("click", RECIPE.selectCard);
        $(ELEM_NAME.RECIPE_SAVE_BTN).on("click", RECIPE.save);
        $(ELEM_NAME.RECIPE_NEW_BTN).on("click", RECIPE.new_recipe);
    }
}

STEP = {
    selectFrom: undefined,
    selected: undefined,
    init: function () {
        this.buildEquipmentList = function () {
            let data = {iterable: EQUIPMENT.all_equipment};
            rebuildRepeatComponent(ELEM_NAME.STEP_EQUIP_LIST, TEMPLATE_ID.STEP_EQUIPMENT_LIST,
                "<li/>", undefined, data, "click",
                (eqm, event) => {
                    selectFrom = "equipment";
                    STEP.selected = eqm;
                    $(ELEM_NAME.STEP_EQUIP_LIST).find("li").removeClass("eqmli_selected");
                    $(event.currentTarget).addClass("eqmli_selected");
                    $(ELEM_NAME.STEP_CARD).find("p").text("Target:" + eqm.name);
                });
        }
        this.save = function () {
            if (STEP.selectFrom == "step" || STEP.selected == undefined) {
                return;
            }
            data = {
                labId: LAB_INFO.id,
                targetEquipmentId: STEP.selected.id,
                targetTemperature: $(ELEM_NAME.STEP_I_TEMPERATURE).val(),
                targetVolume: $(ELEM_NAME.STEP_I_VOLUME).val(),
                targetWeight: $(ELEM_NAME.STEP_I_WEIGHT).val(),
            }
            let data_json = JSON.stringify(data);
            $.ajax({
                url: "/lab/rest/" + LAB_INFO.id + "/step",
                type: 'POST',
                dataTye: 'json',
                contentType: 'application/json; charset=utf-8',
                data: data_json,
                success: function (result) {
                    retObjHandle(result, null);
                }
            })
        }
        this.load = function () {
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
                        rebuildRepeatComponent(ELEM_NAME.STEP_LIST, TEMPLATE_ID.STEP_LIST,
                            "<li/>", "a", data, "click",
                            (step, event) => {
                                let changeStepNum = () => {
                                }
                                if ($(event.target).hasClass("step_move_up") || $(event.target).hasClass("step_move_down")) {
                                    $.ajax({
                                        url: "/lab/rest/" + LAB_INFO.id + "/" + step.stepNum + "/" + ($(event.target).hasClass("step_move_up") ? -1 : 1),
                                        type: 'GET',
                                        success: (result) => {
                                            retObjHandle(result, STEP.load())
                                        }
                                    });
                                }
                                else if($(event.target).hasClass("step_remove")){
                                    $.ajax({
                                        url: "/lab/rest/" + LAB_INFO.id + "/" + step.stepNum,
                                        type: 'DELETE',
                                        success:(result)=>{
                                            retObjHandle(result, STEP.load)
                                        }
                                    })
                                }
                        });
                    })
                }
            })
        }
    },
    onclickInit() {
        $(ELEM_NAME.STEP_SAVE_BTN).on("click", STEP.save);

    }

}
$(document).ready(() => {
    EQUIPMENT.init();
    RECIPE.init();
    STEP.init();

    EQUIPMENT.loadAll();
    EQUIPMENT.loadInLab();

    RECIPE.load();
    RECIPE.onclickInit()

    STEP.load();
    STEP.onclickInit();

})