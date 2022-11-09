import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverFactory {

    public static WebDriver getDriver(String browserName, String strategy) {
        DesiredCapabilities capabilities = getLoadStrategy(strategy);

        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--incognito");
                chromeOptions.addArguments("--kiosk");
                chromeOptions.merge(capabilities);
                return new ChromeDriver(chromeOptions);
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-private");
                firefoxOptions.merge(capabilities);
                return new FirefoxDriver(firefoxOptions);
            default:
                throw new RuntimeException("Введено некорректное название браузера");
        }
    }

    private static DesiredCapabilities getLoadStrategy(String strategy) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        switch (strategy) {
            case "normal":
                capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "normal");
            case "eager":
                capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "eager");
            default:
                capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");
        }

        return capabilities;
    }

}