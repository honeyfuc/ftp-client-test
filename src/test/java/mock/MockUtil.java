package mock;

import ru.yankovyaroslav.ftp.client.FTPClient;
import ru.yankovyaroslav.ftp.domain.ServerMode;

import java.io.IOException;

public class MockUtil {

    private static final String SERVER_HOST = "ftp.dlptest.com";

    private static final String SERVER_USERNAME = "dlpuser";

    private static final String SERVER_PASSWORD = "rNrKYTX9g7z3RgJRmxWuGHbeu";
    public static void connect(FTPClient ftpClient) {
        ftpClient.setHost(SERVER_HOST);
        ftpClient.setUsername(SERVER_USERNAME);
        ftpClient.setPassword(SERVER_PASSWORD);
        ftpClient.connect();
        ftpClient.setServerMode(ServerMode.PASSIVE);
    }

    public static void uploadFile(FTPClient ftpClient, String file) {
        try {
            ftpClient.uploadFile(file);
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
