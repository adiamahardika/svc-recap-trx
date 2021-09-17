package trilogi.myg.svc.scheduler.transaction.log.service;

import com.jcraft.jsch.*;

import trilogi.myg.svc.scheduler.transaction.log.model.SystemOutProgressMonitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@RefreshScope
@Component
public class SFTPClient {

    private static final Logger scheduleLog = LogManager.getLogger("schedule-log");

    private Session session = null;

    @Value("${sftp.transaction.log.username}")
    private String username;

    @Value("${sftp.transaction.log.host}")
    private String host;

    @Value("${sftp.transaction.log.folder}")
    private String folder;

    public void connect() throws JSchException {
        scheduleLog.info("Connecting to sftp server ...");
        JSch jsch = new JSch();
        scheduleLog.info(username);
        scheduleLog.info(host);
        session = jsch.getSession(username, host);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }

    public void upload(String source, String destination) throws JSchException, SftpException, IOException {
        scheduleLog.info("Sending File ...");
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.put(new FileInputStream(source), folder + destination, new SystemOutProgressMonitor());
        sftpChannel.exit();
    }

    public void download(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.get(source, destination);
        sftpChannel.exit();
    }

    public void disconnect() {
        scheduleLog.info("Disconnecting From SFTP Server...");
        if (session != null) {
            session.disconnect();
        }
    }
}
