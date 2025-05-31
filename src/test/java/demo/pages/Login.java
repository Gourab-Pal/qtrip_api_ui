package demo.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import demo.wrappers.Wrappers;

public class Login {
    ChromeDriver driver;
    public String pageUrl = "https://qtripdynamic-qa-frontend.vercel.app/pages/login/";
    public Login(ChromeDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }

    @FindBy(name = "email")
    public WebElement emailField;

    @FindBy(name = "password")
    public WebElement passwordField;

    @FindBy(xpath = "//button[text()='Login to QTrip']")
    public WebElement loginButton;

    public void navigateToLogin() {
        if (!driver.getCurrentUrl().equals(pageUrl)) {
            driver.get(pageUrl);
        }
        Wrappers.logInfo("Arrived at login page");
    }
}
