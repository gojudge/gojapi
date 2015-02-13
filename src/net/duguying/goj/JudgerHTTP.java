package net.duguying.goj;

import java.io.IOException;
import java.util.*;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rex on 2015/1/26.
 */
public class JudgerHTTP {
    private String host = "";
    private int port = 80;
    private String url = "";
    private String password = "";
    private boolean login = false;

    public JudgerHTTP(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;

        this.url = "http://" + host + ":" + port;

        this.login = this.Login(this.password);

    }

    /**
     * post method for http
     *
     * @param content
     * @return
     */
    private String Post(String content) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpUriRequest login = RequestBuilder.post().setUri(this.url)
                .setEntity(EntityBuilder.create().setText(content).build()).build();
        CloseableHttpResponse response = httpclient.execute(login);
        HttpEntity entity = response.getEntity();

        String result = EntityUtils.toString(entity);
        httpclient.close();

        return result;
    }

    /**
     * login
     *
     * @param password the password
     * @return
     */
    private boolean Login(String password) {
        Map<String, Object> reqObj = new HashMap<String, Object>();
        reqObj.put("action", "login");
        reqObj.put("password", this.password);

        Map respObj;
        try {
            respObj = this.Request(reqObj);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return (Boolean) respObj.get("result");
    }

    /**
     * pack the message
     *
     * @param map the k-v message
     * @return
     */
    public String MsgPack(Map<String, Object> map) {
        JSONObject json = new JSONObject(map);
        return json.toString();
    }

    /**
     * send a request
     *
     * @param map the k-v content
     * @return the k-v response
     */
    public Map<String, Object> Request(Map<String, Object> map) throws IOException, JSONException {
        String req = this.MsgPack(map);
        String resp = this.Post(req);

        System.out.println(resp);
        JSONObject json = new JSONObject(resp);
        return this.toMap(json);
    }

    /**
     * add task
     *
     * @param obj the k-v content
     * @return the k-v response
     */
    public Map<String, Object> AddTask(Map<String, Object> obj) {
        return null;
    }

    /**
     * get task status and result
     *
     * @param obj the k-v request content
     * @return the k-v response
     */
    public Map<String, Object> GetStatus(Map<String, Object> obj) {
        return null;
    }

    // json to map
    private static Map toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    // json to list
    private static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
