package demo.listeners;

import java.io.IOException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.relevantcodes.extentreports.LogStatus;

import demo.utils.Setup;
import demo.wrappers.Wrappers;

public class MyTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Setup.test.log(LogStatus.INFO, "Executing: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Setup.test.log(LogStatus.PASS, "Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getName().contains("ui")) {
            ChromeDriver driver = Setup.driver;
            long timestamp = System.currentTimeMillis();
            String fileName = result.getName() + "_" + timestamp + ".jpg";
            try {
                Setup.test.log(LogStatus.FAIL,
                        Setup.test.addScreenCapture(Wrappers.capture(fileName, driver)) + "Failed: "
                                + result.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Setup.test.log(LogStatus.FAIL, "Failed: " + result.getName());
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Setup.test.log(LogStatus.SKIP, "Skipped: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}
