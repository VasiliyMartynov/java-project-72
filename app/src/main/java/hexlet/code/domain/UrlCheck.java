package hexlet.code.domain;

import java.sql.Timestamp;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

public final class UrlCheck {

    @Getter @Setter private long id;
    @Getter @Setter private long urlId;
    @Getter private int statusCode;
    @Getter private String title;
    @Getter private String h1;
    @Getter private String description;
    @Setter private Timestamp createdAt;

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

    public Instant getCreatedAt() {
        return this.createdAt.toInstant();
    }
}
