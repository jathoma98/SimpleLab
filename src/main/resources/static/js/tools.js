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