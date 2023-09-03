package hexlet.code;

import com.zaxxer.hikari.HikariDataSource;

public class BaseTestRepository {
    private static HikariDataSource dataSource;

    public static void setDataSource(HikariDataSource someDataSource) {
        dataSource = someDataSource;
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
