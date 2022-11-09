import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FirstTest {

    private static WebDriver driver;
    private Logger logger = LogManager.getLogger(FirstTest.class);
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
    public void testOne() {
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
        WebElement cityAgree = driver.findElement(By.xpath("//button[contains(@class, 'v-confirm-city__btn')]"));
        cityAgree.click();

        Wait.sleep();

        //Перейти по ссылке Бытовая техника
        WebElement appliances = driver.findElement(By.xpath("//a[text()='Бытовая техника']"));
        appliances.click();

        //Проверить, что отображается текст Бытовая техника
        WebElement appliancesHeader = driver.findElement(By.xpath("//h1[text()='Бытовая техника']"));
        boolean isDisplayed = appliancesHeader.isDisplayed();
        logger.info("Текст Бытовая техника отображается " + isDisplayed);
        Assertions.assertTrue(isDisplayed, "Текст Бытовая техника не отображается!");

        //Перейти по ссылке Техника для кухни
        WebElement kitchenAppliances = driver.findElement(By.xpath("//span[text()='Техника для кухни']"));
        kitchenAppliances.click();

        //Проверить, что отображается текст Техника для кухни
        WebElement kitchenAppliancesHeader = driver.findElement(By.xpath("//h1[text()='Техника для кухни']"));
        isDisplayed = kitchenAppliancesHeader.isDisplayed();
        logger.info("Текст Техника для кухни отображается " + isDisplayed);
        Assertions.assertTrue(isDisplayed, "Текст Техника для кухни не отображается!");

        //Проверить, что отображается ссылка Собрать свою кухню
        WebElement assembleKitchen = driver.findElement(By.xpath("//a[text()='Собрать свою кухню']"));
        isDisplayed = assembleKitchen.isDisplayed();
        logger.info("Ссылка Собрать свою кухню отображается " + isDisplayed);
        Assertions.assertTrue(isDisplayed, "Ссылка Собрать свою кухню не отображается!");

        //Вывести в логи названия всех категорий
        List<WebElement> categories = driver.findElements(By.xpath("//span[@class='subcategory__title']"));
        for (WebElement category : categories) {
            logger.info("WebElement: " + category.getText());
        }

        //Проверить, что количество категорий больше 5
        if (categories.size() > 5) {
            logger.info("Количество категорий больше 5");
        } else {
            throw new RuntimeException("ERROR! Количество категорий меньше 5");
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

