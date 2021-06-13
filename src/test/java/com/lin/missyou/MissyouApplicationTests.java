package com.lin.missyou;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
class MissyouApplicationTests {

    @Test
    void contextLoads() {
        Calendar now = Calendar.getInstance();

        System.out.println(now.getTime());
    }

}
