package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;

@Service
public class RegistrationService {
    private final UserDaoImp userDaoImp;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public RegistrationService(UserDaoImp userDaoImp, PasswordEncoder passwordEncoder) {
        this.userDaoImp = userDaoImp;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userDaoImp.add(user);
    }
}
