package com.ada.f1rst.paymentapp.repository;

import com.ada.f1rst.paymentapp.model.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {

    // Método personalizado para verificar a igualdade do número do cartão
    @Query("SELECT COUNT(c) > 0 FROM CartaoCredito c WHERE c.numeroCartao = :numeroCartao")
    boolean existsByNumeroCartao(@Param("numeroCartao") String numeroCartao);

    // Método personalizado para verificar a igualdade do nome do titular
    @Query("SELECT COUNT(c) > 0 FROM CartaoCredito c WHERE c.nomeTitular = :nomeTitular")
    boolean existsByNomeTitular(@Param("nomeTitular") String nomeTitular);

    Optional<CartaoCredito> findByNumeroCartao(String numeroCartao);

}