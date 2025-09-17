package com.springsecurity.springsecurity.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurity.springsecurity.dto.Students;
import com.springsecurity.springsecurity.dto.Users;
import com.springsecurity.springsecurity.service.SpringUsersServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SpringSecurity {

    @Autowired
    private SpringUsersServiceImpl springUsersServiceImpl;

    List<Students> listStudents = new ArrayList<>(
            List.of(new Students(1, "Baskar", "80"), new Students(2, "Baskar", "80"),
                    new Students(3, "Baskar", "80")));

    @GetMapping("/auth")
    public String auth(HttpServletRequest httpServletRequest) {
        return "Welcom to Spring Security:>>>>>> " + httpServletRequest.getSession().getId();
    }

    @PostMapping("/studnets")
    public Students students(@RequestBody Students students) {
        listStudents.add(students);
        return students;
    }

    @GetMapping("/getstudnets")
    public List<Students> getStudents(final Authentication authentication) {
        log.info("authentication: ", authentication.getName());
        return listStudents;
    }

    @GetMapping("/csrf-token")
    public org.springframework.security.web.csrf.CsrfToken getToken(HttpServletRequest httpServletRequest) {
        return (org.springframework.security.web.csrf.CsrfToken) httpServletRequest.getAttribute("_csrf");

    }

    @PostMapping("/saveusers")
    public Users saveUser(@RequestBody Users users) {
        return springUsersServiceImpl.saveUser(users);
    }

}
