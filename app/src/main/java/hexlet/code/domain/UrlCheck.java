package hexlet.code.domain;

import java.sql.Timestamp;

public final class UrlCheck {

    private long id;
    private int statusCode;

    private String title;

    private String h1;

    private String description;

    private int urlId;

    private Timestamp createdAt;

    public UrlCheck() {
        super();
    }

    public UrlCheck(int urlStatusCode,
                    String urlTitle,
                    String urlH1,
                    String urlDescription,
                    int urlIdN,
                    Timestamp checkCreatedAtn) {
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

    public int getUrlId() {
        return urlId;
    }

    public void setId(Long newid) {
        this.id = newid;
    }

    public int getUrl() {
        return this.urlId;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public String getCreatedAtAsString() {
        return this.createdAt.toString();
    }
}
