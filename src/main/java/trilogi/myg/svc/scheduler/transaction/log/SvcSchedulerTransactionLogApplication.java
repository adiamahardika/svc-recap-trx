package trilogi.myg.svc.scheduler.transaction.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SvcSchedulerTransactionLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SvcSchedulerTransactionLogApplication.class, args);
	}

}
