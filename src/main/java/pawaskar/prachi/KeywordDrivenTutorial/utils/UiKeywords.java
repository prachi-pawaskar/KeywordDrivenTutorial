package pawaskar.prachi.KeywordDrivenTutorial.utils;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.time.Duration;

import static pawaskar.prachi.KeywordDrivenTutorial.utils.Base.*;
import static pawaskar.prachi.KeywordDrivenTutorial.utils.RunTestCaseSteps.testCaseReport;

public class UiKeywords {

    private static Logger log = LogManager.getLogger(UiKeywords.class);

    static WebElement returnElement(String elementType, String rawElementValue){
        WebElement element = null;

        if (elementType.equalsIgnoreCase("class")) {
            element = driver.findElement(By.className(rawElementValue));
        }
        if (elementType.equalsIgnoreCase("xpath")) {
            element = driver.findElement(By.xpath(rawElementValue));

        }
        if (elementType.equalsIgnoreCase("id")) {
            element = driver.findElement(By.id(rawElementValue));
        }
        if (elementType.equalsIgnoreCase("tagName")) {
            element = driver.findElement(By.tagName(rawElementValue));
        }
        if (elementType.equalsIgnoreCase("name")) {
            element = driver.findElement(By.name(rawElementValue));
        }
        if (elementType.equalsIgnoreCase("linkText")) {
            element = driver.findElement(By.linkText(rawElementValue));
        }
        if (elementType.equalsIgnoreCase("partialLinkText")) {
            element = driver.findElement(By.partialLinkText(rawElementValue));
        }
        if (elementType.equalsIgnoreCase("css")) {
            element = driver.findElement(By.cssSelector(rawElementValue));
        }

        return  element;
    }

