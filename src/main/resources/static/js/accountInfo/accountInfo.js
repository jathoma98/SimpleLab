let ACC_INFO ={
    init(){
        this.editInfo = function(){
            console.log("editInfoBtn is pressed");
            $("#first_name").prop("readonly", false);
            $("#last_name").prop("readonly", false);
            $("#email").prop("readonly", false);
            $("#institution").prop("readonly", false);
            $("#accPassword").prop("readonly", false);
            $("#editInfoBtn").toggle();
            $("#saveInfoBtn").toggle();
            $("#cancelBtn").toggle();
        }
        this.saveInfo = function(){
            ACC_INFO.editUserInfo();
            $("#first_name").prop("readonly", true);
            $("#last_name").prop("readonly", true);
            $("#email").prop("readonly", true);
            $("#institution").prop("readonly", true);
            $("#accPassword").prop("readonly", true);
            $("#editInfoBtn").toggle();
            $("#saveInfoBtn").toggle();
            $("#cancelBtn").toggle();
        }

        this.cancelEdit = function(){
            $("#first_name").prop("readonly", true);
            $("#last_name").prop("readonly", true);
            $("#email").prop("readonly", true);
            $("#institution").prop("readonly", true);
            $("#accPassword").prop("readonly", true);
            $("#editInfoBtn").toggle();
            $("#saveInfoBtn").toggle();
            $("#cancelBtn").toggle();
        }

        this.addtag = function(){
            tempvalue=$("#tag_input").val();
            var temp = $('<div class="chip">'+tempvalue+'' +
                '<i class="close material-icons">close</i>' +
                '</div>');
            $("#tags_field").append(temp);
        }

        this.btnEvents = new Array();
        this.btnEvents["#editInfoBtn"] = ACC_INFO.editInfo;
        this.btnEvents["#saveInfoBtn"] = ACC_INFO.saveInfo;
        this.btnEvents["#cancelBtn"] = ACC_INFO.cancelEdit;
        this.btnEvents["#add_tag"] = ACC_INFO.addtag;

        this.showUserInfo = function() {
            $.ajax({
                url: "/user/rest/loadUserInfo",
                type: "GET",
                success: function(result){
                    retObjHandle(result, (user)=>{
                        let data = {
                            accModal:{
                                active: "active",
                                user: user,
                            }
                        }
                        rebuildComponent('#modalAccount ul',"#modalAccTpl", data,"click", ACC_INFO.btnEvents);
                    })
                }
            })
        }
        this.editUserInfo = function (){
            $.ajax({
                url: "/user/rest/loadUserInfo",
                type: "GET",
                success: function(user){
                    user.data.firstname = $("#first_name").val();
                    user.data.lastname = $("#last_name").val();
                    user.data.institution = $("#institution").val();
                    user.data.email = $("#email").val();
                    user.data.password = $("#accPassword").val();
                    let user_json =  JSON.stringify(user.data);
                    $.ajax({
                        url: "/user/rest/restUserInfo",
                        type: "POST",
                        contentType: 'application/json; charset=utf-8',
                        data: user_json,
                        success:function (){
                            alert("You have saved your information!")
                        }
                    })
                }
            })

        }
    }
}

$(document).ready( function () {
    ACC_INFO.init();
    $(".accountDia").on("click", ()=>{ACC_INFO.showUserInfo()});

});


