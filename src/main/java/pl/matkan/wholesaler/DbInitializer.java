package pl.matkan.wholesaler;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.company.CompanyRepository;
import pl.matkan.wholesaler.contactperson.ContactPerson;
import pl.matkan.wholesaler.contactperson.ContactPersonRepository;
import pl.matkan.wholesaler.industry.Industry;
import pl.matkan.wholesaler.industry.IndustryRepository;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleRepository;
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.tradenote.TradeNoteRepository;
import pl.matkan.wholesaler.user.User;
import pl.matkan.wholesaler.user.UserRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;


@Component
@ConditionalOnProperty(name = "app.db-init",havingValue = "false")
@RequiredArgsConstructor
public class DbInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final IndustryRepository industryRepository;
    private final CompanyRepository companyRepository;
    private final TradeNoteRepository tradeNoteRepository;
    private final ContactPersonRepository contactPersonRepository;


    @Override
    public void run(String... args) throws Exception {
//       try {
//           Role role = new Role("USER");
//           Role role1 = new Role("ADMIN");
//           Role role2 = new Role("MODERATOR");
//           roleRepository.saveAll(List.of(role1, role2, role));
//
//           User user = new User("Test", "Test", LocalDate.of(2000, Month.AUGUST,22), "test","123" ,role);
//           //        userRepository.save(user);
//           User user1 = new User("Adam", "Moanre", LocalDate.of(1998, Month.MAY,1), "adamMon","123" ,role1);
//           //        userRepository.save(user1);
//
//           Industry industry = new Industry("it");
//           Industry industry1 = new Industry("construction");
//           industryRepository.save(industry);
//           industryRepository.save(industry1);
//
//           Company company = new Company("Test", "123456789", "14-048", "Poznan");
//           Company company1 = new Company("Als", "919491584", "11-020", "Warsaw");
//
//           ContactPerson contactPerson = new ContactPerson("Marek", "Kowalski", "111-222-333", "test@gmail.com", "support");
//           ContactPerson contactPerson1 = new ContactPerson("Adam", "Monek", "111-222-333", "adam@gmail.com", "consultant");
//           //        contactPersonRepository.save(contactPerson);
//           //        contactPersonRepository.save(contactPerson1);
//
//           TradeNote tradeNote = new TradeNote("test");
//           TradeNote tradeNote1 = new TradeNote("sell everything");
//           //        tradeNoteRepository.save(tradeNote);
//           //        tradeNoteRepository.save(tradeNote1);
//           company.addContactPerson(contactPerson);
//           company1.addContactPerson(contactPerson1);
//           company.addTradeNote(tradeNote);
//           company1.addTradeNote(tradeNote1);
//           user.addTradeNotes(tradeNote);
//           user1.addTradeNotes(tradeNote1);
//           user.addCompany(company);
//           user1.addCompany(company1);
//           user.addContactPerson(contactPerson);
//           user1.addContactPerson(contactPerson1);
//           industry.addCompany(company);
//           industry1.addCompany(company1);
//
//
//
//           industryRepository.saveAll(List.of(industry1,industry));
//
//           userRepository.saveAll(List.of(user, user1));
//       }catch (Exception e) {
//           System.out.println("Data is already initialized");
//       }
//
    }
}
