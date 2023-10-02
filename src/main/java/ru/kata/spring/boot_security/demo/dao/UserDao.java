package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
   void add(User user);
   void delete(User user);
   void edit(User user);
   User getById(Long id);
   List<User> listUsers();
   UserDetails loadUserByUsername(String username);

   boolean isUserExists(User user);
}
