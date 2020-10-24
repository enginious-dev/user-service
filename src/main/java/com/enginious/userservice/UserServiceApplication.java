package com.enginious.userservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.addListeners((ApplicationEnvironmentPreparedEvent event) -> {
            Path path = Paths.get(event
                    .getEnvironment()
                    .getProperty("spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target"));
            if (Files.exists(path)) {
                try {
                    FileUtils.forceDelete(path.toFile());
                } catch (Exception e) {
                    log.error(String.format("error while deleting file [%s]:", path), e);
                    throw new RuntimeException(String.format("error while deleting file [%s]:", path), e);
                }
            }
        });
        app.run(args);
    }

}
