package lab5;

import java.time.LocalDate;

public record TestResult(
        Integer id,
        String studentName,
        String testTitle,
        int score,
        boolean passed,
        LocalDate testDate
) {
    public static final int PASSING_SCORE = 60;

    public TestResult(String studentName, String testTitle, int score, LocalDate testDate) {
        this(null, studentName, testTitle, score, score >= PASSING_SCORE, testDate);
    }

    public TestResult withId(int generatedId) {
        return new TestResult(generatedId, studentName, testTitle, score, passed, testDate);
    }
}
