package hexlet.code.domain;

import java.time.Instant;

public final class UrlCheck {

    private long id;
    private int statusCode;

    private String title;

    private String h1;

    private String description;

    private long urlId;

    private Instant createdAt;

    public UrlCheck(int urlStatusCode,
                    String urlTitle,
                    String urlH1,
                    String urlDescription,
                    long urlIdN,
                    Instant checkCreatedAtn) {
        this.statusCode = urlStatusCode;
        this.title = urlTitle;
        this.h1 = urlH1;
        this.description = urlDescription;
        this.urlId = urlIdN;
        this.createdAt = checkCreatedAtn;
    }

    public long getId() {
        return this.id;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getTitle() {
        return this.title;
    }

    public String getH1() {
        return this.h1;
    }

    public String getDescription() {
        return this.description;
    }

    public long getUrlId() {
        return urlId;
    }

    public void setId(Long newid) {
        this.id = newid;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

}
