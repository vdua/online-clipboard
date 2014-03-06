package com.fw.oc.common;

/**
 * Created with IntelliJ IDEA.
 * User: vdua
 * Date: 6/3/14
 * Time: 9:18 AM
 */
public class ClipboardData {

    private String ipAddress;
    private String hostName;
    private String data;

    public ClipboardData(String ip, String host, String data) {
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

    public String getData() {
        return data;
    }
}
