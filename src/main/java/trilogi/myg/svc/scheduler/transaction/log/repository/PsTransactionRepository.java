package trilogi.myg.svc.scheduler.transaction.log.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import trilogi.myg.svc.scheduler.transaction.log.entity.TransactionLog;

@Repository
public interface PsTransactionRepository extends CrudRepository<TransactionLog, Long> {
    
    @Query(value = "SELECT * FROM ps_transaction WHERE created_date >=:start AND created_date <= :end AND no_hp = :no_hp AND status = :status", nativeQuery = true)
    List<TransactionLog> getSuccessTransaction(@Param("start") Timestamp start, @Param("end") Timestamp end, @Param("no_hp") String no_hp, @Param("status") String status);
    
    @Query(value = "SELECT DISTINCT ON (transaction_type) ps_transaction.* FROM ps_transaction WHERE created_date >=:start AND created_date <= :end AND no_hp = :no_hp AND status = :status AND transaction_type IN :transaction_type", nativeQuery = true)
    List<TransactionLog> getTransaction(@Param("start") Timestamp start, @Param("end") Timestamp end, @Param("no_hp") String no_hp, @Param("status") String status, @Param("transaction_type") List<String> transaction_type);
    
    @Query(value = "SELECT DISTINCT ON (no_hp) ps_transaction.no_hp FROM ps_transaction WHERE created_date >=:start AND created_date <= :end", nativeQuery = true)
    List<String> getPhoneNumber(@Param("start") Timestamp start, @Param("end") Timestamp end);
}
