package com.ada.f1rst.paymentapp.controller;

import com.ada.f1rst.paymentapp.dto.DadosPagamentoDTO;
import com.ada.f1rst.paymentapp.producer.PaymentProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="api/payment/bilingdata")
public class BilingDataController {
    @Autowired
    private final PaymentProducer paymentProducer;

    public BilingDataController(PaymentProducer paymentProducer) {
        this.paymentProducer = paymentProducer;
    }

    @PutMapping("/updatebiling")
    private ResponseEntity updateBiling(@RequestBody DadosPagamentoDTO dadosPagamentoDTO){
        this.paymentProducer.producer(dadosPagamentoDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
