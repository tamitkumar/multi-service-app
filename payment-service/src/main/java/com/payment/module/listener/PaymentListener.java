package com.payment.module.listener;

import com.payment.module.entity.PaymentEntity;
import com.payment.module.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {
    private final PaymentService paymentService;

    public PaymentListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = "payment-queue")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        // simulate processing
        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(message);
        payment.setAmount(999.99);
        paymentService.process(payment);
    }
}
