package com.ada.f1rst.paymentapp.controller;

import com.ada.f1rst.paymentapp.dto.CartaoCreditoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="api/payment/creditcard")
public class CreditCardController {
    @PostMapping("/include")
    private ResponseEntity incluirCartao(@RequestBody CartaoCreditoDTO cartacCreditoDTO){
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/consultbynumber")
    private ResponseEntity consultCardsByNumber(@RequestBody CartaoCreditoDTO cartacCreditoDTO){
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/list")
    private ResponseEntity getAll(@RequestBody CartaoCreditoDTO cartacCreditoDTO){
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/consultbyname")
    private ResponseEntity consultCardsByName(@RequestBody CartaoCreditoDTO cartacCreditoDTO){
        return new ResponseEntity(HttpStatus.OK);
    }

}
