package br.com.ecommerce.pedido.dto.response;

import lombok.Data;

@Data
public class ProdutoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer quantidade;
}
