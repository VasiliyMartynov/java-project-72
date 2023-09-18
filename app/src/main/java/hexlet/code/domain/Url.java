package hexlet.code.domain;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.Instant;

public final class Url {

    @Getter @Setter private long id;
    @Getter private String name;
    @Setter private Timestamp createdAt;

    public Url(String urlName) {
        this.name = urlName;
    }

    public Instant getCreatedAt() {
        return this.createdAt.toInstant();
    }
}
