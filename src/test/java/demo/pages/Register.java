package demo.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import demo.wrappers.Wrappers;

public class Register {
    ChromeDriver driver;
    public String pageUrl = "https://qtripdynamic-qa-frontend.vercel.app/pages/register/";
    public Register(ChromeDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }
        
    @FindBy(name = "email")
    public WebElement emailField;

    @FindBy(name = "password")
    public WebElement passwordField;

    @FindBy(name = "confirmpassword")
    public WebElement confirmpasswordField;

    @FindBy(xpath = "//button[text()='Register Now']")
    public WebElement registerButton;

    public void navigateToRegister() {
        if (!driver.getCurrentUrl().equals(pageUrl)) {
            driver.get(pageUrl);
        }
        Wrappers.logInfo("Arrived at register page");
    }
}
