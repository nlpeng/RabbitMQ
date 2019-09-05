package com.nlpeng.consumer;

import com.nlpeng.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Ferry NLP
 * @create 2019-09-04
 * @see
 * @since 1.0v
 **/
@Component
public class OrderReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order-queue",durable = "true"),
            exchange = @Exchange(name = "order-exchange",durable = "true", type = "topic"),
            key = "order.*"))
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, @Headers Map<String,Object> headers, Channel channel)throws Exception {
        System.err.println("_____________________________");
        System.err.println(order.getId());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag,false);
    }
}
