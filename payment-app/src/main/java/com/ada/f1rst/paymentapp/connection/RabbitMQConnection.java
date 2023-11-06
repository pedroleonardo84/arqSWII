package com.ada.f1rst.paymentapp.connection;

import com.ada.f1rst.paymentapp.constant.RabbitMQConstant;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConnection {

    private final AmqpAdmin amqpAdmin;
    public RabbitMQConnection(AmqpAdmin amqpAdmin){
        this.amqpAdmin = amqpAdmin;
    }

    private Queue queueCreate(String queueName) {
        return new Queue(queueName, true,false,false);
    }

    private DirectExchange exchange(){
        return new DirectExchange(RabbitMQConstant.EXCHANGE_NAME);
    }

    private Binding createBinding(Queue queue,DirectExchange exchange, String route) {
        return BindingBuilder.bind(queue).to(exchange).with(route);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    private void configure(){
        Queue paymentProducer = this.queueCreate(RabbitMQConstant.PAYMENT_TO_ORDER_QUEUE);
        Queue paymentConsumer = this.queueCreate(RabbitMQConstant.ORDER_TO_PAYMENT_QUEUE);

        DirectExchange exchange = this.exchange();

        Binding bindingPayment = this.createBinding(paymentProducer,exchange,RabbitMQConstant.PAYMENT_TO_ORDER_ROUTE);
        Binding bindingOrder = this.createBinding(paymentConsumer,exchange,RabbitMQConstant.ORDER_TO_PAYMENT_ROUTE);

        this.amqpAdmin.declareQueue(paymentProducer);
        this.amqpAdmin.declareQueue(paymentConsumer);

        this.amqpAdmin.declareExchange(exchange);

        this.amqpAdmin.declareBinding(bindingPayment);
        this.amqpAdmin.declareBinding(bindingOrder);

    }

}
