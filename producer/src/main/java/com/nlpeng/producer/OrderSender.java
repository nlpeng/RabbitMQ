package com.nlpeng.producer;

import com.nlpeng.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ferry NLP
 * @create 2019-09-04
 * @see
 * @since 1.0v
 **/
@Component
public class OrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Order order) throws Exception{

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getId());
        rabbitTemplate.convertAndSend("order-exchange","order.abcd",order,correlationData);
    }





}
