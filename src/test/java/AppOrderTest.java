import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTestV() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Васильев");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79098885554");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual =
                driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expected,actual);
    }
    @Test
    void shouldTestRequiredName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79098885554");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual =
                driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected,actual);
    }

    @Test
    void shouldTestRequiredPhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Васильев");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual =
                driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected,actual);
    }
    @Test
    void shouldTestErrorName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("kjhkjhkjh");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79098885554");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual =
                driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected,actual);
    }

    @Test
    void shouldTestErrorPhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Васильев");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("32233222");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual =
                driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected,actual);
    }

    @Test
    void shouldTestErrorCheck() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Васильев");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79098885554");
        driver.findElement(By.className("button")).click();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю " +
                "сделать запрос в бюро кредитных историй";

        String actual =
                driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText().trim();
        assertEquals(expected, actual);
    }
}