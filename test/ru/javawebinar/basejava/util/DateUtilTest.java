package ru.javawebinar.basejava.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

public class DateUtilTest {
    @Test
    public void isMonthNowTrue() {
        LocalDate date = LocalDate.of(2023, 7, 20);
        boolean ans = DateUtil.isMonthNow(date);
        Assert.assertTrue(ans);
    }

    @Test
    public void isMonthNowFalse() {
        LocalDate date = LocalDate.of(2020, 7, 20);
        boolean ans = DateUtil.isMonthNow(date);
        Assert.assertFalse(ans);
    }

    @Test
    public void getYearFromString() {
        String date = "2022-01";
        int res = DateUtil.getYear(date);
        Assert.assertEquals(2022, res);
    }

    @Test
    public void getYearFromEmptyString() {
        String date = "";
        int res = DateUtil.getYear(date);
        Assert.assertEquals(LocalDate.now().getYear(), res);
    }

    @Test
    public void getMonthFromString() {
        String date = "2022-05";
        Month res = DateUtil.getMonth(date);
        Assert.assertEquals(Month.MAY, res);
    }

    @Test
    public void getMonthFromEmptyString() {
        String date = "";
        Month res = DateUtil.getMonth(date);
        Assert.assertEquals(LocalDate.now().getMonth(), res);
    }
}