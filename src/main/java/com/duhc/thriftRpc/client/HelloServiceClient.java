package com.duhc.thriftRpc.client;

import com.duhc.thriftRpc.service.demo.Hello;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by duhc on 2017/8/1.
 */
public class HelloServiceClient {
    public static void main(String[] args) throws TException {
        TTransport transport = new TSocket("127.0.0.1",7911);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        Hello.Client  client = new Hello.Client(protocol);
        //1:调用服务的helloVoid方法
        String resp = client.helloString("你好，八月");
        System.out.println(resp);
        transport.close();
    }

}
