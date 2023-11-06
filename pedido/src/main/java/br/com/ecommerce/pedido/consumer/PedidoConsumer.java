package br.com.ecommerce.pedido.consumer;

import br.com.ecommerce.pedido.constant.RabbitMQConstant;
import br.com.ecommerce.pedido.dto.request.DadosCobrancaDTO;
import br.com.ecommerce.pedido.dto.response.DadosPagamentoDTO;
import br.com.ecommerce.pedido.service.service.PedidoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PedidoConsumer {

    @Autowired
    PedidoService pedidoService;
    @RabbitListener(queues = RabbitMQConstant.PAYMENT_TO_ORDER_QUEUE)
    public void consumer(DadosPagamentoDTO dadosPagamentoDTO) {
        pedidoService.processarMensagem(dadosPagamentoDTO);
    }

}
