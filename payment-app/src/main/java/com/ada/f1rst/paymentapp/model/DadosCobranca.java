package com.ada.f1rst.paymentapp.model;

import com.ada.f1rst.paymentapp.enumeration.StatusPagamentoEnum;
import com.ada.f1rst.paymentapp.enumeration.TipoPagamentoEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name="dadoscobranca")
public class DadosCobranca {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name="idpedido")
    private String idPedido;

    @Column(name="tipopagamento")
    private TipoPagamentoEnum tipoPagamento;

    @Column(name="datapagamento")
    private LocalDate dataPagamento;

    @Column(name="valorpagamento")
    private BigDecimal valorPagamento;

    @Column(name="statuspagamento")
    private StatusPagamentoEnum statusPagamento;

}
