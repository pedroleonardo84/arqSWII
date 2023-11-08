package br.com.ecommerce.pedido.service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "carrinho-api", url = "http://appcarrinho.us-east-2.elasticbeanstalk.com")
public interface CartApiFeignClient {
    @GetMapping("/carrinhos/{id}")
       String getCarrinhoPorId(@PathVariable("id") Long id);
}
