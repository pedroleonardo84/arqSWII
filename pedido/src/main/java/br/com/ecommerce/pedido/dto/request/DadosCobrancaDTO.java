package br.com.ecommerce.pedido.dto.request;

import br.com.ecommerce.pedido.Enum.TipoDePagamento;

import java.math.BigDecimal;

public record DadosCobrancaDTO(
     String idPedido,
     TipoDePagamento tipoPagamento,
     br.com.ecommerce.pedido.dto.request.CartaoCreditoDTO cartaoCredito,
     BigDecimal valorPagamento){}

