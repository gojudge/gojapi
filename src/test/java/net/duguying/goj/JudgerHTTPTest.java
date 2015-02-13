package net.duguying.goj;

import junit.framework.TestCase;

/**
 * Created by rex on 2015/1/26.
 */
public class JudgerHTTPTest extends TestCase {
	private JudgerHTTP h = null;
	
    protected void setUp() {
    }
	
	public void testJudgerHTTP(){
        this.h = new JudgerHTTP("oj.duguying.net", 1005, "123456789");
    }
}
