package hexlet.code;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import static hexlet.code.TestUtils.findById;
import static hexlet.code.TestUtils.findIdByUlrName;
import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;



public class AppTest {
    private static Javalin app;
    private static MockWebServer mockServer;
    private static String baseUrl;
    private static HikariConfig hikariConfig;
    protected static HikariDataSource dataSource;

    private static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
    }


    @BeforeAll
    public static void beforeAll() throws IOException, SQLException {
        //Javalin setup
        app = App.getApp();
        app.start();
        int port = app.port();
        baseUrl = "http://localhost:" + port;

        //DB setup
        hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
        dataSource = new HikariDataSource(hikariConfig);
        BaseTestRepository.setDataSource(dataSource);


        //Mock server setup
        mockServer = new MockWebServer();
        MockResponse mockedResponse = new MockResponse()
                .addHeader("Content-Type", "text/html; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(readFixture("index.html"));
        mockServer.enqueue(mockedResponse);
        mockServer.start();


    }

    @BeforeEach
    final void beforeEach() throws IOException {

    }

    @AfterAll
    public static void afterAll() throws IOException {
        app.stop();
        mockServer.shutdown();
    }

    @Nested
    class UrlTest {
        @Test
        void testIndexPage() {
            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            assertThat(response.getStatus()).isEqualTo(200);
        }

        @Test
        void testAddUrl() {
            String inputUrl = "https://testwebpage.ru";
            HttpResponse responsePost = Unirest
                    .post(baseUrl + "/urls")
                    .field("url", inputUrl)
                    .asEmpty();

            assertThat(responsePost.getStatus()).isEqualTo(302);

            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains("Страница успешно добавлена");
        }

        @Test
        void testAddWrongUrl() {
            String inputUrl = "yandex.ru";
            HttpResponse responsePost = Unirest
                    .post(baseUrl + "/urls")
                    .field("url", inputUrl)
                    .asEmpty();

            assertThat(responsePost.getStatus()).isEqualTo(302);

            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).doesNotContain(inputUrl);
            assertThat(body).contains("Некорректный URL");
        }

        @Test
        void testAddSameUrl() {
            String testUrl = mockServer.url("/").toString().replaceAll("/$", "");
            HttpResponse responsePost1 = Unirest
                    .post(baseUrl + "/urls")
                    .field("url", testUrl)
                    .asEmpty();

            HttpResponse responsePost2 = Unirest
                    .post(baseUrl + "/urls")
                    .field("url", testUrl)
                    .asEmpty();

            assertThat(responsePost2.getStatus()).isEqualTo(302);

            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains(testUrl);
            assertThat(body).contains("Страница уже существует");
        }

        @Test
        void testListOfUrls() {
            String testUrl = mockServer.url("/").toString().replaceAll("/$", "");
            String inputUrl2 = baseUrl;
            Unirest
                    .post(baseUrl + "/urls")
                    .field("url", testUrl)
                    .asEmpty();
            Unirest
                    .post(baseUrl + "/urls")
                    .field("url", inputUrl2)
                    .asEmpty();

            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls/")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains(testUrl);
            assertThat(body).contains(inputUrl2);
        }

        @Test
        void testShowUrl() {
            String testUrl = mockServer.url("/").toString().replaceAll("/$", "");
            Unirest
                    .post(baseUrl + "/urls")
                    .field("url", testUrl)
                    .asEmpty();
            var id = findIdByUlrName(dataSource, testUrl);
            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls/1")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains(testUrl);
        }

        @Test
        void testCheckUrl() throws SQLException {
            String testUrl = mockServer.url("/").toString().replaceAll("/$", "");

            Unirest
                    .post(baseUrl + "/urls")
                    .field("url", testUrl)
                    .asEmpty();

            var id = findIdByUlrName(dataSource, testUrl);
            var urlFromDB = findById(dataSource, id);
            assertThat(urlFromDB.getName()).isEqualTo(testUrl);

            Unirest
                    .post(baseUrl + "/urls/" + id.toString() + "/checks")
                    .asString();

            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls/" + id)
                    .asString();

            String body = response.getBody();
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains(testUrl);
            assertThat(body).contains("statements of great people");
            assertThat(body).contains("Do not expect a miracle");
        }


    }
}