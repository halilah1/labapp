package com.mycompany.app;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AppTest
{
	WebDriver driver; 
	WebDriverWait wait; 
	String url = "http://192.168.136.219:80";

    @Before
    public void setUp() { 
		driver = new HtmlUnitDriver(); 
		wait = new WebDriverWait(driver, 10); 
	} 

	@After
    public void tearDown() { 
		driver.quit(); 
	}	 
	
    @Test
    public void testValidSearchTerm() throws InterruptedException { 
        driver.get(url);

        wait.until(ExpectedConditions.titleContains("Home Page")); 

        driver.findElement(By.name("query")).sendKeys("apple");
        driver.findElement(By.tagName("button")).click();

        boolean isResultCorrect = wait.until(ExpectedConditions.textToBe(By.tagName("p"), "Your search term: apple")); 
        assertTrue(isResultCorrect); 
    }

    @Test
    public void testXSSAttack() throws InterruptedException { 
        driver.get(url);

        wait.until(ExpectedConditions.titleContains("Home Page")); 

        driver.findElement(By.name("query")).sendKeys("<script>alert('XSS')</script>");
        driver.findElement(By.tagName("button")).click();

        wait.until(ExpectedConditions.titleContains("Home Page")); 
        boolean isInvalidInputMessageDisplayed = driver.getPageSource().contains("Invalid input detected. Please try again.");
        assertTrue(isInvalidInputMessageDisplayed); 
    }

    @Test
    public void testSQLInjection() throws InterruptedException { 
        driver.get(url);

        wait.until(ExpectedConditions.titleContains("Home Page")); 

        driver.findElement(By.name("query")).sendKeys("' OR 1=1 --");
        driver.findElement(By.tagName("button")).click();

        wait.until(ExpectedConditions.titleContains("Home Page")); 
        boolean isInvalidInputMessageDisplayed = driver.getPageSource().contains("Invalid input detected. Please try again.");
        assertTrue(isInvalidInputMessageDisplayed); 
    }
}