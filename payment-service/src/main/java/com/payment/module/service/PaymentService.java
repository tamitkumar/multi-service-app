package com.payment.module.service;

import com.payment.module.entity.PaymentEntity;
import com.payment.module.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    public PaymentEntity process(PaymentEntity payment) {
        if(Objects.isNull(payment)){
            return null;
        }
        PaymentEntity[] result = new PaymentEntity[1];
        if(Objects.nonNull(payment.getId())) {
            paymentRepository.findById(payment.getId()).ifPresentOrElse(existingPayment -> {
                System.out.println("Payment found: " + payment.getId());
                existingPayment.setAmount(payment.getAmount());
                existingPayment.setOrderId(payment.getOrderId());
                result[0] = paymentRepository.save(existingPayment);
            }, () -> {
                System.out.println("Payment not found: " + payment.getId());
                result[0] = paymentRepository.save(payment);
            });
        } else {
            System.out.println("Payment not found: " + payment.getId());
            result[0] = paymentRepository.save(payment);
        }

        return result[0];
    }
    public List<PaymentEntity> getAll() {
        return paymentRepository.findAll();
    }
}
