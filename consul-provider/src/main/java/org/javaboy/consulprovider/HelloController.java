package org.javaboy.consulprovider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    //稍后验证集群，注入服务端口号，以验证 provider 集群
    @Value("${server.port}")
    Integer port;
    @GetMapping("/hello")
    public String hello(){
        return "hello :" + port;
    }
}
