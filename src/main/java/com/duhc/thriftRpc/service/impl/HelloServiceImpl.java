package com.duhc.thriftRpc.service.impl;

import com.duhc.thriftRpc.service.demo.Hello;
import org.apache.thrift.TException;

/**
 * Created by duhc on 2017/8/1.
 */
public class HelloServiceImpl implements Hello.Iface {

    public String helloString(String para) throws TException {
        System.out.println("入参是：" + para);
        return  "hello:"+para;
    }

    public int helloint(int para) throws TException {
        try {
            Thread.sleep(20000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return para;
    }

    public boolean helloBoolean(boolean para) throws TException {
        return para;
    }

    public void helloVoid() throws TException {
        System.out.println("Hello World");
    }

    public String helloNull() throws TException {
        return null;
    }
}
