package br.com.ecommerce.pedido.dto.response;

import br.com.ecommerce.pedido.Enum.DescricaoStatusEnum;
import br.com.ecommerce.pedido.Enum.TipoDePagamento;

public record DadosPagamentoDTO(String idPedido, TipoDePagamento tipoPagamento, DescricaoStatusEnum statusPagamento) {}
