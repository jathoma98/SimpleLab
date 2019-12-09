ElEM_ID = {
    ALL_EQUIPMENT_LIST : "#all_equipment",
    IN_LAB_EQUIPMENT_LIST: "#in_lab_equipment_list",
};
TEMPLATE_ID ={
    ALL_EQUIPMENT_LIST: "#allequip_list_template",
    LAB_EQUIPMENT_LIST: "#labequip_list_template",
};

EQUIPMENT = {
    init(){
        this.all_Equipment_info;
        this.in_lab_equipment;
        this.loadAll = function (){
            $.ajax({
                url: "/equipment/rest/loadEquipmentObjList",
                type: "GET",
                success: function (result) {
                    retObjHandle(result, function () {
                        EQUIPMENT.all_Equipment_info = result.data;
                        let data = {
                            iterable: result.data,
                        }
                        data.iterable.forEach((eqm)=>{eqm[eqm.type]=true;equipmentPropsToKeyValue(eqm);});
                        rebuildRepeatComponent(ElEM_ID.ALL_EQUIPMENT_LIST, TEMPLATE_ID.ALL_EQUIPMENT_LIST, "<li/>", "a", data, "click",
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
                        EQUIPMENT.in_lab_equipment = result.data;
                        let data = {
                            iterable: result.data,
                        }
                        data.iterable.forEach((eqm)=>{eqm[eqm.type]=true;equipmentPropsToKeyValue(eqm);});
                        rebuildRepeatComponent(ElEM_ID.IN_LAB_EQUIPMENT_LIST, TEMPLATE_ID.LAB_EQUIPMENT_LIST, "<li/>", undefined, data, "click",
                            (eqm)=>{
                                console.log(eqm);
                            });
                    })
                }
            })
        }
    }

}

$(document).ready(()=>{
    EQUIPMENT.init();
    EQUIPMENT.loadAll();
    EQUIPMENT.loadInLab();
})