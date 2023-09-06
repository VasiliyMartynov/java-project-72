package hexlet.code.repository;

import hexlet.code.BaseRepository;
import hexlet.code.domain.UrlCheck;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        List<UrlCheck> urlsChecks = getEntities();
        String sql = "INSERT INTO url_check ("
                + "id,"
                + " status_code,"
                + " title,"
                + " h1,"
                + " description,"
                + " url_id,"
                + " created_at)"
                + " VALUES (?,?,?,?,?,?,?)";
        try (var conn = BaseRepository.getDataSource().getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, urlsChecks.size() + 1);
            preparedStatement.setString(2, String.valueOf(urlCheck.getStatusCode()));
            preparedStatement.setString(3, urlCheck.getTitle());
            preparedStatement.setString(4, urlCheck.getH1());
            preparedStatement.setString(5, urlCheck.getDescription());
            preparedStatement.setString(6, String.valueOf(urlCheck.getUrlId()));
            String createdAt = String.valueOf(new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(7, createdAt);

            preparedStatement.executeUpdate();
        }
    }

    public static UrlCheck find(int id) throws SQLException {
        var sql = "SELECT * FROM url_check WHERE id = ?";
        try (var conn = BaseRepository.getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var statusCode = Integer.valueOf(resultSet.getString("status_code"));
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = Integer.valueOf(resultSet.getString("url_id"));
                var createdAt = Timestamp.valueOf(resultSet.getString("created_at"));
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(Long.valueOf(id));
                return urlCheck;
            }
            return null;
        }
    }

    public static List<UrlCheck> getEntities() throws SQLException {
        var sql = "SELECT * FROM url_check";
        try (var conn = BaseRepository.getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = Integer.valueOf(resultSet.getString("status_code"));
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = Integer.valueOf(resultSet.getString("url_id"));
                var createdAt = Timestamp.valueOf(resultSet.getString("created_at"));
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(Long.valueOf(id));
                result.add(urlCheck);
            }
            return result;
        }
    }

    public static List<UrlCheck> getEntitiesByUrlId(int urlId) throws SQLException {
        var sql = "SELECT * FROM url_check WHERE url_id = ?";
        try (var conn = BaseRepository.getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = Integer.valueOf(resultSet.getString("status_code"));
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = Timestamp.valueOf(resultSet.getString("created_at"));
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(Long.valueOf(id));
                result.add(urlCheck);
            }
            return result;
        }
    }
}
