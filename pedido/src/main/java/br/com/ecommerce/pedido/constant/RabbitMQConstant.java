package br.com.ecommerce.pedido.constant;

public class RabbitMQConstant {
    public static final String EXCHANGE_NAME = "order-payment-exchange";
    public static final String ORDER_TO_PAYMENT_QUEUE = "order-to-payment";
    public static final String PAYMENT_TO_ORDER_QUEUE = "payment-to-order";
    public static final String ORDER_TO_PAYMENT_ROUTE = "order-to-payment-route";
    public static final String PAYMENT_TO_ORDER_ROUTE = "payment-to-order-route";
}
