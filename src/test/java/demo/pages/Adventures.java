package demo.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class Adventures {
    public ChromeDriver driver;

    public Adventures(ChromeDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }

    @FindBy(xpath = "//div[@id='data']/div")
    public List<WebElement> adventureCard;

    //for duration filters

    @FindBy(id = "duration-select")
    public WebElement durationFilterSelect;

    @FindBy(xpath = "//select[@id='duration-select']/option[not(@disabled)]")
    public List<WebElement> durationFilters;

    @FindBy(xpath = "//select[@id='duration-select']/following-sibling::div")
    public WebElement durationClearButton;

    //for category filters

    @FindBy(id = "category-select")
    public WebElement categoryFilterSelect;

    @FindBy(xpath = "//select[@id='category-select']/option[not(@disabled)]")
    public List<WebElement> categoryFilters;

    @FindBy(xpath = "//select[@id='category-select']/following-sibling::div")
    public WebElement categoryClearButton;
}
