package ru.javawebinar.topjava.service;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author gulnaz
 */
@ContextConfiguration({
    "classpath:spring/spring-app.xml",
    "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class AbstractServiceTest {
    private static final Logger log = LoggerFactory.getLogger(AbstractServiceTest.class);
    private static StringBuilder builder = new StringBuilder();
    private static long totalTime;

    @ClassRule
    public static TestWatcher timeSummaryLogger = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            builder.setLength(0);
            builder.append("\n").append(description.getTestClass().getSimpleName());
            totalTime = 0;
        }

        @Override
        protected void finished(Description description) {
            appendDots("TOTAL", totalTime);
            log.info(builder.toString());
        }
    };

    @Rule
    public Stopwatch timeLogger = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
            totalTime += millis;
            String testName = description.getMethodName();
            log.info(testName + " test took {} ms", millis);
            appendDots(testName, millis);
        }
    };

    private static void appendDots(String rowName, long millis) {
        builder.append("\n  ");
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        String timeResult = seconds > 0
                            ? String.format("%d s %d ms", seconds, millis - TimeUnit.SECONDS.toMillis(seconds))
                            : String.format("%d ms", millis);

        int targetLength = 50;
        String row = String.format("%-" + (targetLength - timeResult.length()) + "s", rowName).replaceAll(" ", ".");
        builder.append(row).append(timeResult);
    }
}
