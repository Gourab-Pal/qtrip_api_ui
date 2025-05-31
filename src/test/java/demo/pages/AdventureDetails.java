package demo.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class AdventureDetails {
    ChromeDriver driver;
    public AdventureDetails(ChromeDriver driver) { 
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }

    @FindBy(id = "adventure-name")
    public WebElement adventureName;

    @FindBy(css = "input[name='name']")
    public WebElement nameField;

    @FindBy(css = "input[name='date']")
    public WebElement dateField; 
    
    @FindBy(css = "input[name='person']")
    public WebElement personField;  

    @FindBy(xpath = "//button[text()='Reserve']")
    public WebElement reserveButton;

    @FindBy(id = "reserved-banner")
    public WebElement reservationStatus;
}
