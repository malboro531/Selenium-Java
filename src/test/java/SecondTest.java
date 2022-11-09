import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SecondTest {

    private static WebDriver driver;
    private Logger logger = LogManager.getLogger(SecondTest.class);
    String browser = System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT);
    String strategy = System.getProperty("strategy", "normal").toLowerCase(Locale.ROOT);

    @BeforeEach
    public void setUp() {
        logger.info("browser = " + browser);
        logger.info("strategy = " + strategy);
        driver = WebDriverFactory.getDriver(browser, strategy);
        logger.info("Драйвер стартовал!");
    }

    @Test
    public void testTwo() {
        driver.get("https://www.dns-shop.ru/");
        driver.manage().window().fullscreen();
        driver.manage().timeouts().pageLoadTimeout(6, TimeUnit.SECONDS);
        logger.info("Открыта страница DNS - " + "https://www.dns-shop.ru/");

        // Вывести в логи заголовок страницы
        String title = driver.getTitle();
        logger.info("title - " + title);
        // Вывести в логи текущий URL
        String currentUrl = driver.getCurrentUrl();
        logger.info("current URL - " + currentUrl);
        // Вывести в логи размеры окна браузера
        logger.info(String.format("Ширина окна: %d", driver.manage().window().getSize().getWidth()));
        logger.info(String.format("Высота окна: %d", driver.manage().window().getSize().getHeight()));

        //Закрыть окно подтверждения города
        String cityAgreeXpath = "//button[contains(@class, 'v-confirm-city__btn')]";
        WebElement cityAgree = driver.findElement(By.xpath(cityAgreeXpath));
        cityAgree.click();

        Wait.sleep();

        //Навести курсор на ссылку Бытовая техника
        WebElement appliances = driver.findElement(By.xpath("//a[text()='Бытовая техника']"));
        new Actions(driver)
                .moveToElement(appliances)
                .perform();

        Wait.sleep();

        //Проверить, что отображаются ссылки: Техника для кухни, Техника для дома, Красота и здоровье
        List<WebElement> subcategories = driver.findElements(By.xpath("//a[@class='ui-link menu-desktop__first-level']"));
        for (WebElement subcategory : subcategories) {
            boolean isDisplayed = subcategory.isDisplayed();
            logger.info(subcategory.getText() + " отображается? " + isDisplayed);
            Assertions.assertTrue(isDisplayed, "Не отображается!");
        }

        //Навести курсор на ссылку Приготовление пищи
        WebElement cooking = driver.findElement(By.xpath("//a[text()='Приготовление пищи']"));
        new Actions(driver)
                .moveToElement(cooking)
                .perform();

        Wait.sleep();

        //Проверить, что количество ссылок в подменю Приготовление пищи больше 5
        List<WebElement> subcategories1 = driver.findElements(By.xpath("//a[@class='ui-link menu-desktop__popup-link']"));
        if (subcategories1.size() > 5) {
            logger.info("Количество ссылок больше 5");
        } else {
            throw new RuntimeException("ERROR! Количество ссылок меньше 5");
        }

        //Навести курсор на ссылку Плиты
        WebElement stove = driver.findElement(By.xpath("//a[text()='Плиты']"));
        new Actions(driver)
                .moveToElement(stove)
                .perform();

        Wait.sleep();

        //Перейти по ссылке Плиты
        stove.click();

        //Перейти по ссылке Плиты электрические
        WebElement electricStove = driver.findElement(By.xpath("//span[text()='Плиты электрические']"));
        electricStove.click();

        //Проверить, что в тексте Плиты электрические [количество] товаров количество товаров больше 100
        WebElement stoveHeader = driver.findElement(By.xpath("//span[@class='products-count']"));
        String stoveHeaderString = stoveHeader.getText().replaceAll("[^0-9]+", "");
        int stoveHeaderInt = Integer.parseInt(stoveHeaderString);
        if (stoveHeaderInt > 100) {
            logger.info("Количество электричекских плит больше 100");
        } else {
            throw new RuntimeException("ERROR! Количество электрических плит меньше 100");
        }

    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен!");
        }
    }
}
