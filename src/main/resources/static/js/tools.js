/**
 * @Param tbody Table Body
 * @Param eventFn event function
 *
 * Set event function for each row of Table body.
 */
function setTableBodyRowEvent(tbody, eventFn){
    tbody.find("tr").each(function(){
        $(this).on("click", eventFn);
        $(this).addClass("modal-trigger");
    })
}


/**
 * @Param tbody Table Body
 *
 * remove event function for each row of Table body.
 */
function removeTableBodyRowEvent(tbody){
    tbody.find("tr").each(function(){
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
rebuildComponent  = function (component, template, data, btnEvents) {
    $(component).empty();
    let html_text = Mustache.render($(template).html(), data)
    $(component).html(html_text);
    for(let key in btnEvents){
        $(key).on("click", btnEvents[key]);
    }
    return html_text;
};

