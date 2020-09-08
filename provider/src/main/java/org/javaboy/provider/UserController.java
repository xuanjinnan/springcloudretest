package org.javaboy.provider;

import org.javaboy.commons.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @PostMapping("/register")
    public String register(User user){
        return "redirect:/loginPage?username=" + user.getUsername();
    }
    @GetMapping("/loginPage")
    @ResponseBody
    public String loginPage(String username){
        return "login  " + username;
    }

    @PutMapping("/user1")
    @ResponseBody
    public void updateUser1(User user){
        System.out.println(user);
    }
    @PutMapping("/user2")
    @ResponseBody
    public void updateUser2(@RequestBody User user){
        System.out.println(user);
    }
    @DeleteMapping("/user1")
    @ResponseBody
    public void deleteUser1(Integer id){

        System.out.println(id);
    }
    @DeleteMapping("/user2/{id}")
    @ResponseBody
    public void deleteUser2(@PathVariable Integer id){
        System.out.println(id);
    }
}
