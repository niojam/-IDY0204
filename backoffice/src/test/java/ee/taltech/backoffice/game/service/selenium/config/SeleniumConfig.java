package ee.taltech.backoffice.game.service.selenium.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SeleniumConfig {

    private final WebDriver driver;

    public static final String ACCESS_TOKEN = "eyJhbGciOiJSUzUxMiJ9.eyJ1c2VySWQiOiItMTAwIiwiYXV0aG9yaXRpZXMiOiJBVVRIT1IifQ.zVNJHvxEckqc2KoNk95ibe1fGNBMzHnchyBJDkfQ11AKpJlaWfWhEviFV6vBGogZ1tmgQehg86oLp5Jah6AC63pvYSY0LaTcEv2mE7T78yeiNnJhe8FYgZBN1Zwe9S9cKP6mKVX6xEmAtShZHrdImjd-xLbdjcfuLpMCx5zJ53pxNRM7OtXoCY-uWTp9SA6lim_4Hru8gxL-LDQMiMd3VvppcWIO0SYV7hmAQqLNJxbdYkkP4ZHJdxWLDw9faXlWPHwHwgMufL7qsAmtBdVslA3PYxoOyyRgkunhnYUjlfeJd1GrTNBeYJs4zusPUEQZ58EhMmJ2ZIRTFUoUy0CAuQ";
    public static final String REFRESH_TOKEN = "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJCQUNLX09GRklDRSIsImV4cCI6MzYxOTAyMDc4OCwidXNlcklkIjoiLTEwMCIsImF1dGhvcml0aWVzIjoiQVVUSE9SIn0.DH9htCaNQt6cHJ3_INT6SqALYYAkwskGdAKYJGPgpVV65vFU0DYevF9PXvhgK3K1HyU1X1Diw4c72HBldcZGzgxrpF6Tqy0Q1BRPtOFQPMluJgBRkL3g26q4Q_WqqqdekbGjQFfTOugZqhBrZ6p83GIHaDJf-R-4jI5SPTNG_1fuY3haNOY39XY35up6ZzhMhpTsDSr85ZAXrzZjVTYjcBdWoh5gGKgezGQhRF3ClNsnMzeyKcT8zZ2_9eWm0ySJ53vCEucudFxiuDdgjuigRVsI5N-A12D7AxMarHddlj1yrMMN6DDEqzoy1uK_C3I5P6DsNmj0p7Zt25nKkzqWlA";
    public static final String APP_URL = "http://localhost:3000";
    public SeleniumConfig() {
        WebDriverManager.chromedriver().setup();
        Cookie accessCookie = new Cookie("kahoot-backahoot-back-office_access-tokenk-office_access-token", ACCESS_TOKEN);
        Cookie refreshCookie = new Cookie("kahoot-back-office_refresh-token", REFRESH_TOKEN);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.get(APP_URL);
        driver.manage().addCookie(refreshCookie);
        driver.manage().addCookie(accessCookie);
    }


    public WebDriver getDriver() {
        return driver;
    }
}
