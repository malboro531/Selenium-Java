import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class FirstTest {

    private static WebDriver driver;
    private Logger logger = LogManager.getLogger(FirstTest.class);
    String browser = System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT);
    String loadStrategy = System.getProperty("loadstrategy", "normal").toLowerCase(Locale.ROOT);

    @BeforeEach
    public void setUp() {
        logger.info("browser = " + browser);
        logger.info("load strategy = " + loadStrategy);
        driver = WebDriverFactory.getDriver(browser, loadStrategy);
        logger.info("Драйвер стартовал!");
    }

    @Test
    public void testOne() {
        driver.get("https://www.dns-shop.ru/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        logger.info("Открыта страница DNS - " + "https://www.dns-shop.ru/");

        //На широкоформатном экране скриншоты немного режутся справа. Способа решить эту проблему без js я не нашел
        //Прости меня, господи, что использую js
        Object output = ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio");
        String value = String.valueOf(output);
        float windowDPR = Float.parseFloat(value);

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
        By linkCityAgreeXpath = By.xpath("//button[contains(@class, 'v-confirm-city__btn')]");
        wait.until(ExpectedConditions.elementToBeClickable(linkCityAgreeXpath));
        WebElement linkCityAgree = driver.findElement(linkCityAgreeXpath);
        linkCityAgree.click();

        //Скриншот
        for (int i = 0; i < 1000; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,5)", "");
        }

        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(windowDPR),100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\11HomePage.png"));
            logger.info("Скриншот сохранен в файле [temp\\11HomePage.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Перейти по ссылке Бытовая техника
        By linkAppliancesXpath = By.xpath("//div[@class='menu-desktop']/div[1]");
        WebElement linkAppliances = driver.findElement(linkAppliancesXpath);
        new Actions(driver)
                .scrollToElement(linkAppliances)
                .click(linkAppliances)
                .perform();

        //Скриншот
        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(windowDPR),100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\12Appliances.png"));
            logger.info("Скриншот сохранен в файле [temp\\12Appliances.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Проверить, что отображается текст Бытовая техника
        WebElement textAppliances = driver.findElement(By.xpath("//h1[text()='Бытовая техника']"));
        boolean isDisplayed = textAppliances.isDisplayed();
        logger.info("Текст Бытовая техника отображается " + isDisplayed);
        Assertions.assertTrue(isDisplayed, "ERROR! Текст Бытовая техника не отображается!");

        //Перейти по ссылке Техника для кухни
        WebElement linkKitchenAppliances = driver.findElement(By.xpath("//span[text()='Техника для кухни']"));
        new Actions(driver)
                .scrollToElement(linkKitchenAppliances)
                .click(linkKitchenAppliances)
                .perform();

        //Проверить, что отображается текст Техника для кухни
        WebElement textKitchenAppliances = driver.findElement(By.xpath("//h1[text()='Техника для кухни']"));
        isDisplayed = textKitchenAppliances.isDisplayed();
        logger.info("Текст Техника для кухни отображается " + isDisplayed);
        Assertions.assertTrue(isDisplayed, "ERROR! Текст Техника для кухни не отображается!");

        //Проверить, что отображается ссылка Собрать свою кухню
        WebElement linkAssembleKitchen = driver.findElement(By.xpath("//a[text()='Собрать свою кухню']"));
        isDisplayed = linkAssembleKitchen.isDisplayed();
        logger.info("Ссылка Собрать свою кухню отображается " + isDisplayed);
        Assertions.assertTrue(isDisplayed, "ERROR! Ссылка Собрать свою кухню не отображается!");

        //Вывести в логи названия всех категорий
        List<WebElement> kitchenCategories = driver.findElements(By.xpath("//span[@class='subcategory__title']"));
        for (WebElement kitchenCategory : kitchenCategories) {
            logger.info("WebElement: " + kitchenCategory.getText());
        }

        //Проверить, что количество категорий больше 5
        Assertions.assertTrue(kitchenCategories.size() > 5, "ERROR! Количество категорий меньше 5");
        logger.info("Все ОК! Количество категорий больше 5");

        //Скриншот
        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(windowDPR),100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\13KitchenAppliances.png"));
            logger.info("Скриншот сохранен в файле [temp\\13KitchenAppliances.png]");
        } catch (IOException e) {
            e.printStackTrace();
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

