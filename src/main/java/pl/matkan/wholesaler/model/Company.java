package pl.matkan.wholesaler.model;

import javax.persistence.*;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String nip;

    @ManyToOne
    @JoinColumn(name="industry_id")
    private Industry industry;
    //private Long industryId;
    private String address;

    private Long userId;
    private boolean isDeleted;

    public Company() {
    }

    public Company(String name, String nip, String address, Long userId, boolean isDeleted) {
        this.name = name;
        this.nip = nip;
      //  this.industryId = industryId;
        this.address = address;
        this.userId = userId;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

//    public Long getIndustryId() {
//        return industryId;
//    }
//
//    public void setIndustryId(Long industryId) {
//        this.industryId = industryId;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
