package com.ada.f1rst.paymentapp.service;

import com.ada.f1rst.paymentapp.constant.RabbitMQConstant;
import com.ada.f1rst.paymentapp.consumer.OrderConsumer;
import com.ada.f1rst.paymentapp.dto.DadosCobrancaDTO;
import com.ada.f1rst.paymentapp.dto.DadosPagamentoDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements RabbitMQService{

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private final OrderConsumer orderConsumer;
    public RabbitMQServiceImpl(RabbitTemplate rabbitTemplate, OrderConsumer orderConsumer) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderConsumer = orderConsumer;
    }

    @Override
    @RabbitListener(queues = RabbitMQConstant.ORDER_TO_PAYMENT_QUEUE)
    public void consumer(DadosCobrancaDTO dadosCobrancaDTO) {
        orderConsumer.processarMensagem(dadosCobrancaDTO);
    }

    @Override
    public void producer(DadosPagamentoDTO dadosPagamentoDTO) {
        this.rabbitTemplate.setExchange(RabbitMQConstant.EXCHANGE_NAME);
        this.rabbitTemplate.setRoutingKey(RabbitMQConstant.PAYMENT_TO_ORDER_ROUTE);
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.rabbitTemplate.convertAndSend(dadosPagamentoDTO);
    }

}
