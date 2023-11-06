package com.ada.f1rst.paymentapp.dto;


import com.ada.f1rst.paymentapp.enumeration.TipoDePagamento;

import java.math.BigDecimal;

public record DadosCobrancaDTO(
     String idPedido,
     TipoDePagamento tipoPagamento,
     CartaoCreditoDTO cartaoCredito,
     BigDecimal valorPagamento){}

