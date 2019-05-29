package com.szu.rabbitmq.workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhuwutao on 2019/5/28
 */
public class Recieve {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("root");
        factory.setPassword("root");
        factory.setHost("47.107.125.131");

        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        String exchangeName = "hello-exchange";
        channel.exchangeDeclare(exchangeName,"direct",true);
        //声明队列
        String queueName = channel.queueDeclare().getQueue();

        String routingKey = "hola";
        //绑定队列
        channel.queueBind(queueName,exchangeName,routingKey);

        //消费信息
        while (true){
            //AutoAck设置为true，开启接收确认
            boolean autoAck = true;
            String consumerTag = "";
            channel.basicConsume(queueName,autoAck,consumerTag,new DefaultConsumer(channel){

                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("消息的内容：" + message);
                    try {
                        doWork(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("work done");
                    }
                }
            });
        }

    }

    private static void doWork(String message) throws InterruptedException {
        for (char ch: message.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }

    }
}
