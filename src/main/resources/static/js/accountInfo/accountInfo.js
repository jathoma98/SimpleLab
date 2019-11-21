let ACC_INFO ={
    init(){
        this.editInfo = function(){
            console.log("editInfoBtn is pressed");
            $("#first_name").prop("readonly", false);
            $("#last_name").prop("readonly", false);
            $("#email").prop("readonly", false);
            $("#institution").prop("readonly", false);
            $("#password").prop("readonly", false);
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
            $("#password").prop("readonly", true);
            $("#editInfoBtn").toggle();
            $("#saveInfoBtn").toggle();
            $("#cancelBtn").toggle();
        }

        this.cancelEdit = function(){
            $("#first_name").prop("readonly", true);
            $("#last_name").prop("readonly", true);
            $("#email").prop("readonly", true);
            $("#institution").prop("readonly", true);
            $("#password").prop("readonly", true);
            $("#editInfoBtn").toggle();
            $("#saveInfoBtn").toggle();
            $("#cancelBtn").toggle();
        }

        this.btnEvents = new Array();
        this.btnEvents["#editInfoBtn"] = ACC_INFO.editInfo;
        this.btnEvents["#saveInfoBtn"] = ACC_INFO.saveInfo;
        this.btnEvents["#cancelBtn"] = ACC_INFO.cancelEdit;

        this.showUserInfo = function() {
            $.ajax({
                url: "/user/rest/loadUserInfo",
                type: "GET",
                success: function(user){
                    let data = {
                        accModal:{
                            active: "active",
                            user: user,
                        }
                    }
                    rebuildComponent('#modalAccount ul',"#modalAccTpl", data,ACC_INFO.btnEvents);
                }
            })
        }
        this.editUserInfo = function (){
            $.ajax({
                url: "/user/rest/loadUserInfo",
                type: "GET",
                success: function(user){
                    user.firstname = $("#first_name").val();
                    user.lastname = $("#last_name").val();
                    user.institution = $("#institution").val();
                    user.email = $("#email").val();
                    let course_json =  JSON.stringify(user);
                    $.ajax({
                        url: "/user/rest/restUserInfo",
                        type: "POST",
                        data: course_json,
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
