package netty.rpc.service.impl;

import netty.rpc.service.HelloService;

/**
 * @author dinghy
 * @date 2020/7/31 11:25
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String msg) {
        return "服务端收到消息:["+msg+"]";
    }
}
