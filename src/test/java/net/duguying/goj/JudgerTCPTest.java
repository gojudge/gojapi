package net.duguying.goj;

import junit.framework.TestCase;
import java.io.IOException;
import java.util.Map;

public class JudgerTCPTest extends TestCase {
    private JudgerTCP j = null;

    public void testJudgerTCP() {
        try {
            this.j = new JudgerTCP("oj.duguying.net", 1004, "123456789");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // assert connect success
        assertNotNull(this.j);

        Map<String, Object> response = null;
        try {
            response = this.j.AddTask(12,"randomstring","C","int main(){return 0;}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean rst = (Boolean)response.get("result");

        // assert add task
        assertTrue(rst);

        Map<String, Object> response2 = null;
        try {
            response2 = this.j.GetStatus(12,"randomstring");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int rst2 = (Integer)response2.get("id");

        // assert get status
        assertEquals(12, rst2);

        this.j = null;
        System.gc();
    }



}