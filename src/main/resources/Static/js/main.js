$(document).ready(function () {

    $("#add-lecture").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_submit();

    });

});

function fire_ajax_submit() {

    var lectureBody = {};
    lectureBody["universityId"] = $("#universityId").val();
    lectureBody["date"] = $("#date").val();
    lectureBody["time"]  = $("#time").val();
    lectureBody["teacher"] = $("#teacher").val();
    lectureBody["groupName"] = $("#groupName").val();
    lectureBody["classroom"] = $("#classroom").val();
    lectureBody["subject"] = $("#subject").val();

    $("#submitLecture").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/put/lecture",
        data: JSON.stringify(lectureBody),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#submitLecture").prop("disabled", false);
            alert("Json post successful:  " + JSON.stringify(data));
            window.location.reload(true);
        },
        error: function (e) {
            $("#submitLecture").prop("disabled", false);
            alert("This answer is passed by error function\n " +  e.responseText);
            window.location.reload(true);
        }
    });

}
