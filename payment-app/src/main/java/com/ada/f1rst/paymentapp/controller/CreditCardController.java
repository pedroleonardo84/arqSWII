package com.ada.f1rst.paymentapp.controller;

import com.ada.f1rst.paymentapp.dto.CartaoCreditoDTO;
import com.ada.f1rst.paymentapp.model.CartaoCredito;
import com.ada.f1rst.paymentapp.service.CartaoCreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="api/payment/creditcard")
public class CreditCardController {

    @Autowired
    CartaoCreditoService cartaoCreditoService;

    @PostMapping("/create")
    public ResponseEntity<?> criarCartaoCredito(@RequestBody CartaoCreditoDTO cartaoCreditoDTO) {
        try {
            CartaoCredito novoCartao = cartaoCreditoService.salvarCartaoCredito(cartaoCreditoDTO);
            return ResponseEntity.ok(novoCartao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/consultbynumber")
    private ResponseEntity consultCardsByNumber(@RequestBody CartaoCreditoDTO cartacCreditoDTO){
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/list")
    public Optional<List<CartaoCredito>> getAll() throws Exception {
        return cartaoCreditoService.getAll();
    }

    @PostMapping("/consultbyname")
    private ResponseEntity consultCardsByName(@RequestBody CartaoCreditoDTO cartacCreditoDTO){
        return new ResponseEntity(HttpStatus.OK);
    }

}
