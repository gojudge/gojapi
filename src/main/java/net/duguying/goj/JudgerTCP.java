package net.duguying.goj;

import java.io.*;
import java.net.Socket;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rex on 2015/1/25.
 */
public class JudgerTCP {
    private Socket conn;
    private String host;
    private boolean login = false;
    private String judger_os;
    private int port;
    private char mark = '#';

    public JudgerTCP(String host, int port, String password) throws IOException {
        this.host = host;
        this.port = port;

        // create connection
        this.conn = new Socket(this.host, this.port);

        // read out the response message from server
        String header = this.Read();
        System.out.println(header);
        int idx = header.indexOf(this.mark);
        if (idx > 0){
            idx = idx - 1;
        }else{
            idx = 0;
        }
        this.mark = header.charAt(idx);

        // prepare login data
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("action", "login");
        map.put("password", password);

        // send login
        try {
            Map resp = this.Request(map);
            boolean result = (Boolean)resp.get("result");
            this.login = result;

            if (!result){
                return;
            }

            this.judger_os = resp.get("os").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("send request error!");
        }
    }

    /**
     * send request to server
     * @param requst request content, a map struct
     * @return response content, a map struct
     * @throws IOException net exception
     * @throws JSONException json parse exception
     */
    public Map Request(Map<String,Object> requst) throws IOException, JSONException {
        String content = this.MsgPack(requst);
        DataOutputStream dOut = new DataOutputStream(this.conn.getOutputStream());
        dOut.write(content.getBytes());
        dOut.flush();
        String resp = this.Read();

        return toMap(new JSONObject(resp.toString()));
    }

    // reading from server
    private String Read() throws IOException {
        Reader reader = new InputStreamReader(this.conn.getInputStream());
        char chars[] = new char[10];
        int len;
        StringBuilder sb = new StringBuilder();
        String temp;

        while (true) {
            len = reader.read(chars);
            temp = new String(chars, 0, len);
            sb.append(temp);

            int index = temp.indexOf(this.mark);
            if (index != -1) {//遇到mark时就结束接收
                break;
            }

        }

        return sb.toString();
    }

    // json to map
    private static Map toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    // json to list
    private static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    // pack message
    private String MsgPack(Map<String,Object> map){
        JSONObject json = new JSONObject(map);
        return json.toString() + this.mark;
    }

    // encode html
    private String HtmlEncode(String html){
        StringBuffer out = new StringBuffer();
        for(int i = 0; i < html.length(); i++){
            char c = html.charAt(i);

            if (c > 127 || c == '"' || c == '<' || c == '>') {
                out.append("&#" + (int)c + ";");
            }else {
                out.append(c);
            }
        }

        return out.toString();
    }

    /**
     * 添加任务
     * @param id 任务全局ID
     * @param sid 连接标识
     * @param language 语言C/C++
     * @param code 代码
     * @return 返回信息
     */
    public Map AddTask(int id, String sid, String language, String code) throws IOException, JSONException {
        if (!this.login){
            return null;
        }

        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("action", "task_add");
        obj.put("id", id);
        obj.put("sid", sid);
        obj.put("language", language);

        code = this.HtmlEncode(code);

        obj.put("code", code);

        return this.Request(obj);
    }

    /**
     * 获取任务
     * @param id 任务id
     * @param sid 连接标识
     * @return 返回信息
     * @throws IOException
     * @throws JSONException
     */
    public Map GetStatus(int id, String sid) throws IOException, JSONException {
        if (!this.login){
            return null;
        }

        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("action", "task_info");
        obj.put("id", id);
        obj.put("sid", sid);

        return this.Request(obj);
    }

    protected void finalize(){
    }
}
