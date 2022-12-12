package pl.matkan.wholesaler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.repo.UserRepository;

@SpringBootApplication
public class WholesalerApplication {

    public static void main(String[] args) {
        //SpringApplication.run(WholesalerApplication.class, args);


        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(WholesalerApplication.class,args);
        UserRepository userRepository = configurableApplicationContext.getBean(UserRepository.class);

        User user  = new User("Test","Test","2000-03-05","test");
        userRepository.save(user);
        User user1  = new User("Adam","Moanre","1988-01-04","adamMon");
        userRepository.save(user1);
    }

}
