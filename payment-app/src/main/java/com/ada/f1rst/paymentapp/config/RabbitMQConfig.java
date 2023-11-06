package com.ada.f1rst.paymentapp.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange createExchange() {return new DirectExchange("order-payment-exchange");}

    @Bean
    public Queue createProducerQueue(){return new Queue("payment-to-order-queue", true);}

    public Queue createConsumerQueue(){return new Queue("order-to-payment-queue", true);}

    @Bean
    public Binding createBinding1(Queue queue, DirectExchange exchange) {return BindingBuilder.bind(queue).to(exchange).with("route-payment-to-order");}
    @Bean
    public Binding createBinding2(Queue queue, DirectExchange exchange) {return BindingBuilder.bind(queue).to(exchange).with("route-order-to-payment");}

    @Bean
    public ConnectionFactory createConnection(){
        var connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("localhost:5672");
        connectionFactory.setUsername("grupo5");
        connectionFactory.setPassword("12345");
        return connectionFactory;
    }

        @Bean
    public RabbitTemplate createTemplateProducer(){
        var template = new RabbitTemplate();
        template.setExchange("order-payment-exchange");
        template.setRoutingKey("route-payment-to-order");
        return template;
    }

    @Bean
    public RabbitTemplate createTemplateConsumer(){
        var template = new RabbitTemplate();
        template.setExchange("order-payment-exchange");
        template.setRoutingKey("route-order-to-payment");
        return template;
    }
}
*/

