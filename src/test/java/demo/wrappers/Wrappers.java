package demo.wrappers;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.relevantcodes.extentreports.LogStatus;
import demo.utils.Setup;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Wrappers {

    public static void clickOnElement(ChromeDriver driver, WebElement webelem) {
        try {
            webelem.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enterText(ChromeDriver driver, WebElement elem, String text, boolean humanTyping) {
        if (humanTyping) {
            try {
                elem.clear();
                Random random = new Random();

                for (char ch : text.toCharArray()) {
                    elem.sendKeys(Character.toString(ch));

                    // Simulate human typing speed: 100ms to 300ms delay between characters
                    int delay = 50 + random.nextInt(50); // 100 to 300 milliseconds
                    Thread.sleep(delay);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                elem.clear();
                elem.sendKeys(text);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String capture(String filename, ChromeDriver driver) throws IOException {
        String folderPath = Setup.screenshotsFolderPath;
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destination = new File(folderPath + filename);
        FileUtils.copyFile(screenshot, destination);
        return destination.getAbsolutePath();
    }

    public static void logInfo(String msg) {
        Setup.test.log(LogStatus.INFO, msg);
    }

    public static void logSuccess(String msg) {
        Setup.test.log(LogStatus.PASS, msg);
    }

    public static void selectDurationFilter(String filter, WebElement selectElement) {
        Select select = new Select(selectElement);
        select.selectByVisibleText(filter);
    }

    public static boolean verifyDurationFilter(String filter, List<WebElement> adventureCard) {
        if (adventureCard.size() == 0)
            return true;
        else {
            int invalidCount = 0;
            filter = filter.replaceAll("[a-zA-Z+]", "").trim();
            String[] times = filter.split("-");
            if (times.length == 2) {
                int lowerTime = Integer.parseInt(times[0]);
                int upperTime = Integer.parseInt(times[1]);
                for (WebElement card : adventureCard) {
                    String strDuration = card.findElement(By.xpath(".//h5[text()='Duration']/following-sibling::p"))
                            .getText();
                    int resultDuration = Integer.parseInt(strDuration.replaceAll("[^0-9]", "").trim());
                    if ((resultDuration < lowerTime) || (resultDuration > upperTime)) {
                        invalidCount++;
                    }
                }
                return invalidCount == 0;
            } else if (times.length == 1) {
                int upperBound = Integer.parseInt(times[0]);
                for (WebElement card : adventureCard) {
                    String strDuration = card.findElement(By.xpath(".//h5[text()='Duration']/following-sibling::p"))
                            .getText();
                    int resultDuration = Integer.parseInt(strDuration.replaceAll("[^0-9]", "").trim());
                    if (resultDuration < upperBound) {
                        invalidCount++;
                    }
                }
                return invalidCount == 0;
            }
            return invalidCount == 0;
        }
    }

    public static void selectCategoryFilter(String catfilter, WebElement selectElement) {
        Select select = new Select(selectElement);
        select.selectByVisibleText(catfilter);
    }

    public static boolean verifyCategoryFilter(String catfilter, List<WebElement> adventureCard) {
        if (adventureCard.size() == 0) {
            return true;
        }
        catfilter = catfilter.toLowerCase().trim();
        int invalidCount = 0;
        for (WebElement card : adventureCard) {
            String category = card.findElement(By.xpath(".//div[@class='category-banner']")).getText().toLowerCase()
                    .trim();
            if (!catfilter.contains(category)) {
                invalidCount++;
            }
        }
        return invalidCount == 0;
    }

    public static void selectAdventure(String adventureName, List<WebElement> adventureCard, ChromeDriver driver) {
        for (WebElement card : adventureCard) {
            String name = card
                    .findElement(By.xpath(".//div[@class='activity-card-text text-md-center w-100 mt-3']/div[1]/h5"))
                    .getText().trim();
            if(name.equals(adventureName)) {
                clickOnElement(driver, card);
                break;
            }
        }
    }
}
