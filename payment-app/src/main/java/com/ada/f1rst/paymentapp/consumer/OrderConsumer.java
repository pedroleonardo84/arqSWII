package com.ada.f1rst.paymentapp.consumer;

import com.ada.f1rst.paymentapp.dto.DadosCobrancaDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class OrderConsumer {

    public void processarMensagem(DadosCobrancaDTO dadosCobrancaDTO){

        System.out.println(dadosCobrancaDTO.idPedido());
        System.out.println(dadosCobrancaDTO.valorPagamento());
        System.out.println(dadosCobrancaDTO.cartaoCredito().numeroCartao());

    }

}
