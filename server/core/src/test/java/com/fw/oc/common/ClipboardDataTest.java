package com.fw.oc.common;

import com.fw.oc.common.ClipboardData;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * ClipboardData Tester.
 *
 * @author vdua
 * @version 1.0
 * @since <pre>May 26, 2014</pre>
 */
public class ClipboardDataTest {

    ClipboardData cd;

    @Before
    public void before() throws Exception {
        cd = new ClipboardData("10.10.10.10", "myhost", "test data".getBytes());
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getIpAddress()
     */
    @Test
    public void testGetIpAddress() throws Exception {
        Assert.assertEquals("IP Address == 10.10.10.10", cd.getIpAddress(),"10.10.10.10");
    }

    /**
     * Method: getHostName()
     */
    @Test
    public void testGetHostName() throws Exception {
        Assert.assertEquals("HostName == myhost", cd.getHostName(),"myhost");
    }

    /**
     * Method: getData()
     */
    @Test
    public void testGetData() throws Exception {
        Assert.assertEquals("bytes should match the input bytes", new String(cd.getData()),"test data");
    }
} 
