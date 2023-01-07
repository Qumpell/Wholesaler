package pl.matkan.wholesaler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.matkan.wholesaler.model.*;
import pl.matkan.wholesaler.repo.*;


@SpringBootApplication
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

        Role role = new Role("user");
        Role role1 = new Role("admin");
        roleRepository.save(role);
        roleRepository.save(role1);

        User user = new User("Test", "Test", "2000-03-05", "test", role);
        userRepository.save(user);
        User user1 = new User("Adam", "Moanre", "1988-01-04", "adamMon", role1);
        userRepository.save(user1);

        Industry industry = new Industry("it");
        Industry industry1 = new Industry("construction");
        industryRepository.save(industry);
        industryRepository.save(industry1);

        Company company = new Company("test", "123456", "14-0895", "Poznan", industry, user1);
        Company company1 = new Company("mcdonald", "9194915", "14151-020", "Warszawa", industry1, user1);
        companyRepository.save(company);
        companyRepository.save(company1);

        ContactPerson contactPerson = new ContactPerson("Marek", "Kowalski", "111-222-333", "test@gmail.com", "support", company, user1);
        ContactPerson contactPerson1 = new ContactPerson("Adam", "Monek", "111-222-333", "adam@gmail.com", "consultant", company1, user);
        contactPersonRepository.save(contactPerson);
        contactPersonRepository.save(contactPerson1);

        TradeNote tradeNote = new TradeNote("test", company, user1);
        TradeNote tradeNote1 = new TradeNote("sell everything", company1, user);
        tradeNoteRepository.save(tradeNote);
        tradeNoteRepository.save(tradeNote1);

    }

}
