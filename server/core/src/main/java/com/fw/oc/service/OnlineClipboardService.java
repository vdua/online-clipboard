package com.fw.oc.service;
import com.fw.oc.common.ClipboardData;
import org.apache.sling.commons.json.JSONArray;

/**
 * Created with IntelliJ IDEA.
 * User: vdua
 * Date: 27/2/14
 * Time: 1:17 PM
 */
public interface OnlineClipboardService {
    public void paste(ClipboardData data) throws Exception;

    public JSONArray getData() throws Exception;
}
