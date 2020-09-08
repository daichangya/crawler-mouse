package com.daicy.crawler.webdriver.browser;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.webdriver.browser
 * @date:9/2/20
 */

public class CrossBrowserTest {
    public static WebDriver driver;
    String URL = "https://lambdatest.github.io/sample-todo-app/";
    static String Node = "http://localhost:4444/wd/hub";
    boolean status = false;

    @BeforeClass
    public static void testSetUp() throws MalformedURLException {
        DesiredCapabilities caps = DesiredCapabilities.firefox();

        /* The execution happens on the Selenium Grid with the address mentioned earlier */
        driver = new RemoteWebDriver(new URL(Node), caps);
    }

    @Test
    public void test_ToDo_App() throws InterruptedException {
        driver.navigate().to(URL);
        driver.manage().window().maximize();
        try {
            /* Let's mark done first two items in the list. */
            driver.findElement(By.name("li1")).click();
            driver.findElement(By.name("li2")).click();

            /* Let's add an item in the list. */
            driver.findElement(By.id("sampletodotext")).sendKeys("Yey, Let's add it to list");
            driver.findElement(By.id("addbutton")).click();

            /* Let's check that the item we added is added in the list. */
            String enteredText = driver.findElement(By.xpath("/html/body/div/div/div/ul/li[6]/span")).getText();
            if (enteredText.equals("Yey, Let's add it to list")) {
                status = true;
            }
            System.out.println("Selenium Grid 4 Standalone Testing Is Complete");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}