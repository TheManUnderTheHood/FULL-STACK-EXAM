package com.eventmanagement.repository;

import com.eventmanagement.model.Payment;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends org.springframework.data.mongodb.repository.MongoRepository<Payment, String> {
    Optional<Payment> findByTransactionId(String transactionId);
    long countByStatus(Payment.PaymentStatus status);
}

