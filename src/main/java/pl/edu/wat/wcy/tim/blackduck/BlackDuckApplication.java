
package pl.edu.wat.wcy.tim.blackduck;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.wat.wcy.tim.blackduck.services.PostService;

import javax.annotation.Resource;


@SpringBootApplication
public class BlackDuckApplication implements CommandLineRunner {

    @Resource
    PostService postService;

    public static void main(String[] args) {
        SpringApplication.run(BlackDuckApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        postService.deleteAll();
        postService.init();
    }

}