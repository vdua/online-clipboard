package com.fw.oc.servlet;

import com.fw.oc.common.ClipboardData;
import com.fw.oc.service.OnlineClipboardService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(metatype = false)
@Service(Servlet.class)
@Properties({
        @Property(name = "sling.servlet.resourceTypes", value = "fw/oc/paste"),
        @Property(name = "sling.servlet.methods", value = "POST"),
        @Property(name = "service.description", value = "Online Clipboard Paste Data Servlet")
})
public class PasteServlet extends SlingAllMethodsServlet {

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    OnlineClipboardService onlineClipboard;

    private Logger logger = LoggerFactory.getLogger(PasteServlet.class);

    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response) throws ServletException {
        String data = request.getParameter("data");
        if(onlineClipboard != null) {
            try {
                ClipboardData pasteData = new ClipboardData(request.getRemoteAddr(), request.getRemoteHost(), data);
                onlineClipboard.paste(pasteData);
            } catch (Exception e) {
                try {
                    response.sendError(500);
                } catch (IOException e1) {
                    logger.error(e1.getMessage());
                }
            }
        }
    }
}