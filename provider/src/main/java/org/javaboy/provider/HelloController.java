package org.javaboy.provider;

import org.javaboy.commons.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @Value("${server.port}")
    Integer port;
    @RequestMapping("/hello")
    public String hello(){
        return "hello   " + port;
    }
    @GetMapping("/hello2")
    public String hello2(String name){
        return "hello" + name;
    }
    @PostMapping("/user1")
    public User addUser1(User user){
        return user;
    }
    @PostMapping("/user2")
    public User addUser2(@RequestBody User user){
        return user;
    }
}
