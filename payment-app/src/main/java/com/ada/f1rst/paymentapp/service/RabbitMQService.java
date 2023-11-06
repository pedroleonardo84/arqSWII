package com.ada.f1rst.paymentapp.service;
import com.ada.f1rst.paymentapp.dto.DadosCobrancaDTO;
import com.ada.f1rst.paymentapp.dto.DadosPagamentoDTO;

public interface RabbitMQService {
    public default void consumer(DadosCobrancaDTO dadosCobrancaDTO){}

    public default void producer(DadosPagamentoDTO dadosPagamentoDTO){}
}
