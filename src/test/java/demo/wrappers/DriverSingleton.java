package demo.wrappers;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverSingleton {

    private static ChromeDriver driver;

    public static ChromeDriver getDriverInstance() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            // options.addArguments("--headless");
            driver = new ChromeDriver(options);
        }
        return driver;
    }

    public static void closeDriverInstance() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
