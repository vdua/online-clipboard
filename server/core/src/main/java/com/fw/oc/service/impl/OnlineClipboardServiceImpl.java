package com.fw.oc.service.impl;

import com.adobe.granite.xss.XSSAPI;
import com.fw.oc.common.ClipboardData;
import com.fw.oc.service.OnlineClipboardService;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.commons.json.JSONArray;

import javax.jcr.*;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.jcr.base.util.AccessControlUtil;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private XSSAPI xssapi;

    private Logger logger = LoggerFactory.getLogger(OnlineClipboardService.class);

    protected void activate(final ComponentContext bundle) {
        if (slingRepository != null) {
            ResourceResolver resourceResolver = null;
            Session session = null;
            try {
                session = slingRepository.loginAdministrative(null);
                resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
                UserManager um = resourceResolver.adaptTo(UserManager.class);
                Authorizable authorizable = um.getAuthorizable("anonymous");
                Node root = session.getRootNode();
                getOrCreatePath(session, session.getRootNode(), "content/fw");
                if (session.hasPendingChanges()) {
                    session.save();
                }
                AccessControlUtil.replaceAccessControlEntry(session, "/content/fw", authorizable.getPrincipal(),
                        new String[]{}, new String[]{"jcr:read"},
                        new String[]{}, null);
                if (session.hasPendingChanges()) {
                    session.save();
                }
            } catch (RepositoryException e) {
                logger.error("Bundle Activation Error" + e.getMessage());
            } catch (LoginException e) {
                logger.error("Bundle Activation Error" + e.getMessage());
            } finally {
                if (resourceResolver != null) {
                    resourceResolver.close();
                }
                if (session != null) {
                    session.logout();
                }
            }
        }
    }

    private Node getOrCreatePath(Session session, Node node, String path) throws RepositoryException {
        String[] paths = path.split("/");
        Node currentNode = node;
        for (int i = 0; i < paths.length; i++) {
            boolean exists = currentNode.hasNode(paths[i]);
            if (!exists) {
                currentNode.addNode(paths[i], JcrConstants.NT_UNSTRUCTURED);
            }
            currentNode = currentNode.getNode(paths[i]);
        }
        return currentNode;
    }

    public void paste(ClipboardData data) throws Exception {
        if (slingRepository != null) {
            Session session = slingRepository.loginAdministrative(null);
            try {
                Node root = session.getRootNode();
                Node content = root.getNode("content");
                Node node = getOrCreatePath(session, content, "fw/oc/data");
                String nodeName = System.currentTimeMillis() + "";
                Node dataNode = node.addNode(nodeName, JcrConstants.NT_UNSTRUCTURED);
                dataNode.setProperty("data", new String(data.getEncodedData(xssapi), "UTF-8"));
                dataNode.setProperty("hostName", data.getHostName());
                dataNode.setProperty("ipAddress", data.getIpAddress());
                session.save();
            } catch (RepositoryException e) {
                throw e;
            } finally {
                session.logout();
            }
        }
    }

    @Override
    public JSONArray getData() throws Exception {
        JSONArray res = new JSONArray();
        if (slingRepository != null) {
            Session session = slingRepository.loginAdministrative(null);
            try {
                Node root = session.getRootNode();
                Node content = root.getNode("content");
                Node node = getOrCreatePath(session, content, "fw/oc/data");
                NodeIterator iter = node.getNodes();
                while (iter.hasNext()) {
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