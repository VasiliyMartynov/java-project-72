package hexlet.code.domain;

import java.sql.Timestamp;
import java.time.Instant;

public final class Url {
    private long id;

    private String name;

    private Timestamp createdAt;

    public Url(String urlName) {
        this.name = urlName;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Instant getCreatedAt() {
        return this.createdAt.toInstant();
    }

    public void setId(Long newid) {
        this.id = newid;
    }

    public void setCreatedAt(Timestamp dateAndTime) {
        this.createdAt = dateAndTime;
    }
}
