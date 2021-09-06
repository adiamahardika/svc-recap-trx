package trilogi.myg.svc.scheduler.transaction.log.schedule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import trilogi.myg.svc.scheduler.transaction.log.service.SFTPClient;

@Component
@RefreshScope
public class TransactionLogTask {
	
	private static final Logger scheduleLog = LogManager.getLogger("schedule-log");
	
	@Autowired
    private LgPaymentRepository lgPaymentRepository;
	
	@Autowired
    private PsTransactionRepository transactionRepository;
	
	@Autowired
    private SFTPClient sftpClient;
	
	@Value("${sftp.scheduler.transaction.log.folder.local}")
    private String folderLocal;
	
	@Scheduled(cron = "0 0/1 * 1/1 * *")
    public void transactionLogScheduler() {
		scheduleLog.info("Starting Scheduler ...");
		try {
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
	            
	            List<String> listPhoneNumber = transactionRepository.getPhoneNumber(new Timestamp(start.getTime()), new Timestamp(end.getTime()));
	            for (String phoneNumber: listPhoneNumber) {
	            	
	            	String[] listTrxType = new String[]{"IP", "PB", "PSB", "UP", "PA", "GK"};
	            	List<String> trxType = new ArrayList <String>(Arrays.asList(listTrxType));
	    	        
//	            	~!Get Success Transaction!~
	            	List<TransactionLog> successTransaction = transactionRepository.getSuccessTransaction(new Timestamp(start.getTime()), new Timestamp(end.getTime()), phoneNumber, "00");
	            	if (successTransaction.size() > 0) {
	            		for (TransactionLog succesValue: successTransaction) {	            		
		            		LgPayment lgPayment = lgPaymentRepository.findByTransactionId(succesValue.getTransactionId());
		            		String data = writeData(succesValue, lgPayment, start, end);
		            		writer.write(data);
		                	writer.newLine();
		            		if (trxType.contains(succesValue.getTransactionType()) == true) {	            			
		            			trxType.remove(succesValue.getTransactionType());
		            		}
		            	}
	            	}
	            	
//	            	~!Get Cancel Transaction!~
	            	List<TransactionLog> cancelTransaction = transactionRepository.getTransaction(new Timestamp(start.getTime()), new Timestamp(end.getTime()), phoneNumber, "02", trxType);
	            	if (cancelTransaction.size() > 0) {
		            	for (TransactionLog cancelValue: cancelTransaction) {	            		
		            		LgPayment lgPayment = lgPaymentRepository.findByTransactionId(cancelValue.getTransactionId());
		            		String data = writeData(cancelValue, lgPayment, start, end);
		            		writer.write(data);
		                	writer.newLine();
		                	if (trxType.contains(cancelValue.getTransactionType()) == true) {	            			
		            			trxType.remove(cancelValue.getTransactionType());
		            		}
		            	}
	            	}
	            	
//	            	~!Get Fail Transaction!~
	            	List<TransactionLog> failTransaction = transactionRepository.getTransaction(new Timestamp(start.getTime()), new Timestamp(end.getTime()), phoneNumber, "01", trxType);
	            	for (TransactionLog failValue: failTransaction) {	            		
	            		LgPayment lgPayment = lgPaymentRepository.findByTransactionId(failValue.getTransactionId());
	            		String data = writeData(failValue, lgPayment, start, end);
	            		writer.write(data);
	                	writer.newLine();
	            	}
	            	
	            	
	            }
	            writer.close();
            } else {
        	 Path pathSuccess = Paths.get(folderLocal + "/sent/" + file);
             if (!Files.exists(pathSuccess)) { //send to sftp
                 sentToSftpRevass((folderLocal + file), file);
             }
            }
		} catch (Exception e) {
			 e.printStackTrace();
	         System.out.println(e.getMessage());
		}
	}

	public static String writeData (TransactionLog value, LgPayment lgPayment, Date start, Date end) {
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
    	
    	return data;
	}
	
	private void sentToSftpRevass(String source, String destination) {
        
        try {
            sftpClient.connect();
            sftpClient.upload(source, destination);
            sftpClient.disconnect();

            //move to success
            File scr = new File(source);
            File dest = new File(folderLocal + "/sent/" + destination);
            Files.copy(scr.toPath(), dest.toPath());
            
        } catch (Exception er) {
            er.printStackTrace();
            scheduleLog.error(er.getMessage());
        }

    }
}
