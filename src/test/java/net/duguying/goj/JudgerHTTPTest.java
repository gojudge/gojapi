package net.duguying.goj;

import junit.framework.TestCase;

import java.util.Map;

/**
 * Created by rex on 2015/1/26.
 */
public class JudgerHTTPTest extends TestCase {
	private JudgerHTTP h = null;
	
    protected void setUp() {
    }
	
	public void testJudgerHTTP(){
        this.h = new JudgerHTTP("oj.duguying.net", 1005, "123456789");

        Map<String, Object> response = this.h.AddTask(12,"randomstring","C","int main(){return 0;}");
        boolean rst = (Boolean)response.get("result");
        assertTrue(rst);

        Map<String, Object> response2 = this.h.GetStatus(12, "randomstring");
        int rst2 = (Integer)response2.get("id");
        assertEquals(12, rst2);
    }
}
