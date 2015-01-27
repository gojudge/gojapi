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
    private int port;

    public JudgerTCP(String host, int port, String password) throws IOException {
        this.host = host;
        this.port = port;

        // create connection
        this.conn = new Socket(this.host, this.port);

        // read out the response message from server
        String msg = this.read();
        System.out.println(msg);

        // prepare login data
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("action", "login");
        map.put("password", password);

        // send login
        try {
            Map resp = this.Request(map);
            String os = resp.get("os").toString();
            System.out.println("os: "+os);
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
        String content = this.msgPack(requst);
        DataOutputStream dOut = new DataOutputStream(this.conn.getOutputStream());
        dOut.write(content.getBytes());
        dOut.flush();
        String resp = this.read();

        return toMap(new JSONObject(resp.toString()));
    }

    // reading from server
    private String read() throws IOException {
        Reader reader = new InputStreamReader(this.conn.getInputStream());
        char chars[] = new char[10240];
        int len;
        StringBuilder sb = new StringBuilder();
        String temp;
        len=reader.read(chars);
        temp = new String(chars, 0, len);
        int index = temp.indexOf("eof");
        if (index != -1) {//遇到eof时就结束接收
            return "";
        }
        sb.append(temp);

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
    private String msgPack(Map<String,Object> map){
        JSONObject json = new JSONObject(map);
        return json.toString()+"\003";
    }

    protected void finalize(){
    }
}
