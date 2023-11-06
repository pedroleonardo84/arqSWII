package br.com.ecommerce.pedido.dto.request;


public record CartaoCreditoDTO(
     String numeroCartao,
     String nomeTitular,
     String dataValidade,
     String cvv) {}
