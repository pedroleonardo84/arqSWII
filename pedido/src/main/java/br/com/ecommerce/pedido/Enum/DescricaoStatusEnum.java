package br.com.ecommerce.pedido.Enum;

public enum DescricaoStatusEnum {
    CVV_INVALIDO("Código de Segurança inválido"),
    LIMITE_EXCEDIDO("O valor da compra excede o limite do cartão"),
    DADOS_INVALIDOS("Um ou mais dados informados são invalidos: Número do cartão, Nome Titular, Data Validade"),
    VALIDADE_EXPIRADA("O cartão informado possui data de validade expirada"),
    PAGAMENTO_EFETUADO("Pagamento realizado com sucesso"),
    PAGAMENTO_RECUSADO("Pagamento foi recusado"),
    ESTORNO_EFETUADO("Estorno realizado com sucesso"),
    PAGAMENTO_PENDENTE("O pedido está pendente de pagamento");

    private String descricao;

    DescricaoStatusEnum(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }


}
