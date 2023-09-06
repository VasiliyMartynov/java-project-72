package hexlet.code;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.domain.Url;

import java.sql.SQLException;
import java.sql.Timestamp;


public class TestUtils {
    static Long findIdByUlrName(HikariDataSource dataSource, String urlName) {
        var sql = "SELECT id FROM url WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urlName);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                return id;
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
                url.setId(Long.valueOf(id));
                return url;
            }
            return null;
        }
    }
}
