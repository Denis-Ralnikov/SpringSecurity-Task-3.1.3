package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepositories;
import ru.kata.spring.boot_security.demo.repositories.UserRepositories;

import java.util.List;


@Service
public class UserService implements UserDetailsService{

    private UserRepositories userRepositories;
    @Autowired
    public void setUserRepositories(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }
    @Autowired
    RoleRepositories repositories;
    public User findByUsername(String username){
        return userRepositories.findByUsername(username);
    }

    public List<User> getAllUsers(){
       return userRepositories.findAll();
    }

    public boolean saveUser(User user){
        user.getRoles().add(new Role(2L,"ROLE_USER"));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepositories.save(user);
        return true;
    }
    public User getAuthUser(){
        return findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }
    public void deleteUser(String username){
        User user= findByUsername(username);
        userRepositories.delete(user);
    }
}
