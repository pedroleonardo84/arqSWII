package com.ada.f1rst.paymentapp.producer;

import com.ada.f1rst.paymentapp.constant.RabbitMQConstant;
import com.ada.f1rst.paymentapp.dto.DadosPagamentoDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentProducer {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void producer(DadosPagamentoDTO dadosPagamentoDTO) {
        this.rabbitTemplate.setExchange(RabbitMQConstant.EXCHANGE_NAME);
        this.rabbitTemplate.setRoutingKey(RabbitMQConstant.PAYMENT_TO_ORDER_ROUTE);
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.rabbitTemplate.convertAndSend(dadosPagamentoDTO);
        System.out.println("==================================");
        System.out.println("mensagem enviada" + dadosPagamentoDTO);
    }

}
