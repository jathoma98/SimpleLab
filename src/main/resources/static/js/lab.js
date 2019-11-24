var globalselect=undefined;
var sidebar_step_select=undefined;

$(document).ready(function(){
    $(".draggable_item").draggable({
        containment: "parent",
        start: startDrag,
        stop: stopDrag
    });
    $( ".draggable_item" ).droppable({
        drop: dropitem
    });

    $("#operation_area").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();
    })

    // to avoid click propagation from infobar to operation area
    $("#infobar").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();
    })

    // to avoid click propagation from sidebar to operation area
    $("#sidebar").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();
    })
    //to select item in sidebar steps
    $(".sidebar_selectable_item").click(function(event){
        if(sidebar_step_select==this.id){

        }else if(sidebar_step_select==undefined){
            sidebar_step_select=this.id;
            $("#"+this.id).addClass("ui-selected");
        }else{
            $("#"+sidebar_step_select).removeClass("ui-selected");
            $("#"+this.id).addClass("ui-selected");
            sidebar_step_select=this.id;
        }

    })




    $("#sidebar_add").click(function (event) {
        $("#equipmentModal").modal("open");
    })

    $(function(){
        $('#sidebar_trigger').hover(function(){
            $("#sidebar").show("slide", { direction: "left" }, 400);
        },function(){
            $("#sidebar").hide("slide", { direction: "left" }, 400);
        }).trigger('mouseleave');
    });




    //every item that added to the operation table should have the function below
    //every item should be selectable
    $("#item_one").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();
    })

    $("#item_two").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();

    })


});

function startDrag(event,ui){
    var itemid = $(this).attr("id");
    var item=document.getElementById(itemid);
    item.classList.add("zdeep");
}

function stopDrag(event,ui){
    var itemid = $(this).attr("id");
    var item=document.getElementById(itemid);
    item.classList.remove("zdeep");
}

// $(document).click(function(event) {
//     var id = event.target.id);
//     console.log(id);
// });

function dropitem(event, ui) {
    $("#modal1").modal("open");

    // this could be used to get two interactive items
    var draggableId = ui.draggable.attr("id");
    var droppableId = $(this).attr("id");
    console.log("drag item  " +draggableId+"drop item  "+droppableId )
}



function selectItem(id){
    if(id=="operation_area"){
        if(!(globalselect==undefined)){
            var unselect = document.getElementById(globalselect);
            unselect.classList.remove("selected")
        }
        $("#infobar").hide("slide", { direction: "right" }, 400);
        globalselect=undefined;
    }else if(id=="sidebar"){

    }else if(id=="infobar"){

    }else if(globalselect===undefined){
        globalselect=id;
        var element = document.getElementById(id);
        element.classList.add("selected");
        loadInfo(id);
        $("#infobar").show("slide", { direction: "right" }, 400);

    }else if(globalselect==id){

    }else{
        var unselect = document.getElementById(globalselect);
        unselect.classList.remove("selected")
        var element = document.getElementById(id);
        element.classList.add("selected");
        globalselect=id;
        $("#infobar").hide("slide", { direction: "right" }, 400);
        loadInfo(id);
        $("#infobar").show("slide", { direction: "right" }, 400);
    }
}


function loadInfo(id){
    var element = document.getElementById(id);
    //item info can be write here
    

}






