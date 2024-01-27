import data.FtpClientDataSet;
import mock.MockUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yankovyaroslav.ftp.client.FTPClient;

import java.io.*;
import java.nio.file.NoSuchFileException;

import static org.testng.Assert.*;

public class FtpClientTest {

    private FTPClient ftpClient;

    @BeforeMethod()
    public void setUp() {
        ftpClient = FTPClient.getInstance();
    }

    @AfterMethod
    public void tearDown() {
        ftpClient.resetState();
    }

    @Test(description = "Check valid conn of client with server",
            dataProvider = "validConnectionData", dataProviderClass = FtpClientDataSet.class)
    public void connectionTest(String host, String username, String password) {
        ftpClient.setHost(host);
        ftpClient.setUsername(username);
        ftpClient.setPassword(password);
        ftpClient.connect();
        assertNotNull(ftpClient.getConnectionSocket());
        assertNotNull(ftpClient.getServerReader());
        assertNotNull(ftpClient.getServerWriter());
        assertTrue(ftpClient.isConnected());
    }

    @Test(enabled = false)
    public void invalidConnectionTest() {
        ftpClient.setHost("ftp.dlpuser.com");
        ftpClient.setUsername("dlpuser");
        ftpClient.setPassword("rNrKYTX9g7z3RgJRmxWuGHbeu");

    }

    @Test(description = "Check successfully passive mode activation")
    public void activatePassiveModeTest() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        MockUtil.connect(ftpClient);
        ftpClient.activatePassiveMode();

        assertTrue(outputStreamCaptor.toString().trim().contains("SUCCESSFUL !!! Пассивный режим успешно включен!"));
        assertNotNull(ftpClient.getServerHost());
        assertTrue(ftpClient.getServerPort() > 0);
    }


    @Test(description = "Check successfully file uploading",
            dataProvider = "uploadFileData", dataProviderClass = FtpClientDataSet.class)
    public void uploadFileTest(String filePath, boolean expectedResult)
            throws IOException {
        MockUtil.connect(ftpClient);
        if (!expectedResult) {
            assertThrows(NoSuchFileException.class, () -> ftpClient.uploadFile(filePath));
        } else {
            boolean actualResult = ftpClient.uploadFile(filePath);
            assertTrue(actualResult);
        }
    }

    @Test(description = "Check correct and incorrect file downloading",
        dataProvider = "downloadFileData", dataProviderClass = FtpClientDataSet.class)
    public void downloadFileTest(String fileName, boolean expectedResult) {
        MockUtil.connect(ftpClient);
        MockUtil.uploadFile(ftpClient, fileName);
        boolean actualResult = ftpClient.downloadFile(fileName);
        if (expectedResult) {
            assertNotNull(ftpClient.getFileBuffer());
            assertTrue(actualResult);
        } else {
            assertNull(ftpClient.getFileBuffer());
            assertFalse(actualResult);
        }
    }
}
