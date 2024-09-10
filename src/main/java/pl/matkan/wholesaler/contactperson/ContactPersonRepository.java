package pl.matkan.wholesaler.contactperson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {

    @Modifying
    @Query("UPDATE ContactPerson cp SET cp.companyName = :newCompanyName WHERE cp.companyName = :oldCompanyName")
    int updateCompanyNameInContactPersons(@Param("oldCompanyName") String oldCompanyName, @Param("newCompanyName") String newCompanyName);
}
