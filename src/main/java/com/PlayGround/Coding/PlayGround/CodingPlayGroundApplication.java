package com.PlayGround.Coding.PlayGround;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Coding PlayGround Spring Boot application.
 * <p>
 * This class uses the {@link SpringBootApplication} annotation, which is a convenience
 * annotation that encapsulates the following three annotations:
 * <ul>
 *     <li>{@link org.springframework.boot.autoconfigure.EnableAutoConfiguration @EnableAutoConfiguration}:
 *         Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.</li>
 *     <li>{@link org.springframework.context.annotation.ComponentScan @ComponentScan}:
 *         Tells Spring to look for other components, configurations, and services in the 'com.PlayGround.Coding.PlayGround' package,
 *         allowing it to find and register the controllers, services, repositories, etc.</li>
 * </ul>
 */
@SpringBootApplication
public class CodingPlayGroundApplication {

    /**
     * The main method which serves as the entry point for the Java application.
     * It delegates to Spring Boot's {@link SpringApplication#run(Class, String...)} method
     * to launch the application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(CodingPlayGroundApplication.class, args);
    }

}