package com.jowk.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.jowk.user",
        "com.jowk.common.security",
        "com.jowk.common.domain",
        "com.jowk.common.api",
        "com.jowk.common.web"
})
public class UserServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApp.class, args);
    }

}