package com.ada.f1rst.paymentapp.consumer;

import com.ada.f1rst.paymentapp.constant.RabbitMQConstant;
import com.ada.f1rst.paymentapp.dto.DadosCobrancaDTO;
import com.ada.f1rst.paymentapp.service.CartaoCreditoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {
    @Autowired
    private final CartaoCreditoService cartaoCreditoService;

    public PaymentConsumer(CartaoCreditoService cartaoCreditoService) {
        this.cartaoCreditoService = cartaoCreditoService;
    }

    @RabbitListener(queues = RabbitMQConstant.ORDER_TO_PAYMENT_QUEUE)
    public void consumer(DadosCobrancaDTO dadosCobrancaDTO) {
        System.out.println("==================================");
        System.out.println("pedido recebido" + dadosCobrancaDTO);
        cartaoCreditoService.processarMensagem(dadosCobrancaDTO);
    }
}
