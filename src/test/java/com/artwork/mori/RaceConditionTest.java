package com.artwork.mori;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrmExamplesApplication.class)
@Rollback(value = false)
@SqlGroup(
        value = {
                @Sql(scripts = "classpath:insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
                @Sql(scripts = "classpath:clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        }
)
public class RaceConditionTest {

    Logger logger = LoggerFactory.getLogger(RaceConditionTest.class);

    @Autowired
    private ITestService testService;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    @Test
    public void test_concurrent_update() throws InterruptedException {

        Future<?> t1 = EXECUTOR_SERVICE.submit(() -> {
            testService.update1(1L, 10000);
            logger.info("After T1");
        });

        Future<?> t2 = EXECUTOR_SERVICE.submit(() -> {
            testService.update2(1L, 1000);
            logger.info("After T2");
        });

        Thread.sleep(3000);

        testService.getOne(1L);
        logger.info("After T3");

        EXECUTOR_SERVICE.shutdown();
        EXECUTOR_SERVICE.awaitTermination(1, TimeUnit.MINUTES);
    }
}
