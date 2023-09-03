package hexlet.code.repository;

import hexlet.code.BaseRepository;
import hexlet.code.domain.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO url_check ("
                + " status_code,"
                + " title,"
                + " h1,"
                + " description,"
                + " url_id,"
                + " created_at)"
                + " VALUES (?,?,?,?,?,?)";
        try (var conn = BaseRepository.getDataSource().getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, String.valueOf(urlCheck.getStatusCode()));
            preparedStatement.setString(2, urlCheck.getTitle());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getDescription());
            preparedStatement.setString(5, String.valueOf(urlCheck.getUrlId()));
            String createdAt = String.valueOf(new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(6, createdAt);

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            // Устанавливаем ID в сохраненную сущность
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
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
                //var urlIdN = Integer.valueOf(resultSet.getString("url_id"));
                var createdAt = Timestamp.valueOf(resultSet.getString("created_at"));
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(Long.valueOf(id));
                result.add(urlCheck);
            }
            return result;
        }
    }
}
