package com.szu.rabbitmq.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhuwutao on 2019/5/28
 */
public class Send {
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("root");
        factory.setPassword("root");
        //设置rabbitmq的地址
        factory.setHost("47.107.125.131");
        //建立连接
        Connection connection = factory.newConnection();
        //获得通道
        Channel channel = connection.createChannel();
        //声明交换器
        String exchangeName = "hello-exchange";
        channel.exchangeDeclare(exchangeName,"direct",true);
        //声明routing key
        String routingKey = "hola";
        //发布消息
        //用字符串中的点号.来表示任务的复杂性，一个点就表示需要耗时1秒
        byte[] message = ("zhuwutao2..").getBytes();
        channel.basicPublish(exchangeName, routingKey, null, message);

        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
