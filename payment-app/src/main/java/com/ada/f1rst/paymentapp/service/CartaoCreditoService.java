package com.ada.f1rst.paymentapp.service;

import com.ada.f1rst.paymentapp.dto.CartaoCreditoDTO;
import com.ada.f1rst.paymentapp.dto.DadosCobrancaDTO;
import com.ada.f1rst.paymentapp.dto.DadosPagamentoDTO;
import com.ada.f1rst.paymentapp.enumeration.DescricaoStatusEnum;
import com.ada.f1rst.paymentapp.enumeration.TipoDePagamento;
import com.ada.f1rst.paymentapp.model.CartaoCredito;
import com.ada.f1rst.paymentapp.producer.PaymentProducer;
import com.ada.f1rst.paymentapp.repository.CartaoCreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CartaoCreditoService {
    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;

    @Autowired
    private final PaymentProducer paymentProducer;


    public CartaoCreditoService(PaymentProducer paymentProducer) {
        this.paymentProducer = paymentProducer;

    }

    public void processarMensagem(DadosCobrancaDTO dadosCobrancaDTO) {
        CartaoCreditoDTO cartaoCreditoDTO = dadosCobrancaDTO.cartaoCredito();
        if(dadosCobrancaDTO.tipoPagamento().equals(TipoDePagamento.CARTAO_DE_CREDITO)){
            if (!validarInformacoesCartaoCredito(cartaoCreditoDTO)){
                returnDadosInvalidos(dadosCobrancaDTO);
            } else {
                DadosPagamentoDTO dadosPagamentoDTO = buscarPorNumeroCartao(cartaoCreditoDTO.numeroCartao(), dadosCobrancaDTO);
                this.paymentProducer.producer(dadosPagamentoDTO);
            }
        } else {
            //boleto (pagto tardio) e demais ainda nao implementados
            DadosPagamentoDTO dadosPagamentoDTO = buildDadosPagamento(dadosCobrancaDTO, DescricaoStatusEnum.PAGAMENTO_PENDENTE);
            this.paymentProducer.producer(dadosPagamentoDTO);
        }
    }

    public DadosPagamentoDTO buscarPorNumeroCartao(String numeroCartao, DadosCobrancaDTO dadosCobrancaDTO) {
        Optional<CartaoCredito> cartaoOpcional = cartaoCreditoRepository.findByNumeroCartao(numeroCartao);
        DadosPagamentoDTO DadosPagamentoDTO;


        if (cartaoOpcional.isPresent()) {
            CartaoCredito cartao = cartaoOpcional.get();

            if(!cartao.getNomeTitular().equals(dadosCobrancaDTO.cartaoCredito().nomeTitular()) ||
                (!cartao.getDataValidade().equals(dadosCobrancaDTO.cartaoCredito().dataValidade()))) {
                return buildDadosPagamento(dadosCobrancaDTO, DescricaoStatusEnum.DADOS_INVALIDOS);
            }

            if (!cartao.getCvv().equals(dadosCobrancaDTO.cartaoCredito().cvv())) {
                return buildDadosPagamento(dadosCobrancaDTO, DescricaoStatusEnum.CVV_INVALIDO);
            }

            if(!verificarValidadeExpirada(cartao)){
                return buildDadosPagamento(dadosCobrancaDTO, DescricaoStatusEnum.VALIDADE_EXPIRADA);
            }

            if(!verficaLimite(cartao.getSaldoAtual(),dadosCobrancaDTO.valorPagamento())){
                return buildDadosPagamento(dadosCobrancaDTO, DescricaoStatusEnum.LIMITE_EXCEDIDO);
            }

            return buildDadosPagamento(dadosCobrancaDTO, DescricaoStatusEnum.PAGAMENTO_EFETUADO);

        } else {
            return buildDadosPagamento(dadosCobrancaDTO, DescricaoStatusEnum.DADOS_INVALIDOS);
        }
    }

    private boolean verficaLimite(BigDecimal saldoAtual, BigDecimal valorCompra) {
        int resultado = saldoAtual.compareTo(valorCompra);
        return (resultado >= 0);
    }

    private boolean verificarValidadeExpirada(CartaoCredito cartao) {
        Pattern pattern = Pattern.compile("^(\\d{2})/(\\d{2})$");
        Matcher matcher = pattern.matcher(cartao.getDataValidade());

        String anoBase = "99";
        String mesBase = "12";

        if (matcher.matches()) {
             mesBase = matcher.group(1);
             anoBase = matcher.group(2);
        } else {
            return false;
        }

        LocalDate dataAtual = LocalDate.now();
        int mes = dataAtual.getMonthValue();

        DateTimeFormatter formatoAno = DateTimeFormatter.ofPattern("yy"); // Formato de dois dígitos
        String anoFormatado = dataAtual.format(formatoAno);

        if (anoFormatado.equals(anoBase)){
            return (Integer.parseInt(mesBase) >= mes);
        }

        return (Integer.parseInt(anoFormatado) > Integer.parseInt(anoBase));

    }

    private void returnDadosInvalidos(DadosCobrancaDTO dadosCobrancaDTO) {
        DadosPagamentoDTO dadosPagamentoDTO = buildDadosPagamento(dadosCobrancaDTO, DescricaoStatusEnum.DADOS_INVALIDOS);
        this.paymentProducer.producer(dadosPagamentoDTO);
    }

    private static DadosPagamentoDTO buildDadosPagamento(DadosCobrancaDTO dadosCobrancaDTO, DescricaoStatusEnum descricaoStatusEnum) {
        return new DadosPagamentoDTO(dadosCobrancaDTO.idPedido(), dadosCobrancaDTO.tipoPagamento(),descricaoStatusEnum);
    }


    //    public CartaoCredito salvarCartaoCredito(CartaoCredito cartaoCredito) {
//        if (validarInformacoesCartaoCredito(cartaoCredito)) {
//            return cartaoCreditoRepository.save(cartaoCredito);
//        } else {
//            throw new IllegalArgumentException("Informações do cartão de crédito inválidas");
//        }
//
  private static boolean validarInformacoesCartaoCredito(CartaoCreditoDTO cartaoCredito) {
            // Validação do número do cartão usando o algoritmo de Luhn
            if (!isValidLuhn(cartaoCredito.numeroCartao())) {
                return false;
            }

            // Validação do nome do titular (pode adicionar regras específicas, como tamanho mínimo/máximo)
            if (cartaoCredito.nomeTitular() == null || cartaoCredito.nomeTitular().isEmpty()) {
                return false;
            }

            // Validação da data de validade no formato MM/YY (pode adicionar regras como data futura)
            if (!isValidDataValidade(cartaoCredito.dataValidade())) {
                return false;
            }

            // Validação do CVV (código de segurança de 3 dígitos)
            if (!isValidCVV(cartaoCredito.cvv())) {
                return false;
            }

            return true;
        }

    private static boolean isValidLuhn(String numeroCartao) {
        // Remova espaços em branco e caracteres não numéricos
        numeroCartao = numeroCartao.replaceAll("\\s", "").replaceAll("[^0-9]", "");

        // Verifique se o número do cartão é composto apenas por dígitos e tem pelo menos dois dígitos
        if (!numeroCartao.matches("^\\d{2,}$")) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;

        // Percorra os dígitos do número do cartão da direita para a esquerda
        for (int i = numeroCartao.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(numeroCartao.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        // O número do cartão é válido se a soma dos dígitos for um múltiplo de 10
        return sum % 10 == 0;
    }

        private static boolean isValidDataValidade(String dataValidade) {
            if (dataValidade == null || !dataValidade.matches("\\d{2}/\\d{2}")) {
                return false;
            }

            // Você também pode adicionar validações adicionais, como verificar se a data é futura

            return true;
        }

        private static boolean isValidCVV(String cvv) {
            if (cvv == null || !cvv.matches("\\d{3}")) {
                return false;
            }

            return true;
        }

//    public CartaoCredito salvarCartaoCredito(CartaoCreditoDTO cartaoCredito) {
//        if (validarInformacoesCartaoCredito(cartaoCredito) &&
//                !cartaoCreditoRepository.existsByNumeroCartao(cartaoCredito.numeroCartao()) &&
//                !cartaoCreditoRepository.existsByNomeTitular(cartaoCredito.nomeTitular())) {
//            return cartaoCreditoRepository.save(cartaoCredito);
//        } else {
//            throw new IllegalArgumentException("Informações do cartão de crédito inválidas ou já existem na tabela");
//        }
//    }

}