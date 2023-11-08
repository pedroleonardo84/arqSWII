package br.com.ecommerce.pedido.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class CarrinhoDTO {
    private List<ItemCarrinhoDTO> produtos;
    private Double valorTotal;
}

