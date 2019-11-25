var globalselect=undefined;
var sidebar_step_select=undefined;
var itemcount=0;
var current_drag=undefined;
var current_drap=undefined;

$(document).ready(function(){

    // add item to operation area when click sidebar item
    $(".sidebaritem").click(function (event) {
        var item_id="item_"+itemcount;
        var c = $('<div id='+item_id+' class="ui-widget-content draggable_item center">' +
            '<img src="../img/beaker.png" class="item_img">' +
            '<p >200ml H2SO4</p>' +
            '</div>');

        c.click(function (event) {
            selectItem(this.id);
            event.stopPropagation();
        })
        c.draggable({
            containment: "parent",
            start: startDrag,
            stop: stopDrag
        });
        c.droppable({
            drop: dropitem
        });
        $("#operation_area").append(c);
        c.offset({top:150,left:100})
        itemcount++;
    })

    // to avoid propagation
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

    //to open add equipment modal
    $("#sidebar_add").click(function (event) {
        $("#equipmentModal").modal("open");
    })



    //to open add compound modal
    $("#addcompound").click(function (event) {
        $("#compoundmodal").modal("open");
    })

    //side bar on hover
    $(function(){
        $('#sidebar_trigger').hover(function(){
            $("#sidebar").show("slide", { direction: "left" }, 400);
        },function(){
            $("#sidebar").hide("slide", { direction: "left" }, 400);
        }).trigger('mouseleave');
    });

    //for popup dialog rotate back
    //not complete
    $(".popup_dialog").modal({
        onCloseEnd: function (event) {
            alert("close")
            $("#"+current_drag).css({"transform": "rotate(180deg)"});
        }
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


function dropitem(event, ui) {
    $("#modal1").modal("open");

    // this could be used to get two interactive items
    var draggableId = ui.draggable.attr("id");
    var droppableId = $(this).attr("id");
    current_drag=draggableId;
    current_drap=droppableId;
    console.log("drag item  " +draggableId+"drop item  "+droppableId)
    var item2ps=$("#"+draggableId).position()
    $("#"+droppableId).offset({top:item2ps.top+75,left:item2ps.left+140})
    console.log( $("#"+draggableId))
    $("#"+draggableId).css({"transform": "rotate(20deg)"});
    // console.log(item2ps)
    // console.log($("#"+draggableId).position())
}


//select item in the operation area
function selectItem(id){
    console.log(id);
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






