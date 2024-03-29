package pl.matkan.wholesaler.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.dto.CompanyDto;
import pl.matkan.wholesaler.dto.mapper.CompanyMapper;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.model.Company;
import pl.matkan.wholesaler.repo.CompanyRepository;
import pl.matkan.wholesaler.service.CompanyService;

import java.util.List;
import java.util.stream.Collectors;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserServiceImpl userService;
    private final CompanyMapper companyMapper;

    private final IndustryServiceImpl industryService;

    public CompanyServiceImpl(CompanyRepository companyRepo, UserServiceImpl userService, CompanyMapper companyMapper, IndustryServiceImpl industryService) {
        this.companyRepository = companyRepo;
        this.userService = userService;
        this.companyMapper = companyMapper;
        this.industryService = industryService;
    }

    @Override
    public Company create(CompanyDto companyIn) {
        Company companyToCreate = companyMapper.companyDtoToCompany(companyIn);

        companyToCreate.setIndustry(industryService.getOneIndustryByName(companyIn.getIndustryName()));
        companyToCreate.setUser(userService.getOneUserById(companyIn.getOwnerId()));

        return companyRepository.save(companyToCreate);
    }

    @Override
    public Company update(Long id, CompanyDto companyDto) {
        Company companyDataToUpdate = companyMapper.companyDtoToCompany(companyDto);
        Company companyFetched = getOneCompanyById(id);

        companyFetched.setName(companyDataToUpdate.getName());
        companyFetched.setAddress(companyDataToUpdate.getAddress());
        companyFetched.setCity(companyDataToUpdate.getCity());
        companyFetched.setNip(companyDataToUpdate.getNip());
        companyFetched.setUser(userService.getOneUserById(companyDto.getOwnerId()));
        companyFetched.setIndustry(industryService.getOneIndustryByName(companyDto.getIndustryName()));

        return companyRepository.save(companyFetched);
    }

    @Override
    public CompanyDto findById(Long id) {
        return companyMapper.companyToCompanyDto(
                companyRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Company not found", "with id:=" + id))
        );
    }

    @Override
    public boolean existsById(Long id) {
        return companyRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public List<CompanyDto> findAll() {
        List<Company> companies  = companyRepository.findAll();
        return companies.stream()
                .map(companyMapper::companyToCompanyDto)
                .collect(Collectors.toList());
    }
    public Page<CompanyDto> findCompaniesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order)
    {
        Page<Company> companies  = companyRepository.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
        return companies.map(companyMapper::companyToCompanyDto);
    }

    public Company getOneCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow((() -> new EntityNotFoundException("Company not found", "with given id:= " + id))
                );
    }
}
