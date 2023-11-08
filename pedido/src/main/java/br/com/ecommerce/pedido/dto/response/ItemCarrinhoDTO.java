package br.com.ecommerce.pedido.dto.response;

import lombok.Data;

@Data
public class ItemCarrinhoDTO {

    private ProdutoDTO produto;
    private Integer quantidade;
    private Double valor;
}
