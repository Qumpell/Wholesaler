package pl.matkan.wholesaler.dto;

public class CompanyDto {
    private Long id;
    private String name;
    private String nip;
    private String address;
    private String city;
    private String industryName;
    private Long ownerId;


    public CompanyDto(Long id, String name, String nip, String address, String city, String industryName, Long ownerId) {
        this.id = id;
        this.name = name;
        this.nip = nip;
        this.address = address;
        this.city = city;
        this.industryName = industryName;
        this.ownerId = ownerId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

}
