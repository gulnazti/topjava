package ru.javawebinar.topjava.service;

import org.junit.ClassRule;
import org.junit.Rule;
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
    private static StringBuilder builder;
    private static long totalTime;

    @ClassRule
    public static TestWatcher timeSummaryLogger = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            builder = new StringBuilder();
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
    public TestWatcher timeLogger = new TestWatcher() {
        private long startTime;

        @Override
        protected void starting(Description description) {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected void finished(Description description) {
            long delta = System.currentTimeMillis() - startTime;
            totalTime += delta;
            String testName = description.getMethodName();
            log.info(testName + " test took {} ms", delta);
            appendDots(testName, delta);
        }
    };

    private static void appendDots(String rowName, long delta) {
        builder.append("\n  ").append(rowName);
        long seconds = delta / 1000;
        String timeResult = seconds > 0
                            ? String.format("%d s %d ms", seconds, delta - seconds * 1000)
                            : String.format("%d ms", delta);

        int targetLength = 50;
        int appendLength = targetLength - rowName.length() - timeResult.length();
        while (appendLength-- > 0) {
            builder.append(".");
        }
        builder.append(timeResult);
    }
}
