package pl.matkan.wholesaler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import pl.matkan.wholesaler.config.RsaKeyProperties;



@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
//@EnableConfigurationProperties(RsaKeyProperties.class)
public class WholesalerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WholesalerApplication.class, args);
    }

}
