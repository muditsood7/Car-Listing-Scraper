package scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {

        Document doc;

        try {
            doc = Jsoup.connect("https://www.kijijiautos.ca/cars/ferrari/")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .header("Accept-Language", "*")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements listings = doc.select("article");

        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:/Users/mudit/Downloads/random shit/chromedriver-win64/chromedriver-win64/chromedriver.exe");

        // Set the path to the Chrome binary
        String chromeBinaryPath = "C:/Program Files/Google/Chrome/Application/chrome.exe"; // Replace with the actual path

        // Instantiate ChromeOptions and set the binary path
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        options.addArguments("--disable-automation");
        options.addArguments("--disable-blink-features=AutomationControlled");

        options.setBinary(chromeBinaryPath);

        // Instantiate a ChromeDriver object with ChromeOptions
        WebDriver driver = new ChromeDriver(options);

        // Open the webpage
        driver.get("https://www.kijijiautos.ca/cars/ferrari/");

        // Wait for the article elements to be present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        List<WebElement> articleElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("article")));

        // Initialize list to store scraped data
        ArrayList<KijijiListing> kijijiListings = new ArrayList<>();

        // Iterate over article elements and extract data
        for (int i = 0; i < Math.min(articleElements.size(), listings.size()); i++) {
            WebElement article = articleElements.get(i);
            Element listing = listings.get(i);
            KijijiListing kijijiListing = new KijijiListing();

            // Extracting data of interest using Selenium
            WebElement anchor = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.bcNN7t")));
            String url = anchor.getAttribute("href");
            kijijiListing.setUrl(url);

            // Extracting name data
            kijijiListing.setName(article.findElement(By.cssSelector("h2.G2jAym")).getText());

            // Extracting price data
            String price = listing.selectFirst("span.G2jAym.d3uM7V.C2jAym.p2jAym.b2jAym").text();
            kijijiListing.setPrice(price);

            kijijiListings.add(kijijiListing);
        }

        kijijiListings.toString();
        // System.out.println(kijijiListings);

        // Close the browser

        // Scraping AutoTrader.ca

        Document doc2;

        try {
            doc2 = Jsoup.connect("https://www.autotrader.ca/cars/ferrari/on")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .header("Accept-Language", "*")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements listings2 = doc2.select("div.result-item");

        // Instantiate a ChromeDriver object with ChromeOptions
        WebDriver driver2 = new ChromeDriver(options);

        // Open the webpage
        driver2.get("https://www.autotrader.ca/cars/ferrari/on");
        List<WebElement> articleElements2 = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("result-item")));

        // Create list for AutoTrader Listings
        ArrayList<AutoTraderListing> autoTraderListings = new ArrayList<>();

        for (int i = 0; i < Math.min(articleElements2.size(), listings2.size()); i++) {
            WebElement article2 = articleElements2.get(i);
            Element listing2 = listings2.get(i);
            AutoTraderListing autoTraderListing = new AutoTraderListing();

            // Extracting url
            WebElement anchor = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.detail-price-area")));
            String url = anchor.getAttribute("href");
            autoTraderListing.setUrl(url);

            // Extracting name data
            autoTraderListing.setName(article2.findElement(By.cssSelector("span.title-with-trim")).getText());

            // Extracting price data
            String price = listing2.selectFirst("span.price-amount").text();
            autoTraderListing.setPrice(price);

            autoTraderListings.add(autoTraderListing);
        }

        autoTraderListings.toString();
        System.out.println(autoTraderListings);

        driver.quit();
        driver2.quit();

    }
}
