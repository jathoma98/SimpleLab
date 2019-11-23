var globalselect=undefined;

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

    $("#infobar").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();
    })

    $("#item_one").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();
    })

    $("#item_two").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();

    })



    $("#sidebar_lock").click(function (event) {
        $("#sidebar").toggleClass("sidebarhover");
    })

    // $('#sidebar_trigger').hover(function(){
    //         $("#sidebar").show("slide", { direction: "left" }, 500);
    // })
    //
    $(function(){
        $('#sidebar_trigger').hover(function(){
            $("#sidebar").show("slide", { direction: "left" }, 400);
        },function(){
            $("#sidebar").hide("slide", { direction: "left" }, 400);
        }).trigger('mouseleave');
    });


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






