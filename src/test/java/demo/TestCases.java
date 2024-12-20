package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider{ // Lets us read the data
        ChromeDriver driver;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
        */

        @Test(enabled = true)
        public void testCase01(){
                Wrappers wp = new Wrappers(driver);
                
                wp.NavigateToUrl();
                String txt = wp.getAboutMessage();
                System.out.println(txt);

        }

        @Test(enabled = true)
        public void testCase02(){
                Wrappers wp = new Wrappers(driver);
                SoftAssert sa = new SoftAssert();

                wp.NavigateToUrl();
                wp.selectExploreOption("Movies");
                String[] movie = wp.verifyTopSellingMovies();
                System.out.println(movie[0]);
                System.out.println(movie[1]);

                sa.assertTrue(movie[0].contains("U") || movie[0].contains("A") || movie[0].contains("U/A") || movie[0].contains("R"));
                sa.assertTrue(movie[1].contains("Animation") || movie[1].contains("Comedy") || movie[1].contains("Drama"));
                sa.assertAll();

        }

        @Test(enabled = true)
        public void testCase03(){
                
                Wrappers wp = new Wrappers(driver);
                SoftAssert sa = new SoftAssert();

                wp.NavigateToUrl();
                wp.selectExploreOption("Music");
                wp.verifyMusic();
                String title = wp.songTitle;
                int likes = wp.songLikes;
                System.out.println("song Title: "+title);
                System.out.println("Song Likes: "+likes);

                sa.assertTrue(likes <= 50 && likes > 0);
                sa.assertAll();
                
        }

        @Test(enabled = true)
        public void testCase04(){

                Wrappers wp = new Wrappers(driver);
                
                wp.NavigateToUrl();
                wp.selectExploreOption("News");
                wp.verifyLatestNews();

        }


        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }
}