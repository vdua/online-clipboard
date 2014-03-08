package com.fw.oc.service.impl;

import com.fw.oc.common.ClipboardData;
import com.fw.oc.service.OnlineClipboardService;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.commons.json.JSONArray;
import javax.jcr.*;

import com.day.cq.commons.jcr.JcrConstants;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: vdua
 * Date: 27/2/14
 * Time: 1:17 PM
 */
@Component(label = "Online Clipboard Data Paste Service",
           description = "Service for Pasting Data on Online Clipboard",
           immediate = true,
           metatype = true)
@Service(OnlineClipboardService.class)
public class OnlineClipboardServiceImpl implements OnlineClipboardService {
    @Reference
    private SlingRepository slingRepository;

    private Node getOrCreatePath(Session session, Node node, String path) throws RepositoryException {
        String[] paths = path.split("/");
        Node currentNode = node;
        for(int i = 0;i<paths.length;i++) {
            boolean exists = currentNode.hasNode(paths[i]);
            if(!exists) {
                currentNode.addNode(paths[i],JcrConstants.NT_UNSTRUCTURED);
            }
            currentNode = currentNode.getNode(paths[i]);
        }
        return currentNode;
    }

    public void paste(ClipboardData data) throws Exception {
       if(slingRepository != null) {
           Session session = slingRepository.loginAdministrative(null);
           try {
               Node root = session.getRootNode();
               Node content = root.getNode("content");
               Node node = getOrCreatePath(session, content, "fw/oc/data");
               String nodeName = System.currentTimeMillis() + "";
               Node dataNode = node.addNode(nodeName, JcrConstants.NT_UNSTRUCTURED);
               dataNode.setProperty("data",data.getData());
               dataNode.setProperty("hostName",data.getHostName());
               dataNode.setProperty("ipAddress",data.getIpAddress());
               session.save();
           } catch (RepositoryException e) {
               throw e;
           }  finally {
               session.logout();
           }
       }
    }

    @Override
    public JSONArray getData() throws Exception {
        JSONArray res = new JSONArray();
        if(slingRepository != null) {
            Session session = slingRepository.loginAdministrative(null);
            try {
                Node root = session.getRootNode();
                Node content = root.getNode("content");
                Node node = getOrCreatePath(session, content, "fw/oc/data");
                NodeIterator iter = node.getNodes();
                while(iter.hasNext()) {
                    Node data = iter.nextNode();
                    JSONObject obj = new JSONObject();
                    obj.put("data", data.getProperty("data").getValue().toString());
                    obj.put("ipAddress", data.getProperty("ipAddress").getValue().toString());
                    obj.put("hostName", data.getProperty("hostName").getValue().toString());
                    obj.put("timestamp", data.getName());
                    res.put(obj);
                }
            } catch (RepositoryException e) {
                throw e;
            } finally {
                session.logout();
            }
        }
        return res;
    }
}