package br.com.ecommerce.pedido.dto.request;

import br.com.ecommerce.pedido.Enum.TipoDePagamento;
import br.com.ecommerce.pedido.util.constraints.CartaoCreditoValidator;
import br.com.ecommerce.pedido.util.constraints.ValidCartaoCredito;
import br.com.ecommerce.pedido.util.constraints.ValidTipoDePagamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    private String idCarrinho;
    private double valorTotal;
    @ValidCartaoCredito
    private String numeroCartao;
    @ValidTipoDePagamento private String tipo;
    private TipoDePagamento tipoDePagamento;
    private String nomeTitular;
    private String dataValidadeCartao;
    private String codigoSeguranca;
    private String codigoBoleto;
    private String formaEntrega;
}
