package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.model.Company;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.repo.CompanyRepository;
import pl.matkan.wholesaler.repo.UserRepository;
import pl.matkan.wholesaler.service.CompanyService;

import java.util.List;
import java.util.Optional;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyServiceImpl(CompanyRepository companyRepo, UserRepository userRepository) {
        this.companyRepository = companyRepo;
        this.userRepository = userRepository;
    }

    @Override
    public Company create(Company companyIn) {
//        Company companyOut = new Company(
//                companyIn.getName(),
//                companyIn.getCity(),
//                companyIn.getAddress(),
//                companyIn.getNip()
//        );
//        for(ContactPerson contactPersonIn : companyIn.getContactPersonList()){
//            companyOut.addContactPerson(contactPersonIn);
//        }
//
//        for(TradeNote tradeNoteIn : companyIn.getTradeNotes()){
//            companyOut.addTradeNote(tradeNoteIn);
//        }

        return companyRepository.save(companyIn);
    }

    @Override
    public Company update(Long id, Company one) {
        one.setId(id);
        return companyRepository.save(one);
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return companyRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Company> companyOptional = companyRepository.findById(id);

        companyOptional.ifPresent(company -> {
            User user = company.getUser();

            user.removeCompany(company);

            userRepository.save(user);
        });

        companyRepository.deleteById(id);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }
}
