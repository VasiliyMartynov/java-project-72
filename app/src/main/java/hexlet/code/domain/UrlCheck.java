package hexlet.code.domain;

import java.sql.Timestamp;
import java.time.Instant;

public final class UrlCheck {

    private long id;
    private int statusCode;

    private String title;

    private String h1;

    private String description;

    private long urlId;

    private Timestamp createdAt;

    public UrlCheck(int urlStatusCode,
                    String urlTitle,
                    String urlH1,
                    String urlDescription,
                    long urlIdN
                    ) {
        this.statusCode = urlStatusCode;
        this.title = urlTitle;
        this.h1 = urlH1;
        this.description = urlDescription;
        this.urlId = urlIdN;
    }



    public long getId() {
        return this.id;
    }

    public Integer getStatusCode() {
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
        return this.createdAt.toInstant();
    }

    public void setUrlId(long urlId) {
        this.urlId = urlId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
