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

    @Query(value = "SELECT DISTINCT ON (transaction_id) id, transaction_id, tgl_transaksi, terminal_id, transaction_type, no_hp, start_time, end_time, total_time, status, kip_id, interaction_id, nama_customer, description, created_by, created_date FROM ( SELECT * FROM ps_transaction WHERE created_date >=:start AND created_date <= :end ORDER BY status)", nativeQuery = true)
    List<TransactionLog> getAllTransactionToday(@Param("start") Timestamp start, @Param("end") Timestamp end); 
}
