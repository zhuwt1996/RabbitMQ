package com.szu.rabbitmq.hello;

import com.rabbitmq.client.*;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhuwutao on 2019/5/28
 */
public class Recieve {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("localhost");

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
            boolean autoAck = false;
            String consumerTag = "";
            channel.basicConsume(queueName,autoAck,consumerTag,new DefaultConsumer(channel){

                @Override
                public void handleDelivery(String consumerTag, 
                                           Envelope envelope, 
                                           AMQP.BasicProperties properties, 
                                           byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    System.out.println("消息的路由键为：" + routingKey);
                    System.out.println("消息的内容类型为：" + contentType);
                    long deliveryTag = envelope.getDeliveryTag();
                    //确认消息
                    channel.basicAck(deliveryTag,false);
                    System.out.println("消息的内容：");
                    String bodyStr = new String(body, "UTF-8");
                    System.out.println(bodyStr);
                }
            });
        }

    }
}
