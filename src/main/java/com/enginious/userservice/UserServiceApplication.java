package com.enginious.userservice;

import com.enginious.userservice.listeners.DdlGenerationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.addListeners(new DdlGenerationListener());
        app.run(args);
    }

}
