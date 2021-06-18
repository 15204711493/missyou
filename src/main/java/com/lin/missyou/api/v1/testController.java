package com.lin.missyou.api.v1;

import com.lin.missyou.manager.rocketmq.ProducerSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class testController {
    @Autowired
    private ProducerSchedule producerSchedule;

    @GetMapping("/ok")
    public void s() throws Exception{
        producerSchedule.send("TopicTest","test");
    }
}
