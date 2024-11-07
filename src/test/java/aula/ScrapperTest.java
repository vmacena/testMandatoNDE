package aula;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ScrapperTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vini_\\OneDrive\\Área de Trabalho\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-web-security");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testMandatoNDE() {
        driver.get("https://scl.ifsp.edu.br/");

        try {
            WebElement cursosMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='item-263']/a[@href='/index.php/cursos.html']")));
            cursosMenu.click();

            WebElement adsLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"content-section\"]/div/div[1]/table/tbody/tr[14]/td/p/span/a")));
            adsLink.click();

            String originalWindow = driver.getWindowHandle();

            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(originalWindow)) {
                    driver.switchTo().window(handle);
                    break;
                }
            }

            WebElement ndeLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"tab-nde\"]")));
            ndeLink.click();

            WebElement mandatoInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), 'mandato')]")));
            String mandatoTexto = mandatoInfo.getText();

            assertTrue(mandatoTexto.contains("FÁBIO ROBERTO OCTAVIANO (mandato até 31/03/2025)"), "The term of office of the NDE president runs until 03/31/2025.");

            System.out.println("Test passed");

            // driver.close();
            // driver.switchTo().window(originalWindow);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Error during testing.");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}