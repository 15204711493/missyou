package com.lin.missyou.manager.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class ProducerSchedule {

    private DefaultMQProducer mqProducer;

    @Value("${rocketmq.producer.producer-group}")
    private String producerGroup;
    @Value("${rocketmq.namesrv-addr}")
    private String namesrvAddr;

    public ProducerSchedule() {

    }


   @PostConstruct
    public void defaultMQProducer(){
        if(mqProducer == null){
            this.mqProducer = new DefaultMQProducer(this.producerGroup);
            this.mqProducer.setNamesrvAddr(this.namesrvAddr);
        }
        try {
            this.mqProducer.start();
            System.out.println(".....................p s");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }


    public String send(String topic,String  messageText)throws Exception{
        Message message = new Message(topic,messageText.getBytes());
        message.setDelayTimeLevel(4);
        SendResult result = this.mqProducer.send(message);
        System.out.println(result.getMsgId()+":"+result.getSendStatus());
        return result.getMsgId();

    }


}
