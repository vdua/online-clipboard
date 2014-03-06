var oc = {};
oc.$ = jQuery.noConflict();
oc._ = _.noConflict();
(function ($, _) {
    var dataHTMLTemplate = "<div class=\"clipboard-data-wrapper panel panel-primary\">" +
                                "<div class = \"panel-heading clearfix\">" +
                                    "<div class=\"data-user pull-left\">" +
                                    "</div>" +
                                    "<div class=\"timestamp pull-right\">" +
                                    "</div>" +
                                "</div>" +
                                "<div class=\"clipboard-data panel-body\">" +
                                "</div>" +
                           "</div>",
        formatDate = function (timestamp) {
            var b = new Date(timestamp);
            return b.getDate() + "/" + b.getMonth() + "/" + b.getFullYear() + " " + b.getHours() + ":" +
                   b.getMinutes() + ":" + b.getSeconds();
        },
        wrapClipBoardData = function ($elem, data) {
            $(dataHTMLTemplate).appendTo($elem)
                               .find(".clipboard-data")
                               .text(data.data)
                               .end()
                               .find(".data-user")
                               .text(data.hostName + " (" + data.ipAddress + ") ")
                                .end()
                                .find(".timestamp")
                                .text(formatDate(+data.timestamp));
        },

        showData = function (data) {
            _.each(_.sortBy(data, function (item) { return -1 * +item.timestamp; }),
                function (item) {
                    wrapClipBoardData($("#clipboard-content"), item);
                });
        };

    $(function () {
        var contentDiv = $("#clipboard-content"),
            data = contentDiv.data("oc-content");
        showData(data);
        $("button").click(function () {
            var data = $("textarea").val();
            if (data.length > 0) {
                $.ajax({
                    url: "/content/freeworks/paste.jsp",
                    method: "POST",
                    data: {"data" : data}
                }).done(function () {
                    window.alert('done');
                });
            }
        });
    });
}(oc.$, oc._));