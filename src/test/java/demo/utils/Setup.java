package demo.utils;

import java.lang.reflect.Method;
import java.time.Duration;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import demo.wrappers.DriverSingleton;

public class Setup {

    public static ChromeDriver driver;
    public static ExtentReports reports;
    public static ExtentTest test;

    public static String resourcesFolderPath = System.getProperty("user.dir") + "\\src\\test\\resources\\";
    public static String extentReportFileName = resourcesFolderPath + "qtrip_reports.html";
    public static String screenshotsFolderPath = System.getProperty("user.dir") + "\\src\\test\\screenshots\\";

    @BeforeMethod(alwaysRun = true)
    public void init(Method method) {
        // Initialize ExtentReports if not already done
        if (reports == null) {
            reports = new ExtentReports(extentReportFileName, true);
        }

        // Start a new test with method name
        test = reports.startTest(method.getName());

        // Try to initialize WebDriver only for functional tests
        try {
            driver = DriverSingleton.getDriverInstance();
            if (driver != null) {
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            }
        } catch (Exception e) {
            // Log or ignore if driver isn't needed for this test
            System.out.println("Driver initialization skipped: " + e.getMessage());
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        if (reports != null && test != null) {
            reports.endTest(test);
            reports.flush();
        }

        if (driver != null) {
            DriverSingleton.closeDriverInstance();
        }
    }
}
