package br.com.ecommerce.pedido.producer;

import br.com.ecommerce.pedido.constant.RabbitMQConstant;
import br.com.ecommerce.pedido.dto.request.DadosCobrancaDTO;
import br.com.ecommerce.pedido.dto.response.DadosPagamentoDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PedidoProducer {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public PedidoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void producer(DadosCobrancaDTO dadosCobrancaDTO) {
        this.rabbitTemplate.setExchange(RabbitMQConstant.EXCHANGE_NAME);
        this.rabbitTemplate.setRoutingKey(RabbitMQConstant.ORDER_TO_PAYMENT_ROUTE);
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.rabbitTemplate.convertAndSend(dadosCobrancaDTO);
    }

}
