ElEM_ID = {
    ALL_EQUIPMENT_LIST : "#all_equipment",
};
TEMPLATE_ID ={
    ALL_EQUIPMENT_LIST: "#all_equipment_template",
};

EQUIPMENT = {
    init(){
        this.all_Equipment_info;
        this.load = function (){
            $.ajax({
                url: "/equipment/rest/loadEquipmentList",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, function () {
                        EQUIPMENT.all_Equipment_info = result.data;
                        let data = {
                            iterable: result.data,
                        }
                        rebuildRepeatComponent(ElEM_ID.ALL_EQUIPMENT_LIST, TEMPLATE_ID.ALL_EQUIPMENT_LIST, "<li/>", data, "click",
                            (eqm)=>{
                                console.log(eqm);
                            });
                    })
                }
            })
        };
    }
}

$(document).ready(()=>{
    EQUIPMENT.init();
    EQUIPMENT.load();
})