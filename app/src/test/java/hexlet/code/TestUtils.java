package hexlet.code;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.domain.Url;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;


public class TestUtils {

    static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");
    }

    static String getDatabaseUserName() {
        return System.getenv().getOrDefault("JDBC_DATABASE_USERNAME", "sa");
    }

    static String getDatabasePassword() {
        return System.getenv().getOrDefault("JDBC_DATABASE_USERNAME", "sa");
    }
    static Long findIdByUlrName(HikariDataSource dataSource, String urlName) {
        var sql = "SELECT id FROM url WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urlName);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Url findById(HikariDataSource dataSource, Long id) throws SQLException {
        var sql = "SELECT * FROM url WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = Timestamp.valueOf(resultSet.getString("created_at"));
                var url = new Url(name, createdAt);
                url.setId(id);
                return url;
            }
            return null;
        }
    }

    static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
    }
}
