package data;

import org.testng.annotations.DataProvider;

public class FtpClientDataSet {

    @DataProvider(name = "validConnectionData")
    public static Object[][] connectionData() {
        return new Object[][]{
                {"ftp.dlptest.com", "dlpuser", "rNrKYTX9g7z3RgJRmxWuGHbeu"},
                {"test.rebex.net", "demo", "password"}
        };
    }

    @DataProvider(name = "uploadFileData")
    public static Object[][] uploadFileData() {
        return new Object[][]{
                {"students-mock.json", true},
                {"mock-students.json", true},
                {"wrong-dir.json", false}
        };
    }

    @DataProvider(name = "downloadFileData")
    public static Object[][] downloadFileData() {
        return new Object[][]{
                {"students-mock.json", true},
                {"mock-students.json", true},
        };
    }
}
