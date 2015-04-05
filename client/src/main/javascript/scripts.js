var oc = {},
    urlRegex = /https?:\/\/[^\/\s]+(?:\/[^\/\s]*)*/g;
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
                                    '<div class="text"></div>' +
                                    '<div class="urls"></div>' +
                                "</div>" +
                           "</div>",
        formatDate = function (timestamp) {
            var b = new Date(timestamp);
            return b.getDate() + "/" + b.getMonth() + "/" + b.getFullYear() + " " + b.getHours() + ":" +
                   b.getMinutes() + ":" + b.getSeconds();
        },
        wrapClipBoardData = function ($elem, data) {
            var htmlClipboardEl = $(dataHTMLTemplate).appendTo($elem),
                matchUrl = data.data.match(urlRegex),
                clipboardDataUrls = htmlClipboardEl.find(".clipboard-data .urls");
            htmlClipboardEl.find(".clipboard-data .text")
                .html(data.data)
                .end()
                .find(".data-user")
                .text(data.hostName + " (" + data.ipAddress + ") ")
                .end()
                .find(".timestamp")
                .text(formatDate(+data.timestamp));

            oc._.each(matchUrl, function (url, index) {
                $("<a></a>").attr("href", url).text("[1] " + url).appendTo(clipboardDataUrls);
            });
        },

        showData = function (data) {
            _.each(_.sortBy(data, function (item) { return -1 * +item.timestamp; }),
                function (item) {
                    wrapClipBoardData($("#clipboard-content"), item);
                });
        },
        submit = function (data) {
            if (data.length > 0) {
                $.ajax({
                    url: "/content/freeworks/paste.jsp",
                    method: "POST",
                    data: {"data": data},
                    async: false
                }).done(function () {
                    window.location.reload(true);
                });
            }
        };

    $(function () {
        var contentDiv = $("#clipboard-content"),
            data = contentDiv.data("oc-content");
        showData(data);
        $("button").click(function (evnt) {
            submit($("textarea").val());
        });
        $("textarea").keypress(function (evnt) {
            var key = evnt.key || evnt.charCode || evnt.keyCode;
            if (evnt.originalEvent.shiftKey === true && evnt.which === 13) {
                $(this).attr("disabled", "disabled");
                submit($("textarea").val());
                evnt.preventDefault();
            }
        });
    });
}(oc.$, oc._));