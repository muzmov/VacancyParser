function setHandler() {
    $("th > a").on("click", function (event) {
        event.preventDefault();
        $.ajax({
            type: 'get',
            url: 'search.do' + $(this).attr("href"),
            success: function (data) {
                console.log(data);
                $("#results").html(data);
            }
        });
    })
}

$(function () {
    $("#nameform").on("submit", function (event) {
        event.preventDefault();
        $.ajax({
            url: $(this).attr("action"),
            data: $("#nameform").serialize(),
            type: 'get',
            success: function (data) {
                $("#results").html(data);
            }
        });
    });
});
