package pl.matkan.wholesaler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
//import pl.matkan.wholesaler.config.RsaKeyProperties;
import pl.matkan.wholesaler.model.*;
import pl.matkan.wholesaler.repo.*;


@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
//@EnableConfigurationProperties(RsaKeyProperties.class)
public class WholesalerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WholesalerApplication.class, args);
    }

}
