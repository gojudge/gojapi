package net.duguying.goj;

import junit.framework.TestCase;

/**
 * Created by rex on 2015/1/26.
 */
public class JudgerHTTPTest extends TestCase {
    protected void setUp() {
        System.out.println("setUp");
    }

    public void testJudgerHTTP() {
        JudgerHTTP j = new JudgerHTTP();
        j = null;
        System.out.println("HTTP");
        System.gc();
    }
}
