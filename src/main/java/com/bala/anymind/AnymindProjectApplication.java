package com.bala.anymind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@ComponentScan(basePackages = "com.bala.anymind.config")
public class AnymindProjectApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AnymindProjectApplication.class, args);
        AnymindProjectApplication anymindProjectApplication = applicationContext.getBean(AnymindProjectApplication.class);
        final CountDownLatch shutdownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdownLatch.countDown();
            anymindProjectApplication.initiateShutdown(applicationContext, 0);
        }));
    }

    private void initiateShutdown(ApplicationContext applicationContext, int exitCode) {
        SpringApplication.exit(applicationContext, () -> exitCode);
    }

}