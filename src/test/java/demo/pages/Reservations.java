package demo.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class Reservations {
    ChromeDriver driver;
    public Reservations(ChromeDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }

    @FindBy(id = "no-reservation-banner") 
    public WebElement noReservationBanner;

    @FindBy(xpath = "//th[@scope='row']")
    public WebElement transactionID;

    @FindBy(xpath = "//button[text()='Cancel']")
    public WebElement cancelReservationButton;
}
