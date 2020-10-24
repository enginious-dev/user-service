package com.enginious.userservice;

import com.enginious.userservice.listeners.DdlGenerationListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;

import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.addListeners(new DdlGenerationListener());
        app.run(args);
    }

}
