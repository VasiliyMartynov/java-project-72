package hexlet.code;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.stream.Collectors;



public class TestUtils {

    protected static void executeSQL(String sqlFileName) throws IOException {
        var url = AppTest.class.getClassLoader().getResource(sqlFileName);
        var file = new File(url.getFile());
        var sql = Files.lines(file.toPath())
                .collect(Collectors.joining("\n"));
        var dataSource = BaseTestRepository.getDataSource();
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
