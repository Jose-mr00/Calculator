package com.rankingtable.calculator.impl;

import com.rankingtable.calculator.RankingTableCalculator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class RankingTableCalculatorImplTest {
    RankingTableCalculator impl = new RankingTableCalculatorImpl();

    @Test
    public void test_exceptionFileDontExist() {
        String filePathInvalid = "thefilePath";
        IOException thrown = assertThrows(
                IOException.class,
                () -> impl.getFileLinesList(filePathInvalid),
                "Expected doThing() to throw, but it didn't"
        );
    }

    @SneakyThrows
    @Test
    public void test_getFileLinesList_correctFile() {

        URL res = getClass().getClassLoader().getResource("results.txt");
        File file = Paths.get(res.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();

        String filePathInvalid = absolutePath;
        List<String> lines= impl.getFileLinesList(filePathInvalid);
        assertNotNull(lines);
        assertEquals(5, lines.size());

    }

    @SneakyThrows
    @Test
    public void test_getFileLinesList_withEmptyLines() {

        URL res = getClass().getClassLoader().getResource("results_withEmptyLines.txt");
        File file = Paths.get(res.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();

        String filePathInvalid = absolutePath;
        List<String> lines= impl.getFileLinesList(filePathInvalid);
        assertNotNull(lines);
        assertEquals(5, lines.size());

    }

    @SneakyThrows
    @Test
    public void test_getRankingTableInfo_correctFile() {

        List<String> lines = new ArrayList<>();
        lines.add("lions 3 , snakes 3");
        lines.add("Tarantulas 1 , FC Awesome 0");
        lines.add("lions 1 , FC Awesome 1");
        lines.add("Tarantulas 3, snakes 1");
        lines.add("lions 4, Grouches 0");

        String result = impl.getRankingTableInfo(lines);
        assertNotNull(result);
        assertTrue(result.contains("1. Tarantulas, 6"));
        assertTrue(result.contains("2. lions, 5"));
        assertTrue(result.contains("3. FC Awesome, 1"));
        assertTrue(result.contains("3. snakes, 1"));
        assertTrue(result.contains("4. Grouches, 0"));

    }

    @SneakyThrows
    @Test
    public void test_getRankingTableInfo_errorInLine() {

        List<String> lines = new ArrayList<>();
        lines.add("lions 3 , snakes 3");
        lines.add("Tarantulas 1 , FC Awesome 0");
        lines.add("lions 1 , FC Awesome 1%");
        lines.add("Tarantulas 3, snakes 1");
        lines.add("lions 4, Grouches 0");

        String result = impl.getRankingTableInfo(lines);
        assertNotNull(result);
        assertTrue(result.contains("1. Tarantulas, 6"));
        assertTrue(result.contains("2. lions, 4"));
        assertTrue(result.contains("3. snakes, 1"));
        assertTrue(result.contains("4. FC Awesome, 0"));
        assertTrue(result.contains("4. Grouches, 0"));

    }
    @SneakyThrows
    @Test
    public void test_getRankingTableInfo_errorInLineSideA() {

        List<String> lines = new ArrayList<>();
        lines.add("lions 3# , snakes 3");
        lines.add("Tarantulas 1 , FC Awesome 0");
        lines.add("lions 1 , FC Awesome 1");
        lines.add("Tarantulas 3, snakes 4");
        lines.add("lions 4, Grouches 0");

        String result = impl.getRankingTableInfo(lines);
        assertNotNull(result);
        assertTrue(result.contains("1. lions, 4"));
        assertTrue(result.contains("2. snakes, 3"));
        assertTrue(result.contains("2. Tarantulas, 3"));
        assertTrue(result.contains("3. FC Awesome, 1"));
        assertTrue(result.contains("4. Grouches, 0"));

    }
}