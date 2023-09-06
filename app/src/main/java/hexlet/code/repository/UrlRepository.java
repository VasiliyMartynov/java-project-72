package hexlet.code.repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import hexlet.code.BaseRepository;
import hexlet.code.domain.Url;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        List<Url> urls = getEntities();
        var conn = BaseRepository.getDataSource().getConnection();
        String sql = "INSERT INTO url (id, name, created_at) VALUES (?,?,?)";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, urls.size() + 1);
            preparedStatement.setString(2, url.getName());
            String createdAt = String.valueOf(new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(3, createdAt);
            preparedStatement.executeUpdate();
        }
    }

    public static Url find(int id) throws SQLException {
        var sql = "SELECT * FROM url WHERE id = ?";
        try (var conn = BaseRepository.getDataSource().getConnection();
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

    public static Url findByName(String urlName) throws SQLException {
        var sql = "SELECT * FROM url WHERE name = ?";
        try (var conn = BaseRepository.getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urlName);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var createdAt = Timestamp.valueOf(resultSet.getString("created_at"));
                var url = new Url(name, createdAt);
                url.setId(Long.valueOf(id));
                return url;
            }
            return null;
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM url";
        try (var conn = BaseRepository.getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var createdAt = Timestamp.valueOf(resultSet.getString("created_at"));
                var url = new Url(name, createdAt);
                url.setId(Long.valueOf(id));
                result.add(url);
            }
            return result;
        }
    }
}
