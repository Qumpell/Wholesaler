//package pl.matkan.wholesaler;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Component;
//import pl.matkan.wholesaler.model.*;
//import pl.matkan.wholesaler.repo.*;
//
//
//@Component
//@ConditionalOnProperty(name = "app.db-init",havingValue = "true")
//public class DbInitializer implements CommandLineRunner {
//    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;
//    private final IndustryRepository industryRepository;
//    private final CompanyRepository companyRepository;
//    private final TradeNoteRepository tradeNoteRepository;
//    private final ContactPersonRepository contactPersonRepository;
//
//    public DbInitializer(RoleRepository roleRepository, UserRepository userRepository, IndustryRepository industryRepository, CompanyRepository companyRepository, TradeNoteRepository tradeNoteRepository, ContactPersonRepository contactPersonRepository) {
//        this.roleRepository = roleRepository;
//        this.userRepository = userRepository;
//        this.industryRepository = industryRepository;
//        this.companyRepository = companyRepository;
//        this.tradeNoteRepository = tradeNoteRepository;
//        this.contactPersonRepository = contactPersonRepository;
//    }
//
//
//    @Override
//    public void run(String... args) throws Exception {
//        Role role = new Role("USER");
//        Role role1 = new Role("ADMIN");
//        Role role2 = new Role("MODERATOR");
//        roleRepository.save(role);
//        roleRepository.save(role1);
//        roleRepository.save(role2);
//
//        User user = new User("Test", "Test", "2000-03-05", "test","123" ,role);
//        userRepository.save(user);
//        User user1 = new User("Adam", "Moanre", "1988-01-04", "adamMon","123" ,role1);
//        userRepository.save(user1);
//
//        Industry industry = new Industry("it");
//        Industry industry1 = new Industry("construction");
//        industryRepository.save(industry);
//        industryRepository.save(industry1);
//
//        Company company = new Company("test", "123456", "14-0895", "Poznan", industry, user1);
//        Company company1 = new Company("mcdonald", "9194915", "14151-020", "Warszawa", industry1, user1);
//        companyRepository.save(company);
//        companyRepository.save(company1);
//
//        ContactPerson contactPerson = new ContactPerson("Marek", "Kowalski", "111-222-333", "test@gmail.com", "support", company, user);
//        ContactPerson contactPerson1 = new ContactPerson("Adam", "Monek", "111-222-333", "adam@gmail.com", "consultant", company1, user1);
//        contactPersonRepository.save(contactPerson);
//        contactPersonRepository.save(contactPerson1);
//
//        TradeNote tradeNote = new TradeNote("test", company, user);
//        TradeNote tradeNote1 = new TradeNote("sell everything", company1, user1);
//        tradeNoteRepository.save(tradeNote);
//        tradeNoteRepository.save(tradeNote1);
//    }
//}
