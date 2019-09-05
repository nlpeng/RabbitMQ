package com.nlpeng.task;

/**
 * @author Ferry NLP
 * @create 2019-09-05
 * @see
 * @since 1.0v
 **/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nlpeng.constant.Constants;
import com.nlpeng.entity.BrokerMessageLog;
import com.nlpeng.entity.Order;
import com.nlpeng.mapper.BrokerMessageLogMapper;
import com.nlpeng.producer.RabbitOrderSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class RetryMessageTasker {


    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;
    //https://www.jianshu.com/p/1defb0f22ed1  参考@Scheduled
    @Scheduled(initialDelay = 5000, fixedDelay = 10000)//第一次延迟5秒后执行，之后按fixedRate的规则每10秒执行一次
    public void reSend(){
        //pull status = 0 and timeout message
        List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        list.forEach(messageLog -> {
            if(messageLog.getTryCount() >= 3){
                System.err.println("大于3次了");
                //update fail message
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), Constants.ORDER_SEND_FAILURE, new Date());
            } else {
                System.err.println("重新尝试");
                // resend
                brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(),  new Date());
                Order reSendOrder = JSON.parseObject(messageLog.getMessage(), Order.class);
               // Order reSendOrder = FastJsonConvertUtil.convertJSONToObject(messageLog.getMessage(), Order.class);
                try {
                    rabbitOrderSender.sendOrder(reSendOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("-----------异常处理-----------");
                }
            }
        });
    }
}

