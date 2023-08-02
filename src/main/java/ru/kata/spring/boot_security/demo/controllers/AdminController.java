package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String adminPage(Model model) {
        model.addAttribute("listUser", userService.getAllUsers());
        return "users";
    }
    @GetMapping("/saveUser")
    public String saveUser(Model model){
        return  "save-user";
    }

    @PostMapping("/saveUser")
    public String saveUserAndRedirect(@RequestParam String username, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email,
                                      @RequestParam String password, @RequestParam Byte age, @RequestParam String role_admin, Model model){
        User user = new User(username, firstName, lastName, email, password, age, new HashSet<>());
        if(!role_admin.equals("null")){
            user.getRoles().add(new Role(1L,"ROLE_ADMIN"));
        }
        User userFromDB = userService.findByUsername(user.getUsername());
        if (userFromDB == null) {
            userService.saveUser(user);
        }

        return "redirect:/admin";
    }


    @GetMapping("/{username}/edit")
    public String editUser(@PathVariable(value = "username") String username,Model model){
        User user = userService.findByUsername(username);
        model.addAttribute("user",user);
        return "edit_user";
    }
    @PostMapping("/{username}/edit")
    public String editUserPost(@PathVariable(value = "username") String username, @RequestParam String firstName,
                               @RequestParam String lastName, @RequestParam String email, @RequestParam String password,
                               @RequestParam Byte age, @RequestParam String role_admin, Model model){
        User user = userService.findByUsername(username);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setAge(age);
        if(!role_admin.equals("null")){
            user.setRoles(new HashSet<>());
            user.getRoles().add(new Role(1L,"ROLE_ADMIN"));
        } else{
            user.setRoles(new HashSet<>());
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{username}/delete")
    public String deleteUser(@PathVariable(value = "username") String username,Model model){
        userService.deleteUser(username);
        return "redirect:/admin";
    }
}
