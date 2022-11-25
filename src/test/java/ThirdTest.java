import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
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
import java.util.Locale;

public class ThirdTest {
    private static WebDriver driver;
    private Logger logger = LogManager.getLogger(ThirdTest.class);
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
    public void testThree() {
        driver.get("https://www.dns-shop.ru/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        String oldWindow = driver.getWindowHandle();

        //На широкоформатном экране скриншоты немного режутся справа. Способа решить эту проблему без js я не нашел
        //Прости меня, господи, что использую js
        Object output = ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio");
        String value = String.valueOf(output);
        float windowDPR = Float.parseFloat(value);

        //Закрыть окно подтверждения города
        cityAgree(wait).click();

        //Сделать скриншот всей страницы (с прокруткой) после загрузки страницы
        for (int i = 0; i < 1000; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,5)", "");
        }

        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR), 100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\31HomePage.png"));
            logger.info("Скриншот сохранен в файле [temp\\31HomePage.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Навести курсор на ссылку (Компьютеры и периферия) ПК, ноутбуки, периферия
        WebElement linkComputers = driver.findElement(By.xpath("//div[@class='menu-desktop']/div[5]"));
        new Actions(driver)
                .moveToElement(linkComputers)
                .perform();

        //Курсор наводится, но блока "ПК, ноутбуки, периферия" на скрине без ожидания не видно
        //Явное ожидание появления блока приводит к исключению NoSuchElement
//        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(
//                "//div[contains (@cllass, 'menu-desktop__submenu_top')]"))));
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Сделать скриншот всей страницы (с прокруткой) после открытия меню
        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR), 100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\32linkComputers.png"));
            logger.info("Скриншот сохранен в файле [temp\\32linkComputers.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Перейти по ссылке Ноутбуки
        new Actions(driver)
                .moveToElement(linkComputers)
                .perform();
        WebElement linkLaptops = driver.findElement(By.xpath("//a[text()='Ноутбуки']"));
        wait.until(ExpectedConditions.elementToBeClickable(linkLaptops));
        new Actions(driver)
                .click(linkLaptops)
                .perform();

        //Сделать скриншот всей страницы (с прокруткой) после загрузки страницы
        for (int i = 0; i < 1000; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,15)", "");
        }

        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR), 100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\33Laptops.png"));
            logger.info("Скриншот сохранен в файле [temp\\33Laptops.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Скрыть блок страницы
        WebElement header = driver.findElement(By.xpath("//header"));
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].parentNode.removeChild(arguments[0])", header);

        //Сделать скриншот всей страницы (с прокруткой) после скрытия блока
        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR), 100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\34NoHeader.png"));
            logger.info("Скриншот сохранен в файле [temp\\34NoHeader.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Выбрать в фильтре Производитель значение ASUS
        WebElement checkBoxAsus = driver.findElement(By.xpath("//span[text()='ASUS  ']"));
        new Actions(driver)
                .scrollToElement(checkBoxAsus)
                .click(checkBoxAsus)
                .perform();

        //Выбрать в фильтре Объем оперативной памяти значение 32 ГБ
        WebElement parametersRam = driver.findElement(By.xpath("//span[text()='Объем оперативной памяти (ГБ)']"));
        new Actions(driver)
                .scrollToElement(parametersRam)
                .click(parametersRam)
                .perform();

        WebElement checkBoxRam = driver.findElement(By.xpath("//span[text()='32 ГБ  ']"));
        new Actions(driver)
                .click(checkBoxRam)
                .perform();

        //Нажать кнопку Применить
        WebElement linkApply = driver.findElement(
                By.xpath("//button[@class='button-ui button-ui_brand left-filters__button']"));
        new Actions(driver)
                .scrollToElement(linkApply)
                .click(linkApply)
                .perform();

        for (int i = 0; i < 1000; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-15)", "");
        }

        //Сделать скриншот всей страницы (с прокруткой) после применения фильтров
        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR), 100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\35Asus32gb.png"));
            logger.info("Скриншот сохранен в файле [temp\\35Asus32gb.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Применить сортировку Сначала дорогие
        WebElement parametersSorting = driver.findElement(By.xpath("//div[@data-id='order']/a"));
        new Actions(driver)
                .scrollToElement(parametersSorting)
                .click(parametersSorting)
                .perform();

        driver.findElement(By.xpath("//div[@class='popover-block popover-block_show']/div[1]/label[2]/span")).click();

        //Сделать скриншот всей страницы (с прокруткой) после применения сортировки
        for (int i = 0; i < 1000; i++) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,5)", "");
        }

        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR), 100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\36Asus32gbExpensive.png"));
            logger.info("Скриншот сохранен в файле [temp\\36Asus32gbExpensive.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Перейти на страницу первого продукта в списке
        //Я не нашел варианта, как кликнуть по элементу с открытием нового окна
        //Я нашел вариант, как добиться того же результата обходным путем:
        WebElement firstLaptop = driver.findElement(By.xpath("//div[@class='catalog-products view-simple']/div[1]/a"));
        firstLaptop.click();

        String currentURL = driver.getCurrentUrl();
        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get(currentURL);
        String newWindow = driver.getWindowHandle();
        driver.manage().window().maximize();

        cityAgree(wait).click();

        //Нет способа явного ожидания полной прогрузки картинок
        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Сделать скриншот всей страницы (с прокруткой) после загрузки страницы
        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(
                            ShootingStrategies.scaling(windowDPR), 100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\37FirstLaptop.png"));
            logger.info("Скриншот сохранен в файле [temp\\37FirstLaptop.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Проверить, что заголовок страницы соответствует ожидаемому
        String actualTitle = driver.findElement(By.xpath("//h1[@class='product-card-top__title']")).getText();

        driver.switchTo().window(oldWindow);
        driver.navigate().back();

        String expectedTitle = driver.findElement(By.xpath(
                "//div[@class='catalog-products view-simple']/div[1]/a/span")).getText().split(" \\[")[0];

        Assertions.assertEquals(expectedTitle, actualTitle);

        logger.info("Заголовок страницы соответствует ожидаемому");

        //Проверить, что в блоке Характеристики заголовок содержит ASUS
        driver.switchTo().window(newWindow);

        WebElement linkAllSpecifications = driver.findElement(By.xpath(
                "//button[contains(@class, 'product-characteristics__expand')]"));
        new Actions(driver)
                .scrollToElement(linkAllSpecifications)
                .click(linkAllSpecifications)
                .perform();

        String actualNameLaptop = driver.findElement(By.xpath(
                "//div[@class='product-characteristics__group']/div[3]/div[2]")).getText();

        Assertions.assertTrue((actualNameLaptop).contains("ASUS"));
        String actualRamLaptop = driver.findElement(By.xpath(
                "//div[@class='product-characteristics']/div[7]/div[3]/div[2]")).getText();

        logger.info("В блоке Характеристики заголовок содержит ASUS");

        //Проверить, что в блоке Характеристики значение Объем оперативной памяти равно 32 ГБ
        Assertions.assertEquals("32 ГБ", actualRamLaptop);

        logger.info("В блоке Характеристики значение Объем оперативной памяти равно 32 ГБ");
    }

    private WebElement cityAgree(WebDriverWait wait) {
        By buttonCityAgreeXpath = By.xpath("//button[contains(@class, 'v-confirm-city__btn')]");
        wait.until(ExpectedConditions.elementToBeClickable(buttonCityAgreeXpath));
        WebElement buttonCityAgree = driver.findElement(buttonCityAgreeXpath);
        return buttonCityAgree;
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен!");
        }
    }

}