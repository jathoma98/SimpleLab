var globalselect=undefined;

$(document).ready(function(){
    $(".draggable_item").draggable({
        containment: "parent"
    });
    $( ".draggable_item" ).droppable({
        drop: Drop
    });

    $("#item_one").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();

    })
    $("#item_two").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();

    })
    $("#operation_area").click(function (event) {
        selectItem(this.id);
        event.stopPropagation();
    })

});

// $(document).click(function(event) {
//     var id = event.target.id);
//     console.log(id);
// });

function Drop(event, ui) {
    $( this )
        .addClass( "ui-state-highlight" )
        .find( "p" )
        .html( "Dropped!" );
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
        $("#infobar").hide();
        globalselect=undefined;
    }else if(globalselect===undefined){
        globalselect=id;
        var element = document.getElementById(id);
        element.classList.add("selected");
        $("#infobar").show();
        loadInfo(id);
    }else if(globalselect==id){

    }else{
        var unselect = document.getElementById(globalselect);
        unselect.classList.remove("selected")
        var element = document.getElementById(id);
        element.classList.add("selected");
        globalselect=id;
        loadInfo(id);
    }
}


function loadInfo(id){
    var element = document.getElementById(id);
    //item info can be write here
    

}






