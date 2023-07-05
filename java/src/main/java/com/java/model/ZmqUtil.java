package com.java.model;


import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ZmqUtil {

    private static final String encoding = "UTF-8";
    private static ZMQ.Context mContext = null;
    private static ZMQ.Socket mZmqSocket = null;
    private static ServerSocket mServerSocket = null;
    private static PrintStream mPrintStream;
    private static Socket mSocket;
    private static int number = 0;

    public static void main(String[] args) {
        initServiceSocket();

        final String tcp = TCP.getTcp();
        if (tcp.length() == 0) {
            System.out.println("tcp is empty！");
        }
        System.out.println("tcp is : " + tcp);

        if (tcp.length() > 0) {
            try {
                mContext = ZMQ.context(1);
                System.out.println("zmq --- context create success ! ");
            } catch (Exception e) {
                System.out.println("zmq --- context create error: " + e.getMessage());
            }

            try {
                if (mContext != null) {
                    mZmqSocket = mContext.socket(SocketType.PAIR);
                    System.out.println("zmq --- socket create success ! ");
                }
            } catch (Exception e) {
                System.out.println("zmq --- socket create error: " + e.getMessage());
            }

            try {
                if (mZmqSocket != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean bind = mZmqSocket.bind(tcp);
                            System.out.println("bind is success: " + bind);

                            if (mContext != null) {
                                System.out.println("start receiver data ...");
                                while (!mContext.isTerminated()) {
                                    byte[] recv = mZmqSocket.recv(0);
                                    if (recv != null) {
                                        try {
                                            String content = new String(recv, "UTF-8");
                                            sendSocket(content);
                                        } catch (UnsupportedEncodingException e) {
                                            System.out.println("convert error : " + e.getMessage());
                                        }
                                    }
                                }
                            }
                        }
                    }).start();
                }
            } catch (Exception e) {
                System.out.println("recv is error: " + e.getMessage());
            }
        }
    }

    private static void initServiceSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mServerSocket = new ServerSocket(9998);
                    while (true) {
                        // 等待客户端连接，阻塞线程
                        System.out.println("开始阻塞线程，等待客户端链接 ~~~~~~~！");
                        mSocket = mServerSocket.accept();
                        InetAddress inetAddress = mSocket.getInetAddress();
                        System.out.println("客户端连接成功：主机地址：" + inetAddress.getHostAddress() + "\r\n" + "主机名字：" + inetAddress.getHostName());
                    }
                } catch (Exception e) {
                    System.out.println("recv is error: " + e.getMessage());
                }
            }
        }).start();
    }

    private static void sendSocket(String content) {
        content = "number: [" + number + " ]" + content;

        if (content != null) {
            System.out.println("receiver is : " + content);
            // 发送数据
            if (mSocket == null) {
                System.out.println("Socket为空！");
            } else {
                boolean connected = mSocket.isConnected();
                if (connected) {
                    try {
                        if (mPrintStream == null) {
                            mPrintStream = new PrintStream(mSocket.getOutputStream(), true, encoding);
                        }
                        mPrintStream.println(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (mPrintStream != null) {
                            mPrintStream.close();
                            mPrintStream = null;
                        }
                        System.out.println("Socket发送数据异常：" + e.getMessage());
                    }
                } else {
                    System.out.println("Socket断开中！！！");
                }
            }
        }
    }
}
