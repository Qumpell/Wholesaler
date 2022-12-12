package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.repo.UserRepository;
import pl.matkan.wholesaler.service.UserService;

import java.util.List;
import java.util.Optional;

@Service("userService")
@Repository
public class UserServiceImpl implements UserService {
    private final UserRepository usrRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.usrRepo = userRepo;
    }

    @Override
    public User create(User one) {
        return usrRepo.save(one);
    }

    @Override
    public User update(Long id, User one) {

        one.setId(id);
        return usrRepo.save(one);
    }

    @Override
    public Optional<User> findById(Long id) {
        //	Customer dto = modelMapper.map(one.get(), EmployeeDTO.class);
        return usrRepo.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return usrRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        usrRepo.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        List<User> all = usrRepo.findAll();
        return all;
    }
}
