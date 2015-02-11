package net.duguying.goj;

import java.util.Map;

/**
 * Created by rex on 2015/1/26.
 */
public class JudgerHTTP {
    private String host = "";
    private int port = 80;
    private String url = "";
    private String password = "";

    public JudgerHTTP(String host, int port, String password){
        this.host = host;
        this.port = port;
        this.password = password;

        this.url = "http://" + host + ":" + port;

    }

    /**
     * post method for http
     * @param content
     * @return
     */
    private String Post(String content){
        return "";
    }

    /**
     * login
     * @param password the password
     * @return
     */
    private boolean Login(String password){
        return false;
    }

    /**
     * pack the message
     * @param msg the k-v message
     * @return
     */
    public Map<String, Object> MsgPack(Map<String, Object> msg){
        return null;
    }

    /**
     * send a request
     * @param obj the k-v content
     * @return the k-v response
     */
    public Map<String, Object> Request(Map<String, Object> obj){
        return null;
    }

    /**
     * add task
     * @param obj the k-v content
     * @return the k-v response
     */
    public Map<String, Object> AddTask(Map<String, Object> obj){
        return null;
    }

    /**
     * get task status and result
     * @param obj the k-v request content
     * @return the k-v response
     */
    public Map<String, Object> GetStatus(Map<String, Object> obj){
        return null;
    }
}
