
package pl.edu.wat.wcy.tim.blackduck;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.wat.wcy.tim.blackduck.models.Role;
import pl.edu.wat.wcy.tim.blackduck.models.RoleName;
import pl.edu.wat.wcy.tim.blackduck.repositories.RoleRepository;
import pl.edu.wat.wcy.tim.blackduck.services.PostService;

import javax.annotation.Resource;


@SpringBootApplication
public class BlackDuckApplication implements CommandLineRunner {

    @Resource
    PostService postService;

    @Resource
    RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(BlackDuckApplication.class, args);
    }

    @Override
    public void run(String... arg) {
        postService.deleteAll();
        postService.init();
        if (!roleRepository.existsByName(RoleName.USER))
            roleRepository.save(new Role(RoleName.USER));
    }

}