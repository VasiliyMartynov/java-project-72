package hexlet.code.domain;

import java.sql.Timestamp;

public final class Url {
    private long id;

    private String name;

    private Timestamp createdAt;

    public Url() {
        super();
    }

    public Url(String urlName, Timestamp urlCreatedAt) {
        this.name = urlName;
        this.createdAt = urlCreatedAt;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public String getCreatedAtAsString() {
        return this.createdAt.toString();
    }

    public void setId(Long newid) {
        this.id = newid;
    }
}
