package com.jowk.parcel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.jowk.common.security",
        "com.jowk.common.domain",
        "com.jowk.common.api"
})
public class ParcelServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ParcelServiceApp.class, args);
    }

}