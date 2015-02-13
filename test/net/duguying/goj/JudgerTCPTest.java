package net.duguying.goj;

import junit.framework.TestCase;
import java.io.IOException;

public class JudgerTCPTest extends TestCase {
    private JudgerTCP j = null;
    private JudgerHTTP h = null;

//    public void testJudgerTCP() {
//        try {
//            this.j = new JudgerTCP("oj.duguying.net", 1004, "123456789");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // assert connect success
//        assertNotNull(this.j);
//
//        this.j = null;
//        System.gc();
//    }

    public void testJudgerHTTP(){
        this.h = new JudgerHTTP("127.0.0.1", 1005, "123456789");
    }

}