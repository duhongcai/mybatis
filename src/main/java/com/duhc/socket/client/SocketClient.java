package com.duhc.socket.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by duhc on 2017/8/1.
 */
public class SocketClient {
    public static final String IP = "127.0.0.1";
    public static final int PORT = 8969;

    public static void main(String[] args) {
        handler();
    }

    private static void handler() {
        try {
            Socket client = new Socket(IP, PORT);
            new Thread(new ReadHandler(client)).start();
            new Thread(new WriteHandler(client)).start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}

/**
 * 读操作
 */
class ReadHandler implements Runnable {
    private Socket client;

    public ReadHandler(Socket client) {
        this.client = client;
    }

    public void run() {
        DataInputStream dis = null;
        try {
            while (true) {
                dis = new DataInputStream(client.getInputStream());
                String receiver = dis.readUTF();
                System.out.println("服务端返回的数据是：" + receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 写进程
 */
class WriteHandler implements Runnable {
    private Socket client;

    public WriteHandler(Socket client) {
        this.client = client;
    }

    public void run() {
        DataOutputStream dos = null;
        BufferedReader br = null;
        try {
            while (true) {
                dos = new DataOutputStream(client.getOutputStream());
                System.out.println("请输入：\t");
                //键盘录入
                br = new BufferedReader(new InputStreamReader(System.in));
                String send = br.readLine();
                dos.writeUTF(send);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


