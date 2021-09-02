package trilogi.myg.svc.scheduler.transaction.log.repository;

import org.springframework.data.repository.CrudRepository;

import trilogi.myg.svc.scheduler.transaction.log.entity.LgPayment;

public interface LgPaymentRepository extends CrudRepository<LgPayment, Long> {
    
    LgPayment findByTransactionId(String transactionId);
}
