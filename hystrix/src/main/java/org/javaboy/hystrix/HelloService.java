package org.javaboy.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;
    @HystrixCommand(fallbackMethod = "error")
    public String hello(){
        return restTemplate.getForObject("http://provider/hello",String.class);
    }

    public String error(){
        return "It's error";
    }
}
