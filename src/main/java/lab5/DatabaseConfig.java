package lab5;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConfig {

    private static final String PROPERTIES_FILE = "database.properties";
    private static final Properties properties = new Properties();

    static {
        boolean loaded = false;

        try (InputStream is = DatabaseConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                properties.load(is);
                loaded = true;
            }
        } catch (IOException ignored) {
        }

        if (!loaded) {
            try (InputStream is = DatabaseConfig.class.getResourceAsStream("/" + PROPERTIES_FILE)) {
                if (is != null) {
                    properties.load(is);
                    loaded = true;
                }
            } catch (IOException ignored) {
            }
        }

        if (!loaded) {
            Path[] candidatePaths = {
                    Path.of("src", "main", "resources", PROPERTIES_FILE),
                    Path.of("resources", PROPERTIES_FILE),
                    Path.of(PROPERTIES_FILE)
            };
            for (Path path : candidatePaths) {
                if (Files.exists(path)) {
                    try (InputStream is = Files.newInputStream(path)) {
                        properties.load(is);
                        loaded = true;
                        break;
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        if (!loaded) {
            properties.setProperty("db.url", "jdbc:mysql://localhost:3306/testing_db?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false");
            properties.setProperty("db.user", "root");
            properties.setProperty("db.password", "root");
        }
    }

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUser() {
        return properties.getProperty("db.user");
    }
}
