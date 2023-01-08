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
        //SpringApplication.run(WholesalerApplication.class, args);


        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(WholesalerApplication.class, args);
        UserRepository userRepository = configurableApplicationContext.getBean(UserRepository.class);
        RoleRepository roleRepository = configurableApplicationContext.getBean(RoleRepository.class);
        IndustryRepository industryRepository = configurableApplicationContext.getBean(IndustryRepository.class);
        CompanyRepository companyRepository = configurableApplicationContext.getBean(CompanyRepository.class);
        TradeNoteRepository tradeNoteRepository = configurableApplicationContext.getBean(TradeNoteRepository.class);
        ContactPersonRepository contactPersonRepository = configurableApplicationContext.getBean(ContactPersonRepository.class);

        Role role = new Role("USER");
        Role role1 = new Role("ADMIN");
        Role role2 = new Role("MODERATOR");
        roleRepository.save(role);
        roleRepository.save(role1);
        roleRepository.save(role2);

        User user = new User("Test", "Test", "2000-03-05", "test","123" ,role);
        userRepository.save(user);
        User user1 = new User("Adam", "Moanre", "1988-01-04", "adamMon","123" ,role1);
        userRepository.save(user1);

        Industry industry = new Industry("it");
        Industry industry1 = new Industry("construction");
        industryRepository.save(industry);
        industryRepository.save(industry1);

        Company company = new Company("test", "123456", "14-0895", "Poznan", industry, user1);
        Company company1 = new Company("mcdonald", "9194915", "14151-020", "Warszawa", industry1, user1);
        companyRepository.save(company);
        companyRepository.save(company1);

        ContactPerson contactPerson = new ContactPerson("Marek", "Kowalski", "111-222-333", "test@gmail.com", "support", company, user);
        ContactPerson contactPerson1 = new ContactPerson("Adam", "Monek", "111-222-333", "adam@gmail.com", "consultant", company1, user1);
        contactPersonRepository.save(contactPerson);
        contactPersonRepository.save(contactPerson1);

        TradeNote tradeNote = new TradeNote("test", company, user);
        TradeNote tradeNote1 = new TradeNote("sell everything", company1, user1);
        tradeNoteRepository.save(tradeNote);
        tradeNoteRepository.save(tradeNote1);

    }

}
