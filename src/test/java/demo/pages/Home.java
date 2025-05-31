package demo.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import demo.wrappers.Wrappers;

public class Home {

    public ChromeDriver driver;
    public String pageUrl = "https://qtripdynamic-qa-frontend.vercel.app/";

    public Home(ChromeDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }


    @FindBy(xpath = "//a[text()='Register']")
    public WebElement homepageRegisterButton;

    @FindBy(xpath = "//div[text()='Logout']")
    public WebElement homepageLogoutButton;

    @FindBy(xpath = "//a[text()='Login Here']")
    public WebElement homepageLoginHereButton;

    @FindBy(id = "autocomplete")
    public WebElement homepageSearchField;

    @FindBy(xpath = "//ul[@id='results']/h5[text()='No City found']")
    public WebElement noCityFound;

    @FindBy(xpath = "//ul[@id='results']//li")
    public WebElement automcompleteSearchResult;

    @FindBy(xpath = "//a[text()='Reservations']")
    public WebElement homepageReservationButton;

    public void navigateToHome() {
        if (!driver.getCurrentUrl().equals(pageUrl)) {
            driver.get(pageUrl);
        }
        Wrappers.logInfo("Arrived at homepage");
    }
}

