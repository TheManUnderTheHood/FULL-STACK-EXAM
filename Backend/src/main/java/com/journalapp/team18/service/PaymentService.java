package com.eventmanagement.service;

import com.eventmanagement.model.Payment;
import com.eventmanagement.model.Registration;
import com.eventmanagement.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Process mock payment for event registration
     * In production, this would integrate with actual payment gateways
     */
    public Payment processMockPayment(Registration registration, Double amount) {
        String transactionId = "TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .transactionId(transactionId)
                .amount(amount)
                .status(Payment.PaymentStatus.COMPLETED)
                .paymentMethod(Payment.PaymentMethod.MOCK)
                .gatewayResponse("Mock payment successful")
                .paidAt(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    /**
     * Process payment with specific method
     */
    public Payment processPayment(Registration registration, Double amount, Payment.PaymentMethod method) {
        String transactionId = "TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .transactionId(transactionId)
                .amount(amount)
                .status(Payment.PaymentStatus.COMPLETED)
                .paymentMethod(method)
                .gatewayResponse("Payment processed successfully")
                .paidAt(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public long getTotalCompletedPayments() {
        return paymentRepository.countByStatus(Payment.PaymentStatus.COMPLETED);
    }

    public Double getTotalRevenue() {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getStatus().equals(Payment.PaymentStatus.COMPLETED))
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
