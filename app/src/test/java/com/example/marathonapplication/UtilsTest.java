package com.example.marathonapplication;

import com.example.marathonapplication.square.utils.DateUtils;
import com.example.marathonapplication.utils.DealUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void float_2_format() {
        assertEquals("12.67", DealUtils.float2(12.6666f));
        assertEquals("12.00", DealUtils.float2(12f));
    }

    @Test
    public void date_format() {
        assertEquals(2021, DateUtils.dealTheDate("2021-01-01")[0]);
        assertEquals(1, DateUtils.dealTheDate("2021-01-01")[1]);
        assertEquals(1, DateUtils.dealTheDate("2021-01-01")[2]);
    }

}
