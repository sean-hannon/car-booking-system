package com.seanhannon;

import com.seanhannon.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends WebConfig {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
