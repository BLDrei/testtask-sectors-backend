package com.bldrei.sectors;

import io.micrometer.common.util.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Launcher {
  private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

  public static void main(String[] args) {
    if (StringUtils.isBlank(System.getProperty(SPRING_PROFILES_ACTIVE))) {
      System.setProperty(SPRING_PROFILES_ACTIVE, "dev");
    }

    SpringApplication.run(Launcher.class, args);
  }

}
