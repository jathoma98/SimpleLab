ELEM_NAME = {
    ALL_EQUIPMENT_LIST : "#all_equipment",
    LAB_EQUIPMENT_LIST: "#lab_equipment_list",
    RECIPE_CARDS:".recipe_equips",
    RECIPE_CARD_ONE:"#equip_1",
    RECIPE_CARD_TWO:"#equip_2",
    RECIPE_CARD_RESULT:"#equip_r",
    RECIPE_EQUIP_LIST:"#recipe_equip_list",
};
TEMPLATE_ID ={
    ALL_EQUIPMENT_LIST: "#allequip_list_template",
    LAB_EQUIPMENT_LIST: "#labequip_list_template",
};

EQUIPMENT = {
    init(){
        this.all_equipment;
        this.lab_equipment;
        this.loadAll = function (){
            $.ajax({
                url: "/equipment/rest/loadEquipmentObjList",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, function () {
                        EQUIPMENT.all_equipment = result.data;
                        let data = {
                            iterable: result.data,
                        }
                        data.iterable.forEach((eqm)=>{eqm[eqm.type]=true;equipmentPropsToKeyValue(eqm);});
                        rebuildRepeatComponent(ELEM_NAME.ALL_EQUIPMENT_LIST, TEMPLATE_ID.ALL_EQUIPMENT_LIST,
                            "<li/>", "a", data, "click",
                            (eqm)=>{
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
                        data.iterable.forEach((eqm)=>{eqm[eqm.type]=true;equipmentPropsToKeyValue(eqm);});
                        rebuildRepeatComponent(ELEM_NAME.LAB_EQUIPMENT_LIST, TEMPLATE_ID.LAB_EQUIPMENT_LIST,
                            "<li/>", undefined, data, "click",
                            (eqm)=>{
                                console.log(eqm);
                            });
                    })
                }
            })
        }
    }
}

RECIPE ={
    selected: "",
    recipe:{
        equipmentOne:"",
        equipmentTwo:"",
        result:"",
        rationOne:"",
        rationTwo:"",
    },
    init(){
        this.setCard = function(eqm){

        };
        this.selectCard = function(event){
            let isClick_ignore = $(event.target).hasClass("click_ignore");
            if(isClick_ignore) return;
            let select_id = "#" + event.currentTarget.id;
            RECIPE.selected = select_id;
            let data = {iterable: EQUIPMENT.lab_equipment}
            switch (select_id) {
                case ELEM_NAME.RECIPE_CARD_ONE:
                    rebuildRepeatComponent(ELEM_NAME.RECIPE_EQUIP_LIST, TEMPLATE_ID.LAB_EQUIPMENT_LIST,
                        "<li/>", undefined, data, "click",
                        (eqm)=>{
                            RECIPE.recipe.equipmentOne = eqm.id;
                            $(ELEM_NAME.RECIPE_CARD_ONE).find("p").text("Equipment 1:" + eqm.name);
                        });
                    break;
                case ELEM_NAME.RECIPE_CARD_TWO:
                    rebuildRepeatComponent(ELEM_NAME.RECIPE_EQUIP_LIST, TEMPLATE_ID.LAB_EQUIPMENT_LIST,
                        "<li/>", undefined, data, "click",
                        (eqm)=>{
                            RECIPE.recipe.equipmentTwo = eqm.id;
                            $(ELEM_NAME.RECIPE_CARD_TWO).find("p").text("Equipment 2:" + eqm.name);
                        });
                    break;
                case ELEM_NAME.RECIPE_CARD_RESULT:
                    data.iterable = EQUIPMENT.all_equipment;
                    rebuildRepeatComponent(ELEM_NAME.RECIPE_EQUIP_LIST, TEMPLATE_ID.LAB_EQUIPMENT_LIST,
                        "<li/>", undefined, data, "click",
                        (eqm)=>{
                            RECIPE.recipe.result = eqm.id;
                            $(ELEM_NAME.RECIPE_CARD_RESULT).find("p").text("Result:" + eqm.name);
                        });
                    break;
            }
            console.log(RECIPE.recipe);
        }
    },
    onclickInit(){
        $(ELEM_NAME.RECIPE_CARDS).on("click", RECIPE.selectCard)
    }
}

$(document).ready(()=>{
    EQUIPMENT.init();
    EQUIPMENT.loadAll();
    EQUIPMENT.loadInLab();

    RECIPE.init();
    RECIPE.onclickInit()
})