package com.david4it;

import org.jasig.cas.client.boot.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//开启cas client的注解
@EnableCasClient
public class PicServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PicServerApplication.class, args);
    }
}