    //Common internal keywords
    static void waitUntilElementLoads(String elementType, String rawElementValue) throws IOException, InterruptedException {
        WebElement element;
        try{
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(50));
            log.info("Waiting for element: " + rawElementValue + " of type: "+ elementType + " to load.");
            if (elementType.equalsIgnoreCase("class")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(rawElementValue)));
            }
            if (elementType.equalsIgnoreCase("xpath")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rawElementValue)));
            }
            if (elementType.equalsIgnoreCase("id")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(rawElementValue)));
            }
            if (elementType.equalsIgnoreCase("tagName")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(rawElementValue)));
            }
            if (elementType.equalsIgnoreCase("name")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(rawElementValue)));
            }
            if (elementType.equalsIgnoreCase("linkText")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(rawElementValue)));
            }
            if (elementType.equalsIgnoreCase("partialLinkText")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(rawElementValue)));
            }
            if (elementType.equalsIgnoreCase("css")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(rawElementValue)));
            }
        } catch (Exception e){
            log.error(e);
            log.error("Error in executing keyword: waitUntilElementLoads");
            testCaseReport.info("Error in executing keyword: waitUntilElementLoads: "+e);
            String screenshotPath = captureScreenshotForReports(driver);
            testCaseReport.addScreenCaptureFromPath(screenshotPath).info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());


        }
    }

    static String getTextFromElement(String elementType, String rawElementValue) throws IOException, InterruptedException {
        String uiElementText = null;
        WebElement element = null;

        try {
            element = returnElement(elementType,rawElementValue);
            uiElementText = element.getText().trim();
        }catch (Exception e){
            log.error(e);
            log.error("Error in executing keyword: getTextFromElement");
            testCaseReport.info("Error in executing keyword: getTextFromElement: "+e);
            String screenshotPath = captureScreenshotForReports(driver);
            testCaseReport.addScreenCaptureFromPath(screenshotPath).info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }

        return uiElementText;
    }


    //Test Case exposed keywords
    public static void OpenUrl(String urlTogo) throws InterruptedException, IOException {
        try{
            log.info("Navigating to URL: " + urlTogo);
            driver.get(urlTogo);
            testCaseReport.pass("Navigated to URL: " + urlTogo);
        } catch (Exception e){
            log.error(e);
            log.error("Error in executing keyword: OpenUrl");
            String screenshotPath = captureScreenshotForReports(driver);
            testCaseReport.addScreenCaptureFromPath(screenshotPath).info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            testCaseReport.fail("OpenUrl step failed with error: "+e);

            log.fatal("OpenUrl: Important step failed, hence skipping the test case");
            Assert.fail("To Stop the test steps");
        }
        //Waiting for the overlap to go and all elements to load as desired
        Thread.sleep(20000);
    }


    public static void Click(String elementType, String rawElementValue) throws IOException, InterruptedException {
        WebElement element = null;
        //Wait for the element to load before clicking it
        waitUntilElementLoads(elementType,rawElementValue);
        try{
            log.info("Clicking element: " + rawElementValue + " of type: "+ elementType);
            element = returnElement(elementType,rawElementValue);
            JavascriptExecutor executor = (JavascriptExecutor)driver;
            executor.executeScript("arguments[0].click();", element);
            testCaseReport.pass( "Clicked element: " + rawElementValue + " of type: "+ elementType + " successfully");
        } catch (Exception e){
            log.error(e);
            log.error("Error in executing keyword: clickElement");
            String screenshotPath = captureScreenshotForReports(driver);
            testCaseReport.addScreenCaptureFromPath(screenshotPath).info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            testCaseReport.fail("Click step failed with error: "+e);

            log.fatal("Click: Important step failed, hence skipping the test case");
            Assert.fail("To Stop the test steps");
        }
        //Wait after click action
        Thread.sleep(2000);
    }

    public static void NavigateUrl(String direction){
        if(direction.equalsIgnoreCase("back")){
            driver.navigate().back();
        } else if (direction.equalsIgnoreCase("forward")) {
            driver.navigate().forward();
        }else if (direction.equalsIgnoreCase("refresh")) {
            driver.navigate().refresh();
        }
    }

    public static void SelectDropDown(String elementType, String rawElementValue,String visibleText) throws IOException, InterruptedException {
        WebElement element = null;
        //Wait for the element to load before clicking it
        waitUntilElementLoads(elementType,rawElementValue);
        try{
            log.info("Selecting value "+ visibleText+" from: " + rawElementValue + " of type: "+ elementType);

            element = returnElement(elementType,rawElementValue);

            Select dropDown = new Select(element);
            dropDown.selectByVisibleText(visibleText);

            testCaseReport.pass("Selected value "+ visibleText+" from: " + rawElementValue + " of type: "+ elementType);
        } catch (Exception e){
            log.error(e);
            log.error("Error in executing keyword: SelectDropDown");
            String screenshotPath = captureScreenshotForReports(driver);
            testCaseReport.addScreenCaptureFromPath(screenshotPath).info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            testCaseReport.fail("Selecting from drop down failed with error: "+e);

            log.fatal("SelectDropDown: Important step failed, hence skipping the test case");
            Assert.fail("To Stop the test steps");
        }
    }

    public static void SendKeys(String elementType, String rawElementValue,String addText) throws IOException, InterruptedException {
        //Wait for the element to load before checking it
        waitUntilElementLoads(elementType,rawElementValue);
        WebElement element = null;
        try{
            log.info("Adding value "+ addText+" to: " + rawElementValue + " of type: "+ elementType);
            element = returnElement(elementType,rawElementValue);

            element.sendKeys(addText);
            testCaseReport.pass("Added value "+ addText+" to: " + rawElementValue + " of type: "+ elementType);

        }catch (Exception e){
            log.error(e);
            log.error("Error in executing keyword: SendKeys");
            String screenshotPath = captureScreenshotForReports(driver);
            testCaseReport.addScreenCaptureFromPath(screenshotPath).info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            testCaseReport.fail("SendKeys step failed with error: "+e);

        }
    }

    public static void Verify_text(String elementType, String rawElementValue,String expectedText) throws IOException, InterruptedException {
        //Wait for the element to load before checking it
        waitUntilElementLoads(elementType,rawElementValue);

        String actualText = null;

        try{
            log.info("Equal text check from element: " + rawElementValue + " of type: "+ elementType);
            actualText = getTextFromElement(elementType,rawElementValue);
            log.info("Actual text: " + actualText);
            log.info("Expected text: " + expectedText);
            if(actualText.equals(expectedText)){
                log.info("\""+expectedText + "\" is equal to \"" + actualText + "\" from element: " + rawElementValue);
                testCaseReport.pass("\""+expectedText + "\" is equal to \"" + actualText + "\" from element: " + rawElementValue);
            }else {
                throw new CustomException("Verify_text condition failed.");
            }
        }catch (Exception e){
            log.error(e);
            log.error("Error in executing keyword: Verify_text");
            String screenshotPath = captureScreenshotForReports(driver);
            testCaseReport.addScreenCaptureFromPath(screenshotPath).info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            testCaseReport.fail("Verify_text step failed with error: "+e + " \"\n"+expectedText + "\" is NOT equal to \"" + actualText + "\" from element: " + rawElementValue);
        }
    }

    public static void Verify_element(String elementType, String rawElementValue) throws IOException, InterruptedException {
        //Wait for the element to load before checking it
        waitUntilElementLoads(elementType,rawElementValue);
        Boolean elementIsVisible = false;
        //validate element is visible
        try {
            log.info("Verify element: " + rawElementValue + " of type: "+ elementType + " is visible or not.");
            elementIsVisible = returnElement(elementType,rawElementValue).isDisplayed();
            if(elementIsVisible.equals(true)){
                testCaseReport.pass("\""+rawElementValue + "\" is visible");
            }else {
                throw new CustomException("Verify_element condition failed.");
            }
        }catch (Exception e){
            log.error(e);
            log.error("Error in executing keyword: clickElement");
            String screenshotPath = captureScreenshotForReports(driver);
            testCaseReport.addScreenCaptureFromPath(screenshotPath).info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            testCaseReport.fail("Verify_element step failed with error: "+e+" \"\n"+rawElementValue + "\" is NOT visible");
        }

    }


    //Custom keyword creation example
    public static void ClickValidArticles(String elementType, String rawElementValue,String valueToVerifyType, String valueToVerify,
                                          String extraInfo) throws IOException, InterruptedException {

        //Todo -- add xpath check, only xpath should be allowed for block
        //todo -- add random selection
        //This method will loop by clicking on valid articles
        //Extract rows and columns
        String[] articleMetrix = valueToVerifyType.split("|");
        int maxRows = Integer.parseInt(articleMetrix[0]);
        int maxColumns = Integer.parseInt(articleMetrix[2]);
        int validArticleToBeClicked = (int)Double.parseDouble(valueToVerify);

        //Loop till valueToVerify
        int validArticleClickedCount = 0;


        try {

            for (int row = 1; row <= maxRows; row++) {
                for (int col = 1; col <= maxColumns; col++) {

                    if (validArticleClickedCount == validArticleToBeClicked) {
                        //If valid clicked is already done, then don't do anything and break the loop
                        break;
                    } else {
                        //click article
                        String articleXpath = rawElementValue.replace("rows", String.valueOf(row))
                                .replace("columns", String.valueOf(col));
                        log.info("Clicking on article: row> " + row + ", col> " +col);
                        Click("xpath", articleXpath);
                        log.info("Clicked on article: row> " + row + ", col> " +col);
                        //check the article is valid and not needing sub
                        if (driver.getPageSource().contains(getValueFromPropertiesFile("textRepo", extraInfo))) {
                            //This is not a valid so no counter of article to be increased
                            log.info("Article is subscribed only, not a valid click");
                        } else if (driver.getPageSource().contains("You have reached your article limit")) {
                            throw new CustomException("Article reached limit before expected count");
                        } else {
                            log.info("Article is open, a valid click");
                            //Increase the counter of valid articles clicked
                            validArticleClickedCount++;
                            log.info("Valid click count: " +validArticleClickedCount);
                        }
                        //navigate back to main page to have a refresh click
                        NavigateUrl("back");
                    }
                }
                testCaseReport.pass("\""+valueToVerify + "\" valid articles were clicked");
            }
        }catch (Exception e){
            log.error(e);
            log.error("Error in executing keyword: ClickValidArticles");
            String screenshotPath = captureScreenshotForReports(driver);
            testCaseReport.addScreenCaptureFromPath(screenshotPath).info(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            testCaseReport.fail("ClickValidArticles step failed with error: "+e);

            log.fatal("ClickValidArticles: Important step failed, hence skipping the test case");
            Assert.fail("To Stop the test steps");
        }
    }

}
