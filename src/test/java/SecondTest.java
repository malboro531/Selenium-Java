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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SecondTest {

    private static WebDriver driver;
    private Logger logger = LogManager.getLogger(SecondTest.class);
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
    public void testTwo() {
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
        By buttonCityAgreeXpath = By.xpath("//button[contains(@class, 'v-confirm-city__btn')]");
        wait.until(ExpectedConditions.elementToBeClickable(buttonCityAgreeXpath));
        WebElement buttonCityAgree = driver.findElement(buttonCityAgreeXpath);
        buttonCityAgree.click();

        //Скриншот
        for (int i = 0; i < 1000; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,5)", "");
        }

        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(windowDPR),100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\21HomePage.png"));
            logger.info("Скриншот сохранен в файле [temp\\21HomePage.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Навести курсор на ссылку Бытовая техника
        WebElement linkAppliances = driver.findElement(By.xpath("//div[@class='menu-desktop']/div[1]"));
        new Actions(driver)
                .moveToElement(linkAppliances)
                .perform();

        //Курсор наводится, но блока "Бытовая техника" на скрине без ожидания не видно
        //Явное ожидание появления блока приводит к исключению NoSuchElement
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Скриншот
        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(windowDPR),100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\22AppliancesCursor.png"));
            logger.info("Скриншот сохранен в файле [temp\\22AppliancesCursor.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Навести курсор на ссылку Бытовая техника повторно (+скролл)
        new Actions(driver)
                .scrollToElement(linkAppliances)
                .moveToElement(linkAppliances)
                .perform();

        //Проверить, что отображаются ссылки: Встраиваемая техника, Техника для кухни, Техника для дома
        By linksSubcategoriesXpath = By.xpath("//a[@class='ui-link menu-desktop__first-level']");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(linksSubcategoriesXpath));
        List<WebElement> linksSubcategories = driver.findElements(linksSubcategoriesXpath);
        List<String> textsSubcategories = linksSubcategories.stream().map(WebElement::getText).collect(Collectors.toList());

        List<String> expectedResultSubcategories = new ArrayList();
        expectedResultSubcategories.add("Встраиваемая техника");
        expectedResultSubcategories.add("Техника для кухни");
        expectedResultSubcategories.add("Техника для дома");

        Assertions.assertEquals(expectedResultSubcategories, textsSubcategories,
                "ERROR!Запрашиваемые категории не найдены");
        logger.info("Все ОК! Ссылки: Встраиваемая техника, Техника для кухни, Техника для дома отображаются");

        //Структура сайта изменена. Блок кода ниже не актуален
        //Навести курсор на ссылку Приготовление пищи
//        WebElement cooking = driver.findElement(By.xpath("//a[text()='Приготовление пищи']"));
//        new Actions(driver)
//                .moveToElement(cooking)
//                .perform();
//
//        Wait.sleep();
//
//        //Проверить, что количество ссылок в подменю Приготовление пищи больше 5
//        List<WebElement> subcategories1 = driver.findElements(By.xpath("//a[@class='ui-link menu-desktop__popup-link']"));
//        Assertions.assertTrue(subcategories1.size() > 5, "ERROR! Количество ссылок меньше 5");

        //Навести курсор на ссылку Плиты
        WebElement linkStove = driver.findElement(By.xpath("//a[text()='Плиты и печи']"));
        new Actions(driver)
                .moveToElement(linkStove)
                .perform();

        //Курсор наводится, но блока "Плиты" на скрине без ожидания не видно
        //Явное ожидание появления блока приводит к исключению NoSuchElement
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR),100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\23linkStoveСursor.png"));
            logger.info("Скриншот сохранен в файле [temp\\23linkStoveСursor.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Перейти по ссылке Плиты
        new Actions(driver)
                .scrollToElement(linkAppliances)
                .moveToElement(linkAppliances)
                .perform();
        wait.until(ExpectedConditions.elementToBeClickable(linkStove));
        new Actions(driver)
                .click(linkStove)
                .perform();

        //Скриншот
        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR),100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\24Stove.png"));
            logger.info("Скриншот сохранен в файле [temp\\24Stove.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Перейти по ссылке Плиты электрические
        WebElement linkElectricStove = driver.findElement(By.xpath("//span[text()='Плиты электрические']"));
        new Actions(driver)
                .scrollToElement(linkElectricStove)
                .click(linkElectricStove)
                .perform();

        //Проверить, что в тексте Плиты электрические [количество] товаров количество товаров больше 100
        WebElement inputElectricStove = driver.findElement(By.xpath("//span[@class='products-count']"));
        String resultElectricStove = inputElectricStove.getText().replaceAll("[^0-9]+", "");
        int sizeElectricStove = Integer.parseInt(resultElectricStove);
        Assertions.assertTrue(sizeElectricStove > 100,
                "ERROR! Количество электрических плит меньше 100");
        logger.info("Все ОК! Количество электрических плит больше 100");

        //Скриншот
        for (int i = 0; i < 1000; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,5)", "");
        }

        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR),100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\25ElectricStove.png"));
            logger.info("Скриншот сохранен в файле [temp\\25ElectricStove.png]");
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
