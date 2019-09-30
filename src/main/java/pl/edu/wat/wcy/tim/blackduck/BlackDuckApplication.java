
package pl.edu.wat.wcy.tim.blackduck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BlackDuckApplication {//implements CommandLineRunner {

//    @Resource
//    FileService fileService;

    public static void main(String[] args) {
        SpringApplication.run(BlackDuckApplication.class, args);
    }

//    @Override
//    public void run(String... arg) throws Exception {
//        fileService.deleteAll();
//        fileService.init();
//    }

}