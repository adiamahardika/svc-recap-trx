package trilogi.myg.svc.scheduler.transaction.log.model;

import com.jcraft.jsch.SftpProgressMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemOutProgressMonitor implements SftpProgressMonitor {

    private static final Logger scheduleLog = LogManager.getLogger("schedule-log");

    @Override
    public void init(int op, java.lang.String src, java.lang.String dest, long max) {
        scheduleLog.info("STARTING SENT FILE: " + op + " " + src + " -> " + dest + " total: " + max);
    }

    @Override
    public boolean count(long bytes) {
        scheduleLog.info("Size : {}", bytes);
        return(true);
    }

    @Override
    public void end() {
        scheduleLog.info("FINISHED SENT FILE");
    }
}
