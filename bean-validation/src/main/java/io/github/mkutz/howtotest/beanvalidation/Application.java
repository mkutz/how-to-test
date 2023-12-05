package io.github.mkutz.howtotest.beanvalidation;

import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  static {
    Locale.setDefault(Locale.ENGLISH);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
