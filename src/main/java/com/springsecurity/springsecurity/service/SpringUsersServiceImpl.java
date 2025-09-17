package com.springsecurity.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springsecurity.springsecurity.dto.Users;
import com.springsecurity.springsecurity.repo.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SpringUsersServiceImpl implements UserDetailsService {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepo.findByUsername(username);
        log.info("UserObject: {}", user);
        if (user == null) {
            // System.out.println("User not found");
            log.info("username not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrinciple(user);
    }

    public Users saveUser(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        userRepo.save(users);
        return users;

    }

    public String login(Users users) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.getToken(users.getUsername());

        } else {
            return "fails";
        }
    }

}
