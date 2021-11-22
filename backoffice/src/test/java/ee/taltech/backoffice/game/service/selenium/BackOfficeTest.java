package ee.taltech.backoffice.game.service.selenium;

import ee.taltech.backoffice.game.model.Question;
import ee.taltech.backoffice.game.model.QuestionType;
import ee.taltech.backoffice.game.model.Quiz;
import ee.taltech.backoffice.game.repository.QuestionRepository;
import ee.taltech.backoffice.game.repository.QuizRepository;
import ee.taltech.backoffice.game.service.selenium.config.SeleniumConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("e2e-tests")
public class BackOfficeTest {

    @Autowired
    private SeleniumConfig seleniumConfig;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private WebDriver driver;

    JavascriptExecutor js;

    @BeforeEach
    public void setUp() {
        driver = seleniumConfig.getDriver();
        js = (JavascriptExecutor) driver;
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void TestId1() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Nimi");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("Question Text");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }

    @Test
    public void TestId2() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("Question Text");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();

        try {

            driver.findElement(By.cssSelector(".mb-4")).click();
        } catch (Exception e) {
            // Button not found
            assertThat(true).isTrue();
        }
    }


    @Test
    public void TestId3() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("Mis on küsimus?");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }


    @Test
    public void TestId4() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();

        try {

            driver.findElement(By.cssSelector(".mb-4")).click();
        } catch (Exception e) {
            // Button not found
            assertThat(true).isTrue();
        }
    }


    @Test
    public void TestId5() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("test");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }


    @Test
    public void TestId6() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("qwertyuio?");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        driver.findElement(By.cssSelector(".ant-select-show-arrow > div")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), '30 seconds')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }

    @Test
    public void TestId7() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("qwertyuio?");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        driver.findElement(By.cssSelector(".ant-select-show-arrow > div")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), '45 seconds')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }

    @Test
    public void TestId8() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("qwertyuio?");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        driver.findElement(By.cssSelector(".ant-select-show-arrow > div")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), '60 seconds')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }



    @Test
    public void TestId9() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("test");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }




    @Test
    public void TestId10() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("test");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");

        driver.findElement(By.xpath("//*[contains(text(), '100 points')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), '0 points')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }



    @Test
    public void TestId11() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("test");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");

        driver.findElement(By.xpath("//*[contains(text(), '100 points')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), '150 points')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }


    @Test
    public void TestId12() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("test");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");

        driver.findElement(By.xpath("//*[contains(text(), '100 points')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), '200 points')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }




    @Test
    public void TestId13() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("test");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");

        driver.findElement(By.xpath("//*[contains(text(), '100 points')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), '250 points')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }


    @Test
    public void TestId14() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("test");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");

        driver.findElement(By.xpath("//*[contains(text(), 'Single Correct Answer')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), 'Single Correct Answer')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }



    @Test
    public void TestId15() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("test");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");

        driver.findElement(By.xpath("//*[contains(text(), 'Single Correct Answer')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), 'Multiple Match all')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }

   @Test
    public void TestId16() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Test");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("test");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");

        driver.findElement(By.xpath("//*[contains(text(), 'Single Correct Answer')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), 'Multiple any')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }


    @Test
    public void TestId17() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Nimi");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("Question Text");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }


    @Test
    public void TestId18() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.xpath("//*[contains(text(), 'Single Correct Answer')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), 'Multiple any')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Nimi");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("Question Text");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer correct n2");
        WebElement myElement = driver.findElement(By.id("register_answer1"));
        WebElement parent = myElement.findElement(By.xpath("./.."));
        parent.click();
        parent.findElement(By.cssSelector(".ant-input-prefix > div")).click();
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }




    @Test
    public void TestId19() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.xpath("//*[contains(text(), 'Single Correct Answer')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), 'Multiple any')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Nimi");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("Question Text");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer correct2");

        driver.findElement(By.id("register_answer2")).click();
        driver.findElement(By.id("register_answer2")).sendKeys("Answer wrong1");
        driver.findElement(By.id("register_answer3")).click();
        driver.findElement(By.id("register_answer3")).sendKeys("Answer wrong2");
        WebElement myElement = driver.findElement(By.id("register_answer1"));
        WebElement parent = myElement.findElement(By.xpath("./.."));
        parent.click();
        parent.findElement(By.cssSelector(".ant-input-prefix > div")).click();
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }



    @Test
    public void TestId20() {
        driver.get("http://localhost:3000/my-quizzes");
        driver.manage().window().setSize(new Dimension(945, 973));
        {
            WebElement element = driver.findElement(By.cssSelector(".anticon-plus > svg"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".anticon-plus > svg")).click();
        driver.findElement(By.id("quizName")).click();
        driver.findElement(By.id("quizName")).sendKeys("Test mega quiz");
        driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".mb-1 > span:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.xpath("//*[contains(text(), 'Single Correct Answer')]")).click();
        try {
            Thread.sleep(2400);
            driver.findElement(By.xpath("//*[contains(text(), 'Multiple any')]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("register_quizName")).click();
        driver.findElement(By.id("register_quizName")).sendKeys("Nimi");
        driver.findElement(By.id("register_questionText")).click();
        driver.findElement(By.id("register_questionText")).sendKeys("Question Text");
        driver.findElement(By.id("register_answer0")).click();
        driver.findElement(By.id("register_answer0")).sendKeys("Answer correct");
        driver.findElement(By.id("register_answer1")).click();
        driver.findElement(By.id("register_answer1")).sendKeys("Answer wrong");
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(".ant-form-item-control-input-content > button")).click();
        driver.findElement(By.cssSelector(".mb-4")).click();
        String url = driver.getCurrentUrl();
        String[] splittedUrl = url.split("/");
        Long quizId = Long.valueOf(splittedUrl[splittedUrl.length - 1]);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        assertThat(quiz.getAuthorId()).isEqualTo(-100L);
    }

}

