package com.ada.f1rst.paymentapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name="cartaocredito" , uniqueConstraints={@UniqueConstraint(columnNames={"numerocartao"})})
@Data
public class CartaoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name="numerocartao")
    private String numeroCartao;

    @Column(name="titular")
    private String nomeTitular;

    @Column(name = "datavalidade")
    private LocalDate dataValidade;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "limitecredito")
    private BigDecimal limiteCredito;

    @Column(name = "saldoatual")
    private BigDecimal saldoAtual;

}
