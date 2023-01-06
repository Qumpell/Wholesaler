package pl.matkan.wholesaler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.matkan.wholesaler.model.Role;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.repo.RoleRepository;
import pl.matkan.wholesaler.repo.UserRepository;

@SpringBootApplication
public class WholesalerApplication {

    public static void main(String[] args) {
        //SpringApplication.run(WholesalerApplication.class, args);


        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(WholesalerApplication.class,args);
        UserRepository userRepository = configurableApplicationContext.getBean(UserRepository.class);
        RoleRepository roleRepository = configurableApplicationContext.getBean(RoleRepository.class);

        Role role = new Role("user");
        Role role1 = new Role("admin");
        roleRepository.save(role);
        roleRepository.save(role1);

        User user  = new User("Test","Test","2000-03-05","test",role);
        userRepository.save(user);
        User user1  = new User("Adam","Moanre","1988-01-04","adamMon",role1);
        userRepository.save(user1);
    }

}
