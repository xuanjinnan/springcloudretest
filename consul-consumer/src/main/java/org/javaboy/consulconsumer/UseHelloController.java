package org.javaboy.consulconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@RestController
public class UseHelloController {
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/hello")
    public String hello(){
        ServiceInstance instance = loadBalancerClient.choose("consul-provider");
        String host = instance.getHost();
        int port = instance.getPort();
        Map<String, String> metadata = instance.getMetadata();
        URI uri = instance.getUri();
        System.out.println("host:" + host);
        System.out.println("port:" + port);
        System.out.println("metadata:" + metadata);
        System.out.println("uri:" + uri);
        return restTemplate.getForObject("http://" + host + ":" + port + "/hello",String.class);
    }
}
