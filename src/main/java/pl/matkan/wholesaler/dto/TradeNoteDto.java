package pl.matkan.wholesaler.dto;

public class TradeNoteDto {
    private Long id;
    private String content;
    private Long ownerId;
    private String companyName;
    private Long companyId;

    public TradeNoteDto(Long id, String content, Long ownerId, String companyName, Long companyId) {
        this.id = id;
        this.content = content;
        this.ownerId = ownerId;
        this.companyName = companyName;
        this.companyId = companyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
