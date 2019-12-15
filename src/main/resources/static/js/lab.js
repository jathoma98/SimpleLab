var globalselect=undefined;
var sidebar_step_select=undefined;
var itemcount=0;
var current_drag=undefined;
var current_drop=undefined;
var stepcount=1;

$(document).ready(function(){

    // add item to operation area when click sidebar item
    $(".sidebaritem").click(function (event) {
        var item_id="item_"+itemcount;

        if(this.id=="500mlbeaker"){
            var c = $('<div id='+item_id+' class="ui-widget-content fixed draggable_item center '+item_id+'">' +
                '<img src="../../img/beaker.png" class="item_img">' +
                '<p >500ml beaker</p>' +
                '</div>');

            var cur_item= $('<li id='+item_id+' class="collection-item">\n' +
                '                        <div class="row sidebaritem">\n' +
                '                            <div class="col s6">\n' +
                '                                <img src="../../img/beaker.png" class="item_img">\n' +
                '                            </div>\n' +
                '                            <div class="col s6">\n' +
                '                                <p>500ml beaker</p>\n' +
                '                                <p>0.01L</p>\n' +
                '                                <a class="right cur_item_remove" href="#">Remove</a>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                    </li>');
        }else{
            var c = $('<div id='+item_id+' class="ui-widget-content fixed draggable_item center '+item_id+'">' +
                '<img src="../../img/cylinder.png" class="item_img">' +
                '<p >200ml cylinder</p>' +
                '</div>');

            var cur_item= $('<li id='+item_id+'  class="collection-item">\n' +
                '                        <div class="row sidebaritem">\n' +
                '                            <div class="col s6">\n' +
                '                                <img src="../../img/cylinder.png" class="item_img">\n' +
                '                            </div>\n' +
                '                            <div class="col s6">\n' +
                '                                <p>200ml cylinder</p>\n' +
                '                                <p>0.01L</p>\n' +
                '                                <a class="right cur_item_remove" href="#">Remove</a>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                    </li>');
        }


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
        //add to operation area
        $("#operation_area").append(c);
        //add item to current set
        $(".current_item_set").append(cur_item);
        c.offset({top:300,left:500})
        itemcount++;

        //for popup dialog rotate back
        $(".popup_dialog").modal({
            onCloseEnd: function (event) {
                $("."+current_drag).css({"transform": "rotate(0deg)"});
            }
        })
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

    //add step
    $('#additemset').click(function() {
        $('.itemset').append('' +
            '<li id="step'+stepcount+'" class="collection-item ui-widget-content sidebar_selectable_item">\n' +
            '     Step'+stepcount+'\n' +
            '     <a class="right step_remove" href="#">Remove</a>\n' +
            '</li>');
        stepcount++;
    });

    //remove current item
    $('.collection').on('click', '.cur_item_remove', function() {
        var temp=$(this).closest('li')["0"].id;
        $("."+temp).remove();
        $(this).closest('li').remove();


    });

    //add step
    $('#addstep').click(function() {
        $('.sidebar_step_collection').append('' +
            '<li id="step'+stepcount+'" class="collection-item ui-widget-content sidebar_selectable_item">\n' +
            '     Step'+stepcount+'\n' +
            '     <a class="right step_remove" href="#">Remove</a>\n' +
            '</li>');
        stepcount++;
    });

    //remove step
    $('.collection').on('click', '.step_remove', function() {
        $(this).closest('li').remove();
    });

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












});
//--------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
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
    current_drop=droppableId;
    // console.log("drag item  " +draggableId+"drop item  "+droppableId)
    var item2ps=$("."+draggableId).position()
    console.log(item2ps)
    $("."+droppableId).offset({top:item2ps.top+50,left:item2ps.left+100})
    console.log( $("#"+draggableId))
    $("."+draggableId).css({"transform": "rotate(45deg)"});
}


//select item in the operation area
function selectItem(id){
    console.log(id);
    if(id=="operation_area"){
        if(!(globalselect==undefined)){
            $("."+globalselect).removeClass("selected")
        }
        $("#infobar").hide("slide", { direction: "right" }, 400);
        globalselect=undefined;
    }else if(id=="sidebar"){

    }else if(id=="infobar"){

    }else if(globalselect===undefined){
        globalselect=id;
        $("."+id).addClass("selected")
        loadInfo(id);
        $("#infobar").show("slide", { direction: "right" }, 400);

    }else if(globalselect==id){

    }else{
        $("."+globalselect).removeClass("selected")
        $("."+id).addClass("selected")
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






