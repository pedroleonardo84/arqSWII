package com.ada.f1rst.paymentapp.dto;
import java.math.BigDecimal;


public record CartaoCreditoDTO(
     String numeroCartao,
     String nomeTitular,
     String dataValidade,
     String cvv) {}
