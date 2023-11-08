package br.com.ecommerce.pedido.service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stock-api", url = "http://stock-app.us-east-2.elasticbeanstalk.com")
public interface StockFeignClient {
    @PutMapping("/api/stock/{id}")
    String setNewQuantity(@PathVariable("id") Long id, @RequestParam("newQuantity") Integer newQuantity);
}
