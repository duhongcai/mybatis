package com.duhc.thriftRpc.server;

import com.duhc.thriftRpc.service.demo.Hello;
import com.duhc.thriftRpc.service.impl.HelloServiceImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by duhc on 2017/8/1.
 */
public class HelloServiceServer {
    /**
     * 使用线程池模拟服务端
     */
    public static void main(String[] args) {

        try {
            //1:设置端口号 7911
            TServerSocket socketServer = new TServerSocket(7911);
            //2:设置协议工厂为TBinaryProtocol.Factory
            TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory();
            //3:关联处理器与Hello服务实现
            TProcessor processor = new Hello.Processor(new HelloServiceImpl());
            TThreadPoolServer.Args args1 = new TThreadPoolServer.Args(socketServer);
            args1.processor(processor);
            args1.protocolFactory(proFactory);
            TServer server = new TThreadPoolServer(args1);
            System.out.println("Start server on port 7911...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
