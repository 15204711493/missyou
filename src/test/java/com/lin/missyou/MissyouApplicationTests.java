package com.lin.missyou;

import com.lin.missyou.manager.rocketmq.ProducerSchedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
class MissyouApplicationTests {

    @Autowired
    private ProducerSchedule producerSchedule;


     @Test
     void pushMessageToMQ() throws Exception {
        producerSchedule.send("TopicTest", "test");
    }

}
