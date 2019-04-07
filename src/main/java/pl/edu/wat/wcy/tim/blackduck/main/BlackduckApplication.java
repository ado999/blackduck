package pl.edu.wat.wcy.tim.blackduck.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("pl.edu.wat.wcy.tim.blackduck")
public class BlackduckApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackduckApplication.class, args);
    }

}
