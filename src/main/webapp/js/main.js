$(function () {
    $("#nameform").on("submit", function (event) {
        event.preventDefault();
        $.ajax({
            url: $(this).attr("action"),
            data: $("#nameform").serialize(),
            type: 'post',
            success: function (data) {
                var timerId = setInterval(function () {
                    $.ajax({
                        url: 'progress.do?attr=ParsingProgress',
                        success: function (data) {
                            try {
                                data = $.parseJSON(data);
                            } catch(e) {
                                clearInterval(timerId);
                            }
                            if (data.done) {
                                clearInterval(timerId);
                            }
                            if (data.status === 1) {
                                $("#parsingProgress").html(data.statusText);
                            }
                        }
                    });
                }, 1000);
            }
        });
    });
    $("#parsereviewsform").on("submit", function (event) {
        event.preventDefault();
        $.ajax({
            url: $(this).attr("action"),
            data: $("#parsereviewsform").serialize(),
            type: 'post',
            success: function (data) {
                var timerId = setInterval(function () {
                    $.ajax({
                        url: 'progress.do?attr=ReviewsProgress',
                        success: function (data) {
                            try {
                                data = $.parseJSON(data);
                            } catch(e) {
                                clearInterval(timerId);
                            }
                            if (data.done) {
                                clearInterval(timerId);
                            }
                            if (data.status === 1) {
                                $("#rewiewsProgress").html(data.statusText);
                            }
                        }
                    });
                }, 1000);
            }
        });
    });
    $("#proxyform").on("submit", function (event) {
        event.preventDefault();
        $.ajax({
            url: $(this).attr("action"),
            data: $("#proxyform").serialize(),
            type: 'post',
            success: function (data) {
                var timerId = setInterval(function () {
                    $.ajax({
                        url: 'progress.do?attr=ParsingProgress',
                        success: function (data) {
                            try {
                                data = $.parseJSON(data);
                            } catch(e) {
                                clearInterval(timerId);
                            }
                            if (data.done) {
                                clearInterval(timerId);
                            }
                            if (data.status === 1) {
                                $("#proxyProgress").html(data.statusText);
                            }
                        }
                    });
                }, 1000);
            }
        });
    });
});
