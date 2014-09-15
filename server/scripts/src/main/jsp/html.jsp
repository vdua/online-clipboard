<%@include file="/libs/foundation/global.jsp"%>
<%@ page session="false"
         contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8"
         import="com.fw.oc.service.OnlineClipboardService,
                 org.apache.sling.commons.json.JSONArray,
                 org.apache.sling.commons.json.JSONObject,
                 org.apache.commons.lang.StringEscapeUtils"%>
<%
    OnlineClipboardService oc = sling.getService(OnlineClipboardService.class);
    JSONArray data = oc.getData();
%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <cq:includeClientLib categories="freeworks.onlineclipboard" />
</head>
<body>
<div class="header jumbotron">
    <div class="container">
        <h1> Online Clipboard</h1>
        <p> Copy from your clipboard and share it with others </p>
    </div>
</div>
<div class="page container">
    <div class="row">
        <div class="form-group col-md-12">
            <textarea name="textarea" class="form-control" placeholder="Paste your text here" rows="5"></textarea>
        </div>
    </div>
    <div class="row">
        <div class="form-group col-md-1 col-md-offset-11">
            <button class="btn-submit btn btn-primary">Submit</button>
        </div>
    </div>
    <div id="clipboard-content" data-oc-content="<%=StringEscapeUtils.escapeXml(data.toString())%>">
    </div>
</div>
</body>
</html>