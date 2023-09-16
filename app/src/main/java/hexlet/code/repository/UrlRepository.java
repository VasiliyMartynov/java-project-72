package hexlet.code.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import hexlet.code.BaseRepository;
import hexlet.code.domain.Url;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls(name) VALUES(?);";
        var conn = BaseRepository.getDataSource().getConnection();

        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.executeUpdate();
        }
    }

    public static Url find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";

        try (var conn = BaseRepository.getDataSource().getConnection();
            var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(name);

                url.setId(id);
                url.setCreatedAt(createdAt);
                return url;
            }
            return null;
        }
    }

    public static Url findByName(String urlName) throws SQLException {
        System.out.println("findByName 1");
        var sql = "SELECT * FROM urls WHERE name = ?";
        System.out.println("findByName 2");

        try (var conn = BaseRepository.getDataSource().getConnection();
                 var stmt = conn.prepareStatement(sql)) {
            System.out.println("findByName 3");
            stmt.setString(1, urlName);
            System.out.println("findByName 4");
            var resultSet = stmt.executeQuery();
            System.out.println("findByName 5");

            if (resultSet.next()) {
                System.out.println("findByName 6");
                var id = resultSet.getLong("id");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(urlName);

                System.out.println("findByName 7");
                url.setId(id);
                url.setCreatedAt(createdAt);
                System.out.println("findByName 8");
                return url;
            }
            return null;
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";

        try (var conn = BaseRepository.getDataSource().getConnection();
            var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<Url>();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var createdAt = resultSet.getTimestamp("created_at");
                var name = resultSet.getString("name");
                var url = new Url(name);

                url.setId(id);
                url.setCreatedAt(createdAt);
                result.add(url);
            }
            return result;
        }
    }
}
