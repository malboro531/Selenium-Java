import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverFactory {

    public static WebDriver getDriver(String browserName, String loadStrategy) {

        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--incognito");
                chromeOptions.addArguments("--start-fullscreen");
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.valueOf(loadStrategy.toUpperCase()));
                return new ChromeDriver(chromeOptions);
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-private");
                firefoxOptions.addArguments("--kiosk");
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.valueOf(loadStrategy.toUpperCase()));
                return new FirefoxDriver(firefoxOptions);
            default:
                throw new RuntimeException("Введено некорректное название браузера");
        }
    }
}