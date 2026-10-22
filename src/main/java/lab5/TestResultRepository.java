package lab5;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestResultRepository {

    public void initDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS test_results (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    student_name VARCHAR(100) NOT NULL,
                    test_title VARCHAR(100) NOT NULL,
                    score INT NOT NULL,
                    passed BOOLEAN NOT NULL DEFAULT FALSE,
                    test_date DATE NOT NULL
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("-> Таблица 'test_results' инициализирована.");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    public void dropTable() {
        String sql = "DROP TABLE IF EXISTS test_results;";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("-> Таблица 'test_results' успешно удалена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    public TestResult save(TestResult result) {
        String sql = """
                INSERT INTO test_results (student_name, test_title, score, passed, test_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, result.studentName());
            ps.setString(2, result.testTitle());
            ps.setInt(3, result.score());
            ps.setBoolean(4, result.passed());
            ps.setObject(5, result.testDate());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return result.withId(generatedId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении результата: " + e.getMessage());
        }
        return result;
    }

    public void saveAll(List<TestResult> results) {
        if (results == null || results.isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO test_results (student_name, test_title, score, passed, test_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (TestResult result : results) {
                    ps.setString(1, result.studentName());
                    ps.setString(2, result.testTitle());
                    ps.setInt(3, result.score());
                    ps.setBoolean(4, result.passed());
                    ps.setObject(5, result.testDate());
                    ps.addBatch();
                }

                int[] updateCounts = ps.executeBatch();
                conn.commit();
                System.out.println("-> Пакетная вставка успешно выполнена. Добавлено строк: " + updateCounts.length);
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Ошибка пакета, выполнен откат транзакции: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка подключения при batch-вставке: " + e.getMessage());
        }
    }

    public Optional<TestResult> findById(int id) {
        String sql = "SELECT id, student_name, test_title, score, passed, test_date FROM test_results WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToTestResult(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка поиска по ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<TestResult> findAll() {
        String sql = "SELECT id, student_name, test_title, score, passed, test_date FROM test_results ORDER BY id";
        List<TestResult> list = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToTestResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка выборки всех записей: " + e.getMessage());
        }
        return list;
    }

    public List<TestResult> findByTitle(String testTitle) {
        String sql = "SELECT id, student_name, test_title, score, passed, test_date FROM test_results WHERE test_title = ? ORDER BY score DESC";
        List<TestResult> list = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, testTitle);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToTestResult(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка поиска по дисциплине: " + e.getMessage());
        }
        return list;
    }

    public List<TestResult> findPassed() {
        String sql = "SELECT id, student_name, test_title, score, passed, test_date FROM test_results WHERE passed = TRUE ORDER BY score DESC";
        List<TestResult> list = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToTestResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка выборки сдавших студентов: " + e.getMessage());
        }
        return list;
    }

    public double getAverageScoreByTitle(String testTitle) {
        String sql = "SELECT AVG(score) FROM test_results WHERE test_title = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, testTitle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка расчета среднего балла: " + e.getMessage());
        }
        return 0.0;
    }

    public boolean updateScore(int id, int newScore) {
        boolean passed = newScore >= TestResult.PASSING_SCORE;
        String sql = "UPDATE test_results SET score = ?, passed = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newScore);
            ps.setBoolean(2, passed);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка обновления оценки: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM test_results WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка удаления: " + e.getMessage());
            return false;
        }
    }

    public void printMetadata() {
        System.out.println("\n========== ИНТРОСПЕКЦИЯ МЕТАДАННЫХ ==========");
        try (Connection conn = DatabaseConfig.getConnection()) {
            DatabaseMetaData dbMeta = conn.getMetaData();
            System.out.println("СУБД:              " + dbMeta.getDatabaseProductName());
            System.out.println("Версия СУБД:       " + dbMeta.getDatabaseProductVersion());
            System.out.println("JDBC Драйвер:      " + dbMeta.getDriverName());
            System.out.println("Версия драйвера:   " + dbMeta.getDriverVersion());
            System.out.println("URL источника:     " + dbMeta.getURL());
            System.out.println("Пользователь БД:   " + dbMeta.getUserName());

            String sql = "SELECT * FROM test_results LIMIT 1";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                ResultSetMetaData rsMeta = rs.getMetaData();
                int colCount = rsMeta.getColumnCount();
                System.out.println("\nСтруктура таблицы 'test_results' (" + colCount + " колонок):");
                for (int i = 1; i <= colCount; i++) {
                    System.out.printf("  %d. %-15s | Тип: %-12s | Размер: %d%n",
                            i,
                            rsMeta.getColumnName(i),
                            rsMeta.getColumnTypeName(i),
                            rsMeta.getColumnDisplaySize(i)
                    );
                }
            }
            System.out.println("=============================================\n");
        } catch (SQLException e) {
            System.err.println("Ошибка получения метаданных: " + e.getMessage());
        }
    }

    private TestResult mapRowToTestResult(ResultSet rs) throws SQLException {
        return new TestResult(
                rs.getInt("id"),
                rs.getString("student_name"),
                rs.getString("test_title"),
                rs.getInt("score"),
                rs.getBoolean("passed"),
                rs.getObject("test_date", LocalDate.class)
        );
    }
}
