package com.java.model;

public class TCP {

    // 接收端的Ip地址，也就是电脑的ip地址
    private static String IP = "192.168.124.4";
    public static int PORT = 6666;

    public static String getTcp() {
        String tcp = "";
        if (IP != null && IP.length() > 0) {
            tcp = "tcp://" + IP + ":" + PORT;
        }
        return tcp;
    }
}
