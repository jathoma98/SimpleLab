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
};
TEMPLATE_ID = {
    ALL_EQUIPMENT_LIST: "#allequip_list_template",
    LAB_EQUIPMENT_LIST: "#labequip_list_template",
    RECIPE_EQUIPMENT_LIST: "#recequip_list_template",
    RECIPE_LIST: "#recipe_template",
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
                                    url: "/recipe/rest/"+recipe.id,
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
        this.new_recipe = function (){
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
    init : function(){
        this.buildEquipmentList = function(){
            
        }
    }


}


$(document).ready(() => {
    EQUIPMENT.init();
    EQUIPMENT.loadAll();
    EQUIPMENT.loadInLab();

    RECIPE.init();
    RECIPE.load();
    RECIPE.onclickInit()
})