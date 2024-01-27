import mock.MockUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yankovyaroslav.ftp.client.FTPClient;
import ru.yankovyaroslav.ftp.service.StudentService;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static org.testng.Assert.*;

public class StudentServiceTest {

    private FTPClient ftpClient;

    private StudentService studentService;

    @BeforeMethod()
    public void setUp() {
        ftpClient = FTPClient.getInstance();
        studentService = StudentService.getInstance();
        MockUtil.connect(ftpClient);
    }

    @AfterMethod
    public void tearDown() {
        ftpClient.resetState();
    }

    @Test(description = "Check if all students are printed")
    public void getAllStudentsTest() {
        MockUtil.uploadFile(ftpClient, "students-mock.json");
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        studentService.getAllStudents();

        assertNotNull(ftpClient.getFileBuffer());

        String consoleOutput = outputStreamCaptor.toString().trim();
        assertTrue(consoleOutput.contains("ID: 433, Name: Abbott Flowers"));
        assertTrue(consoleOutput.contains("ID: 113, Name: Ada Scott"));
        assertTrue(consoleOutput.contains("ID: 177, Name: Addie Kirby"));
        assertTrue(consoleOutput.contains("ID: 17, Name: Young Fulton"));
    }

    @Test(description = "Invalid getAllStudents method work")
    public void getAllStudentsTest_invalid() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        studentService.getAllStudents();

        assertTrue(outputStreamCaptor
                .toString()
                .trim()
                .contains("Ошибка! Вероятно, вы не загрузили файл на сервер.\nПопробуйте снова )))"));
        assertNull(ftpClient.getFileBuffer());
    }

    @Test(description = "Check get student by id",
        dataProvider = "studentsData")
    public void getStudentByIdTest(long studentId, boolean isSuccessful) {
        MockUtil.uploadFile(ftpClient, "students-mock.json");

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        studentService.getStudentById(studentId);

        String consoleOutput = outputStreamCaptor.toString().trim();

        if (isSuccessful) {
            assertTrue(consoleOutput.contains("Студент с id = " + studentId));
        } else {
            assertTrue(consoleOutput.contains("Студента с id = " + studentId + " нет в данном файле"));
        }
    }

    @Test(description = "Check delete student by id",
        dataProvider = "studentsData")
    public void deleteStudentByIdTest(long studentId, boolean isSuccessful) {
        MockUtil.uploadFile(ftpClient, "students-mock.json");

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        studentService.deleteStudentById(studentId);

        String consoleOutput = outputStreamCaptor.toString().trim();

        if (isSuccessful) {
            assertTrue(consoleOutput.contains("Студент с id = " + studentId + " успешно удалён!"));
        } else {
            assertTrue(consoleOutput.contains("Студента с id = " + studentId + " нет в данном файле"));
        }
    }

    @DataProvider(name = "studentsData")
    public Object[][] studentsData() {
        return new Object[][] {
                {0, true},
                {1, true},
                {6, true},
                {17, true},
                {151, true},
                {499, true},

                {-1, false},
                {501, false}
        };
    }
}
