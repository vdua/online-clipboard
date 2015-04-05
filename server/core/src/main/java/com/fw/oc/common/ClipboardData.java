package com.fw.oc.common;

import org.apache.commons.lang3.StringUtils;
import com.adobe.granite.xss.XSSAPI;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: vdua
 * Date: 6/3/14
 * Time: 9:18 AM
 */
public class ClipboardData {

    private String ipAddress;
    private String hostName;
    private byte[] data;

    public ClipboardData(String ip, String host, byte[] data) {
        ipAddress = ip;
        hostName = host;
        this.data = data;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public byte[] getData() {
        try {
            String originalString = new String(data, "UTF-8");
            String dataString = StringUtils.replace(originalString, "\n", "<br />");
            return dataString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getEncodedData(XSSAPI xssapi) {
        try {
            String originalString = new String(data, "UTF-8");
            String encodedString = xssapi.encodeForHTML(originalString);
            String dataString = StringUtils.replace(encodedString, "\n", "<br />");
            return dataString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
