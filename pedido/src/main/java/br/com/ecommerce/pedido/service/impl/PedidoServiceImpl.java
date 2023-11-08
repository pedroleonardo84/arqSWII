package br.com.ecommerce.pedido.service.impl;

import br.com.ecommerce.pedido.Enum.DescricaoStatusEnum;
import br.com.ecommerce.pedido.Enum.TipoDePagamento;
import br.com.ecommerce.pedido.converter.PedidoConverter;
import br.com.ecommerce.pedido.dto.request.CartaoCreditoDTO;
import br.com.ecommerce.pedido.dto.request.DadosCobrancaDTO;
import br.com.ecommerce.pedido.dto.request.PedidoRequestDTO;
import br.com.ecommerce.pedido.dto.response.DadosPagamentoDTO;
import br.com.ecommerce.pedido.dto.response.PedidoDTO;
import br.com.ecommerce.pedido.dto.response.ResponseDTO;
import br.com.ecommerce.pedido.exception.PedidoNaoEncontradoException;
import br.com.ecommerce.pedido.model.Pedido;
import br.com.ecommerce.pedido.producer.PedidoProducer;
import br.com.ecommerce.pedido.repository.PedidoRepository;
import br.com.ecommerce.pedido.service.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired PedidoProducer pedidoProducer;

    private PedidoConverter pedidoConverter = new PedidoConverter();

    @Override
    public Optional<ResponseDTO> finalizarPedido(PedidoRequestDTO pedido) {
        Optional<Pedido> pedidoRetorno = Optional.of(pedidoRepository.save(pedidoConverter.criarPedido(pedido)));

        if (pedidoRetorno.isPresent()){
            PedidoDTO pedidoDTO = new PedidoDTO();
            var id = pedidoRetorno.map(pedidoMap -> pedidoConverter.convertToResponseDTO(pedidoMap).getId());
            pedidoDTO.setId(id.orElse(1L));

            CartaoCreditoDTO cartaoCreditoDTO = new CartaoCreditoDTO(pedido.getNumeroCartao(), pedido.getNomeTitular() ,pedido.getDataValidadeCartao(),pedido.getCodigoSeguranca());
            DadosCobrancaDTO dadosCobrancaDTO = new DadosCobrancaDTO(pedidoDTO.getId().toString(), pedido.getTipoDePagamento() , cartaoCreditoDTO, BigDecimal.valueOf(pedido.getValorTotal()));

            pedidoProducer.producer(dadosCobrancaDTO);
        }

        return Optional.of(
                pedidoRetorno.map(
                        pedidoMap ->
                                new ResponseDTO("Pedido criado com sucesso!",
                                        pedidoConverter.convertToResponseDTO(pedidoMap), LocalDateTime.now())
                    ).orElse(new ResponseDTO("Erro ao criar o pedido", pedido, LocalDateTime.now())));
    }

    @Override
    public Optional<List<Pedido>> getAll() throws Exception {
        return Optional.ofNullable(Optional.ofNullable(pedidoRepository.findAll()).orElseThrow(() -> new Exception("Erro ao buscar lista de Pedidos!")));
    }

    @Override
    public Optional<ResponseDTO> findById(String id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return Optional.ofNullable(pedido.map(pedidoMap -> new ResponseDTO("pedido encontrado!", pedidoMap, LocalDateTime.now())).orElseThrow(() -> new PedidoNaoEncontradoException("Não encontrado pedido de ID " + id)));

    }

    @Override
    public Pedido update(String id, PedidoRequestDTO pedidoRequest) throws Exception {
        Pedido pedido = pedidoRepository.findById(String.valueOf(id)).orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado!"));

        pedido.setDataTransacao(new Date());
        pedido.setTipo(TipoDePagamento.valueOf(pedidoRequest.getTipo()));
        pedido.setCodigoBoleto(pedidoRequest.getCodigoBoleto());
        pedido.setCodigoSeguranca(pedidoRequest.getCodigoSeguranca());
        pedido.setValorTotal(pedidoRequest.getValorTotal());
        pedido.setNumeroCartao(pedidoRequest.getNumeroCartao());
        pedido.setDataValidadeCartao(pedidoRequest.getDataValidadeCartao());
        pedido.setFormaEntrega(pedidoRequest.getFormaEntrega());

        return pedidoRepository.save(pedido);

    }

    @Override
    public void delete(String id) {
        pedidoRepository.findById(String.valueOf(id)).orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado!"));
        this.pedidoRepository.deleteById(id);
    }

    @Override
    public void processarMensagem(DadosPagamentoDTO dadosPagamentoDTO) {
        Pedido pedido = pedidoRepository.findById(String.valueOf(dadosPagamentoDTO.idPedido())).orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado!"));
        pedido.setStatusPagamento(dadosPagamentoDTO.statusPagamento());
        pedidoRepository.save(pedido);
        if (pedido.getStatusPagamento().equals(DescricaoStatusEnum.PAGAMENTO_EFETUADO)){
            //chamar a api Carrinho com o idCarrinho, para obter os produtos e quantidades
            //pedido.getIdCarrinho();
            //de posse dos produtos e quantidade, chamar a api do Estoque para atualizar as quantidades
            System.out.println("Pedido Aprovado");
        }
        System.out.println("Pedido " + pedido.getId() + " atualizado. O status do pagamento é: " + pedido.getStatusPagamento());
    }

}
