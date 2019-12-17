/*
* Handler the retObj
* @Param retObj - obj return from the server rest controller
* @Param callback - function that deal with data.
*/
function retObjHandle(retObj, callback){
    switch(retObj.action){
        case "REDIRECT":
            break;
        case "LOAD_DATA":
            if(callback != undefined){
                callback(retObj.data);
            }
            break;
        case "PRINT_MSG":
            alert(retObj.msg);
            break;
        default:
            if(retObj.success == true){
                if(callback != undefined){
                    callback();
                }
            }
    }
}


/**
 * @Param tbody Table Body
 * @Param eventFn event function
 *
 * Set event function for each row of Table body.
 */
function setTableBodyRowEvent(tbody, eventFn){
    $(tbody).find("tr").each(function(){
        $(this).on("click", eventFn);
        $(this).addClass("modal-trigger");
    })
}

function setTableBodyRowBtnEvent(tbody, btn_class, action, eventFn){
    $(tbody).find("tr").each(function(){
        $(this).find(btn_class).on(action, eventFn);
    })
}


/**
 * @Param tbody Table Body
 *
 * remove event function for each row of Table body.
 */
function removeTableBodyRowEvent(tbody){
    $(tbody).find("tr").each(function(){
        $(this).off("click");
        $(this).removeClass("modal-trigger");
    })
}

/**
 * Pre-load template
 * **/
function templatePreLoad (){
    $('script[type="x-tmpl-mustache"]').each(function(idx, templateSource) {
        $.holdReady(true);
        $.get(templateSource.src, function(template) {
            templateSource.text = template;
            $.holdReady(false);
        });
    });
}

/**
 * Rebuild component base on template.
 * @Param component component id.
 * @param template template id
 * @param data
 *
 * @return html_text
 **/
rebuildComponent  = function (component, template_id, data, action, btnEvents) {
    $(component).empty();
    let template = $(template_id).html();
    let html_text = Mustache.render(template, data);
    $(component).html(html_text);
    for(let key in btnEvents){
        $(key).on("click", btnEvents[key]);
    }
    return html_text;
};

rebuildRepeatComponent = function (component, template_id, repeatElem, subRepeatElem, data, action, eventFn){
    $(component).empty();
    let template = $(template_id).html();
    data.iterable.forEach((obj)=>{
        let html_text = Mustache.render(template, obj);
        let repElem = $(repeatElem)
        repElem.append(html_text);
        if(subRepeatElem != undefined){
            repElem.find(subRepeatElem).on(action, (event)=>eventFn(obj, event));
        }else{
            repElem.on(action, (event)=>eventFn(obj, event));
        }
        repElem.appendTo(component);
    })
};

equipmentPropsToKeyValue = function (equipment){
    let props = equipment.properties;
    equipment.properties = [];
    props.forEach(p=>equipment.properties[p.propertyKey] = p.propertyValue);
}

equipmentPropsDolab = function (equipment){
    equipment.props = {}
    equipment.properties.forEach(p=> equipment.props[p.propertyKey] = p.propertyValue);
}

equipmentPropsSetBack = function (equipment){
    let props = equipment.properties;
    equipment.properties = equipment.old_prop;
}