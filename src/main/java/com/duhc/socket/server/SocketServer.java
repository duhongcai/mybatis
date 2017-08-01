package com.duhc.socket.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by duhc on 2017/8/1.
 */
public class SocketServer {
    public static final int PORT = 8969;

    public static void main(String args[]) throws IOException {
        SocketServer server = new SocketServer();
        server.init();
    }

    private void init() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket client = serverSocket.accept();
                new Thread(new ReadHandlerThread(client)).start();
                new Thread(new WriteHandlerThread(client)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

/**
 * 读线程
 */
class ReadHandlerThread implements Runnable {
    private Socket client;

    public ReadHandlerThread(Socket client) {
        this.client = client;
    }

    public void run() {
        DataInputStream dis = null;
            try {
                while (true) {
                    InputStream inputStream = client.getInputStream();
                    dis = new DataInputStream(inputStream);
                    String receiver = dis.readUTF();
                    System.out.println("客户端发来的内容为：" + receiver);
                }
            } catch (IOException e) {
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
 * 处理写操作线程
 */
class WriteHandlerThread implements Runnable {
    private Socket client;

    public WriteHandlerThread(Socket client) {
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
                if (dos != null) {
                    dos.close();
                }
                if (br != null) {
                    br.close();
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

