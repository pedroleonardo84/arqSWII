package com.ada.f1rst.paymentapp.dto;

import com.ada.f1rst.paymentapp.enumeration.DescricaoStatusEnum;
import com.ada.f1rst.paymentapp.enumeration.TipoDePagamento;


public record DadosPagamentoDTO(String idPedido, TipoDePagamento tipoPagamento, DescricaoStatusEnum statusPagamento) {
}
