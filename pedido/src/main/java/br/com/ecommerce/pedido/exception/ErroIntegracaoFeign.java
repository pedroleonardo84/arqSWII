package br.com.ecommerce.pedido.exception;

public class ErroIntegracaoFeign extends RuntimeException {

    public ErroIntegracaoFeign(String message) {
        super(message);
    }
}

