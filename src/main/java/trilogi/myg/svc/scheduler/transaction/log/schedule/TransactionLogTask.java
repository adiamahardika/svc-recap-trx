package trilogi.myg.svc.scheduler.transaction.log.schedule;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import trilogi.myg.svc.scheduler.transaction.log.entity.LgPayment;
import trilogi.myg.svc.scheduler.transaction.log.entity.TransactionLog;
import trilogi.myg.svc.scheduler.transaction.log.repository.LgPaymentRepository;
import trilogi.myg.svc.scheduler.transaction.log.repository.PsTransactionRepository;

@Component
@RefreshScope
public class TransactionLogTask {
	
	private static final Logger scheduleLog = LogManager.getLogger("schedule-log");
	
	@Autowired
    private LgPaymentRepository lgPaymentRepository;
	
	@Autowired
    private PsTransactionRepository transactionRepository;
	
	@Value("${sftp.scheduler.transaction.log.folder.local}")
    private String folderLocal;
	
	@Scheduled(cron = "0 0/1 * 1/1 * *")
    public void transactionLogScheduler() {
		scheduleLog.info("Starting Scheduler ...");
		try {
			DecimalFormat df = new DecimalFormat("###");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date start = cal.getTime();

            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date end = cal.getTime();

            String fileName = new SimpleDateFormat("yyyyMMdd").format(start);
            String file = "Trilogi_MyGraPARI_" + fileName + "_trilogi.csv";
            
            Path path = Paths.get(folderLocal + file);
            
            scheduleLog.info("Checking File : {}", file);
            if (!Files.exists(path)) {
	            
            	BufferedWriter writer = new BufferedWriter(new FileWriter(folderLocal + file));
	            String header = "transaction_id|date_time|terminal_id|transaction_type|no_hp|start_time|end_time|status|description|payment_method|price_base|payment_value|code";
	            writer.write(header);
	            writer.newLine(); 
	            
	            List<TransactionLog> listData = transactionRepository.getAllTransactionToday(new Timestamp(start.getTime()), new Timestamp(end.getTime()));
	            for (TransactionLog value: listData) {
	            	LgPayment lgPayment = lgPaymentRepository.findByTransactionId(value.getTransactionId());
	            	String paymentMethod = lgPayment != null ? lgPayment.getPaymentMethod().toUpperCase() : "NONE";
	            	String priceBase = lgPayment != null ? lgPayment.getAmount().toString().replaceFirst("^0+(?!$)", "") : "";
	            	if (priceBase != "") {            		
	            		priceBase = priceBase.substring(0, priceBase.length() - 2);
	            	}
	            	String paymentValue = lgPayment != null ? lgPayment.getSubmitAmount().toString().replaceFirst("^0+(?!$)", "") : "";
	            	if (paymentValue != "") {            		
	            		paymentValue = paymentValue.substring(0, paymentValue.length() - 2);
	            	}
	            	
	            	String transactionType = "";
	            	switch (value.getTransactionType().toUpperCase()) {
					case "IP":
						transactionType = "Beli Pulsa";
						break;
					case "UP":
						transactionType = "Ganti Kartu Upgrade 4G";
						break;
					case "GK":
						transactionType = "Ganti Kartu Hilang/Rusak";
						break;
					case "PB":
						transactionType = "Pembayaran Tagihan kartuHalo";
						break;
					case "PSB":
						transactionType = "Pasang Baru kartuHalo";
						break;
					case "PA":
						transactionType = "Aktivasi Paket";
						break;
					default:
						transactionType = "Beli Pulsa";
						break;
					}
	            	
	            	String status = "";
	            	switch (value.getStatus()) {
					case "00":
						status = "Berhasil";
						break;
					case "01":
						status = "Gagal";
						break;
					case "02":
						status = "Cancel";
						break;
					default:
						status = "Berhasil";
						break;
					}
	            	
	            	String data = value.getTransactionId() + "|" + new SimpleDateFormat("yyyy-MM-dd").format(start.getTime()) + " " + value.getStartTime() + "|";
	            	data += (value.getTerminalId() + "|" + transactionType + "|" + value.getNoHp() + "|");
	            	data += (new SimpleDateFormat("yyyy-MM-dd").format(start.getTime()) + " " + value.getStartTime() + "|");
	            	data += (new SimpleDateFormat("yyyy-MM-dd").format(start.getTime()) + " " + value.getEndTime() + "|");
	            	data += (status + "|" +  value.getDescription() + "|" + paymentMethod + "|" + priceBase + "|" + paymentValue + "|" + "T");
	            	writer.write(data);
	                writer.newLine();
	                System.out.println(data);
	            }
	            writer.close();
            } else {
        	 Path pathSuccess = Paths.get(folderLocal + "/sent/" + file);
//             if (!Files.exists(pathSuccess)) { //send to sftp
//                 sentToSftpRevass((folderLocal + file), file, folderLocal, 0);
//             }
            }
		} catch (Exception e) {
			 e.printStackTrace();
	         System.out.println(e.getMessage());
		}
	}

}
