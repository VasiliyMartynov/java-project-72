package hexlet.code.domain;

import java.time.Instant;

public final class Url {
    private long id;

    private String name;

    private Instant createdAt;

    public Url(String urlName, Instant urlCreatedAt) {
        this.name = urlName;
        this.createdAt = urlCreatedAt;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setId(Long newid) {
        this.id = newid;
    }
}
