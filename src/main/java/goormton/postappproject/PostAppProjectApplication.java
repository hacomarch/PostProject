package goormton.postappproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class PostAppProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostAppProjectApplication.class, args);
    }

}
