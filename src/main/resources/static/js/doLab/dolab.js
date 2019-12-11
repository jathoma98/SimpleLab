$(document).ready(function () {
    this.loadSideBar();


}



this.loadSideBar = function () {
    $.ajax({
        url: "/student/doLab/loadCourseList",
        type: "GET",
        success: function (result) {


                }
            })
        }
    })
};