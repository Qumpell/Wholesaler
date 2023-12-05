package pl.matkan.wholesaler.dto;

public class TradeNoteDto {
    private Long id;
    private String content;
    private Long ownerId;
    private String companyName;

    public TradeNoteDto(Long id, String content, Long ownerId, String companyName) {
        this.id = id;
        this.content = content;
        this.ownerId = ownerId;
        this.companyName = companyName;
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
}
