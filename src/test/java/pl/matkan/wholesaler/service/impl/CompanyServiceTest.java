//package pl.matkan.wholesaler.service.impl;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import pl.matkan.wholesaler.model.*;
//import pl.matkan.wholesaler.repo.*;
//
//import java.util.List;
//
////@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//class CompanyServiceTest {
//
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private  UserRepository userRepository;
//    @Autowired
//    private IndustryRepository industryRepository;
//    @Autowired
//    private CompanyRepository companyRepository;
//    @Autowired
//    private TradeNoteRepository tradeNoteRepository;
//    @Autowired
//    private ContactPersonRepository contactPersonRepository;
//
//    @Test
//    void should_Delete_Related_Objects_When_Deleted(){
//        Role role = new Role("USER");
//        Role role1 = new Role("ADMIN");
//        Role role2 = new Role("MODERATOR");
//        roleRepository.save(role);
//        roleRepository.save(role1);
//        roleRepository.save(role2);
//
//        User user = new User("Test", "Test", "2000-03-05", "test","123" ,role);
//
//        User user1 = new User("Adam", "Moanre", "1988-01-04", "adamMon","123" ,role1);
//
//
//        Industry industry = new Industry("it");
//        Industry industry1 = new Industry("construction");
//
//
//        Company company = new Company("test", "123a456", "14-0895", "Poznan");
//        Company company1 = new Company("mcdonald", "9194915", "14151-020", "Warszawa");
//
//
//
//        ContactPerson contactPerson = new ContactPerson("Marek", "Kowalski", "111-222-333", "test@gmail.com", "support");
//        ContactPerson contactPerson1 = new ContactPerson("Adam", "Monek", "111-222-333", "adam@gmail.com", "consultant");
//
//
//
//        TradeNote tradeNote = new TradeNote("test");
//        TradeNote tradeNote1 = new TradeNote("sell everything");
//        roleRepository.save(role);
//        roleRepository.save(role1);
//        roleRepository.save(role2);
//        industryRepository.save(industry);
//        industryRepository.save(industry1);
//        companyRepository.save(company);
//        companyRepository.save(company1);
//        contactPersonRepository.save(contactPerson);
//        contactPersonRepository.save(contactPerson1);
//        tradeNoteRepository.save(tradeNote);
//        tradeNoteRepository.save(tradeNote1);
//        userRepository.save(user);
//        userRepository.save(user1);
//        user1.addCompany(company);
//        industry.addCompany(company);
//        user.addCompany(company1);
//        industry1.addCompany(company1);
//
//        user1.addContactPerson(contactPerson1);
//        user.addContactPerson(contactPerson);
//        company.addContactPerson(contactPerson);
//        company1.addContactPerson(contactPerson1);
//        user.addTradeNotes(tradeNote);
//        user1.addTradeNotes(tradeNote1);
//        company.addTradeNotes(tradeNote);
//        company1.addTradeNotes(tradeNote1);
//        roleRepository.save(role);
//        roleRepository.save(role1);
//        roleRepository.save(role2);
//        industryRepository.save(industry);
//        industryRepository.save(industry1);
//        companyRepository.save(company);
//        companyRepository.save(company1);
//        contactPersonRepository.save(contactPerson);
//        contactPersonRepository.save(contactPerson1);
//        tradeNoteRepository.save(tradeNote);
//        tradeNoteRepository.save(tradeNote1);
//        userRepository.save(user);
//        userRepository.save(user1);
////        System.out.println(industryRepository.findAll());
////        System.out.println(roleRepository.findAll());
////        System.out.println(userRepository.findAll());
////        System.out.println(companyRepository.findAll());
////        System.out.println(tradeNoteRepository.findAll());
////        System.out.println(contactPersonRepository.findAll());
//        System.out.println("PO");
//
//        List<Company> companies1 = companyRepository.findAll();
////        System.out.println(companies1.get(0).getContactPersonList().size());
////        System.out.println("WHOT");
//        company.removeContactPerson(contactPerson);
////        company.removeTradeNote(tradeNote);
//        contactPersonRepository.save(contactPerson);
////        tradeNoteRepository.save(tradeNote);
////        companyRepository.save(company);
////        companyRepository.deleteById(company.getId());
//
////        contactPersonRepository.deleteById(contactPerson.getId());
//
////        System.out.println(companyRepository.findAll());
////        companyRepository.findAll().forEach(t ->
////                System.out.println(t.getContactPersonList()));
//        System.out.println(companyRepository.findAll());
////        System.out.println(companies.get(0).getCity());
//
////        System.out.println(tradeNoteRepository.findAll());
////        System.out.println(contactPersonRepository.findAll());
////        companyRepository.deleteById(company.getId());
////        System.out.println(contactPersonRepository.findAll());
//    }
//}