package pl.edu.wat.wcy.tim.blackduck.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import pl.edu.wat.wcy.tim.blackduck.main.BlackduckApplication;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BlackduckApplication.class);
    }

}
