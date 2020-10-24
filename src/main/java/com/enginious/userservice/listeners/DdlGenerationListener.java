package com.enginious.userservice.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DdlGenerationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        String action = event
                .getEnvironment()
                .getProperty("spring.jpa.properties.javax.persistence.schema-generation.scripts.action");
        if (StringUtils.equals("create", action)) {
            Path path = Paths.get(event
                    .getEnvironment()
                    .getProperty("spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target"));
            String databaseName = event
                    .getEnvironment()
                    .getProperty("jdbc.database.name");
            if (Files.exists(path)) {
                try {
                    FileUtils.forceDelete(path.toFile());
                    FileUtils.touch(path.toFile());
                    PrintStream writer = new PrintStream(path.toFile());
                    writer.printf("-- generated at %s%n", (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS")).format(new Date()));
                    writer.printf("drop database if exists %s;%n", databaseName);
                    writer.printf("create database if not exists %s;%n", databaseName);
                    writer.flush();
                } catch (Exception e) {
                    log.error(String.format("error while deleting file [%s]:", path), e);
                    throw new RuntimeException(String.format("error while deleting file [%s]:", path), e);
                }
            }
        }
    }
}
