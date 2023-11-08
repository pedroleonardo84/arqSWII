package br.com.ecommerce.pedido.service.impl;

import br.com.ecommerce.pedido.Enum.DescricaoStatusEnum;
import br.com.ecommerce.pedido.Enum.StatusPedido;
import br.com.ecommerce.pedido.Enum.TipoDePagamento;
import br.com.ecommerce.pedido.converter.PedidoConverter;
import br.com.ecommerce.pedido.dto.request.CartaoCreditoDTO;
import br.com.ecommerce.pedido.dto.request.DadosCobrancaDTO;
import br.com.ecommerce.pedido.dto.request.PedidoRequestDTO;
import br.com.ecommerce.pedido.dto.response.*;
import br.com.ecommerce.pedido.exception.ErroIntegracaoFeign;
import br.com.ecommerce.pedido.exception.PedidoNaoEncontradoException;
import br.com.ecommerce.pedido.model.Pedido;
import br.com.ecommerce.pedido.producer.PedidoProducer;
import br.com.ecommerce.pedido.repository.PedidoRepository;
import br.com.ecommerce.pedido.service.service.CartApiFeignClient;
import br.com.ecommerce.pedido.service.service.PedidoService;
import br.com.ecommerce.pedido.service.service.StockFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired PedidoProducer pedidoProducer;

    @Autowired
    CartApiFeignClient carrinho;

    @Autowired
    StockFeignClient stockFeignClient;

    ObjectMapper objectMapper = new ObjectMapper();

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

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void processarMensagem(DadosPagamentoDTO dadosPagamentoDTO) {
        Pedido pedido = pedidoRepository.findById(String.valueOf(dadosPagamentoDTO.idPedido())).orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado!"));
        pedido.setStatusPagamento(dadosPagamentoDTO.statusPagamento());
        pedidoRepository.save(pedido);
        if (pedido.getStatusPagamento().equals(DescricaoStatusEnum.PAGAMENTO_EFETUADO)){
            try { //chamar a api Carrinho com o idCarrinho, para obter os produtos e quantidades
                CarrinhoDTO carrinhoDTO = verificarCarrinho(pedido);
                alterarEstoque(carrinhoDTO);

            } catch (FeignException e) {
                if (e.status() != 200) {
                    // O status HTTP não é 200, lança uma exceção personalizada ou tratamento de erro apropriado.
                    throw new ErroIntegracaoFeign("A chamada Feign não retornou status 200");
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            //de posse dos produtos e quantidade, chamar a api do Estoque para atualizar as quantidades
            System.out.println("Pedido Aprovado");
        }
        switch (pedido.getStatusPagamento()){
            case PAGAMENTO_RECUSADO:
            case ESTORNO_EFETUADO:
            case PAGAMENTO_PENDENTE:
                pedido.setStatusPedido(StatusPedido.AGUARDANDO_PAGAMENTO);
                pedidoRepository.save(pedido);
                break;
            case PAGAMENTO_EFETUADO:
                pedido.setStatusPedido(StatusPedido.PREPARANDO_PARA_ENTREGAR);
                pedidoRepository.save(pedido);
                break;
            case EM_PROCESSAMENTO:
                pedido.setStatusPedido(StatusPedido.EM_PROCESSAMENTO);
                pedidoRepository.save(pedido);
                break;

            case CVV_INVALIDO:
            case DADOS_INVALIDOS:
            case LIMITE_EXCEDIDO:
            case VALIDADE_EXPIRADA:
                pedido.setStatusPedido(StatusPedido.CANCELADO);
                pedidoRepository.save(pedido);
                break;
        }
    }

    private void alterarEstoque(CarrinhoDTO carrinhoDTO) {
        for (ItemCarrinhoDTO produtoDTO: carrinhoDTO.getProdutos()) {
            System.out.println("Produto "+ String.valueOf(produtoDTO.getProduto().getId())+" com quantidade "+(produtoDTO.getProduto().getQuantidade()).toString()+", passará a ter "+(produtoDTO.getProduto().getQuantidade() - 1));

            stockFeignClient.setNewQuantity(
                    Long.valueOf(
                            String.valueOf(produtoDTO.getProduto().getId())
                    ), (produtoDTO.getProduto().getQuantidade() - 1));
        }
    }

    private CarrinhoDTO verificarCarrinho(Pedido pedido) throws JsonProcessingException {
        var json = carrinho.getCarrinhoPorId(Long.parseLong(pedido.getIdCarrinho()));
        CarrinhoDTO carrinhoDTO = objectMapper.readValue(json, CarrinhoDTO.class );
        return carrinhoDTO;
    }

}
