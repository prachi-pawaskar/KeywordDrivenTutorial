package pawaskar.prachi.KeywordDrivenTutorial.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pawaskar.prachi.KeywordDrivenTutorial.enums.SupportedBrowsers;
import pawaskar.prachi.KeywordDrivenTutorial.enums.SupportedEnv;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import static pawaskar.prachi.KeywordDrivenTutorial.utils.RunTestCaseSteps.testCaseReport;
import static pawaskar.prachi.KeywordDrivenTutorial.utils.UiKeywords.*;


public class Base {

    private static Logger log = LogManager.getLogger(Base.class);

    static String runEnv = SupportedEnv.test.toString();
    static String browser = SupportedBrowsers.chrome.toString();
    public static WebDriver driver = null;

    static Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    //Reports
    public static ExtentReports extent = new ExtentReports();
    static String reportPath = "reports/SparkReport_"+timeFormat.format(timestamp)+".html";
    public static ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);


    static void setDefaults() {
        //Set default env
        try {
            if (System.getenv("runEnv").equalsIgnoreCase("test")){
                runEnv = SupportedEnv.test.toString();
                log.info("Run env: Test");
            } else if (System.getenv("runEnv").equalsIgnoreCase("prod")) {
                runEnv = SupportedEnv.prod.toString();
                log.info("Run env: Prod");
            }
        }catch (Exception e){
            log.info("There was an error in reading env var. Test env is set as default.");
        }
        //Set default browser
        try {
            if (System.getenv("runBrowser").equalsIgnoreCase("chrome")){
                browser = SupportedBrowsers.chrome.toString();
                log.info("Run on Browser: Chrome");
            } else if (System.getenv("runBrowser").equalsIgnoreCase("firefox")) {
                browser = SupportedBrowsers.firefox.toString();
                log.info("Run on Browser: Firefox");
            }
        }catch (Exception e){
            log.info("There was an error in reading browser var. Chrome browser is set as default.");
        }
    }

    static void setUpBrowser(){
        if(browser.equalsIgnoreCase("chrome")){
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeoption=new ChromeOptions();
            chromeoption.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(chromeoption);
            driver.manage().window().maximize();
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }

    @BeforeSuite
    public static void setUp(){
        //Set defaults
        setDefaults();
        //Reports
        spark.config().setDocumentTitle("Testing Keyword driven Automation suite");
        extent.setSystemInfo("Env",runEnv);
        extent.setSystemInfo("Browser",browser);
        extent.attachReporter(spark);
    }

    @AfterSuite
    public static void setDown(){
        //reports
        extent.flush();
    }

    @BeforeMethod
    public static void beforeEachTestCase(){
        //Open browser
        setUpBrowser();
    }

    @AfterMethod
    public static void afterEachTestCase() throws IOException, InterruptedException {
        driver.quit();
    }


    public static String captureScreenshotForReports(WebDriver driver) throws IOException, InterruptedException {

        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = "reports/images/screenshot"+timeFormat.format(timestamp)+".png";
        File finalDestination = new File(destination);
        String absolutePathLocation = finalDestination.getAbsolutePath();
        FileUtils.copyFile(source, finalDestination);

        Thread.sleep(5000);

        return absolutePathLocation;


    }

    public static Object[][] readMasterRunnerSheet(String excelPath) throws IOException {
        XSSFSheet ExcelWSheet;
        XSSFWorkbook ExcelWBook;
        // Open the Excel file
        String resourceFilePath = excelPath;
        String resourceURL = new File(resourceFilePath).getAbsolutePath();
        File Path = new File(resourceURL);
        FileInputStream ExcelFile = new FileInputStream(Path);
        // Access the required test data sheet
        ExcelWBook = new XSSFWorkbook(ExcelFile);
        // Set default sheet to first
        ExcelWSheet = ExcelWBook.getSheetAt(0);
        ArrayList<ArrayList<String>> md = new ArrayList<ArrayList<String>>();
        //Get total rows
        int totalRows = ExcelWSheet.getLastRowNum();
        int totalCols = ExcelWSheet.getRow(0).getPhysicalNumberOfCells();
        Object[] active = new Object[totalCols];
        //Loop till row ends
        //Starting from second row as first is a header
        for (int i = 1; i <= totalRows; i++) {
            //add row wise data to object array
            for (int j = 0; j < totalCols; j++) {
                String temp = ExcelWSheet.getRow(i).getCell(j).toString();
                active[j] = temp;
            }
            ArrayList<String> row = new ArrayList<String>();
            for(int p=0;p<active.length;p++){
                row.add(active[p].toString());
            }
            md.add(row);
        }

        Object[][] data = new Object[md.size()][active.length];
        //initialize data
        int index=0;
        for(ArrayList<String> r : md){
            for(int i=0;i<r.size();i++){
                data[index][i] = r.get(i).toString();
            }
            ++index;
        }

        //Display data object
        log.info("Printing Testrunner master excel sheet");
        for (Object[] r : data)
            log.info(Arrays.toString(r));

        return data;
    }

    public static Object[][] readTestCaseSheet(String excelFilename, String testCaseSheetName) throws IOException {
        //Read the testcase sheet
        String excelPath = "src/test/resources/TestCases/"+excelFilename;
        XSSFSheet ExcelWSheet;
        XSSFWorkbook ExcelWBook;
        // Open the Excel file
        String resourceFilePath = excelPath;
        String resourceURL = new File(resourceFilePath).getAbsolutePath();
        File Path = new File(resourceURL);
        FileInputStream ExcelFile = new FileInputStream(Path);
        // Access the required test data sheet
        ExcelWBook = new XSSFWorkbook(ExcelFile);
        // Set sheet base on name
        ExcelWSheet = ExcelWBook.getSheet(testCaseSheetName);
        ArrayList<ArrayList<String>> md = new ArrayList<ArrayList<String>>();
        //Get total rows
        int totalRows = ExcelWSheet.getLastRowNum();
        int totalCols = ExcelWSheet.getRow(0).getPhysicalNumberOfCells();
        Object[] active = new Object[totalCols];
        //Loop till row ends
        //Starting from second row as first is a header
        for (int i = 1; i <= totalRows; i++) {
            //add row wise data to object array
            for (int j = 0; j < totalCols; j++) {
                String temp = ExcelWSheet.getRow(i).getCell(j).toString();
                active[j] = temp;
            }
            ArrayList<String> row = new ArrayList<String>();
            for(int p=0;p<active.length;p++){
                row.add(active[p].toString());
            }
            md.add(row);
        }

        Object[][] data = new Object[md.size()][active.length];
        //initialize data
        int index=0;
        for(ArrayList<String> r : md){
            for(int i=0;i<r.size();i++){
                data[index][i] = r.get(i).toString();
            }
            ++index;
        }

        //Display data object
        log.info("Printing TestCase excel sheet");
        for (Object[] r : data)
            log.info(Arrays.toString(r));

        return data;
    }

    public static String getValueFromPropertiesFile(String fileType,String lookingForValue) throws IOException {
        String returnedValue = null;
        try {
            String propFile = null;
            if(fileType.equalsIgnoreCase("envRepo")){
                String envFileName = runEnv + "Env.properties";
                propFile = "src/test/resources/EnvRepo/"+envFileName;
            } else if (fileType.equalsIgnoreCase("textRepo")) {
                propFile = "src/test/resources/ObjectRepo/textObjectRepo.properties";
            }else if (fileType.equalsIgnoreCase("uiRepo")) {
                propFile = "src/test/resources/ObjectRepo/uiObjectRepo.properties";
            }
            Properties prop = new Properties();
            InputStream input = new FileInputStream(propFile);
            // load a properties file
            prop.load(input);
            //Retrieve value from prop file
            returnedValue = prop.getProperty(lookingForValue).toString();
        }
        catch (Exception e){
            log.error("Following exception in returning property value: " + e.toString());
        }
        return returnedValue;
    }

    static String decideAndGetValue(String valueType, String value) throws IOException {
        String returnValue = null;
        if(valueType.equalsIgnoreCase("EnvFile")){
            returnValue = getValueFromPropertiesFile("envRepo",value);
        }else if (valueType.equalsIgnoreCase("raw")){
            returnValue = value;
        }

        if(valueType.equalsIgnoreCase("ElementFile")){
            returnValue = getValueFromPropertiesFile("uiRepo",value);
        }else if (valueType.equalsIgnoreCase("raw")){
            returnValue = value;
        }

        if(valueType.equalsIgnoreCase("TextFile")){
            returnValue = getValueFromPropertiesFile("textRepo",value);
        }else if (valueType.equalsIgnoreCase("raw")){
            returnValue = value;
        }

        return returnValue;
    }

    public static void decideAndCallKeyword(
            String keywordName,
            String locatorType,
            String valueType,
            String value,
            String valueToVerifyType,
            String valueToVerify,
            String extraInfo
    ) throws IOException, InterruptedException {
        String keywordParam = decideAndGetValue(valueType,value);

        if(keywordName.equalsIgnoreCase("OpenUrl")){
            OpenUrl(keywordParam);
        }
        if (keywordName.equalsIgnoreCase("Click")){
            Click(locatorType,keywordParam);
        }
        if (keywordName.equalsIgnoreCase("Verify_text")){
            String expectedText = decideAndGetValue(valueToVerifyType,valueToVerify);
            Verify_text(locatorType,keywordParam,expectedText);
        }
        if (keywordName.equalsIgnoreCase("Verify_element")){
            Verify_element(locatorType,keywordParam);
        }
        if (keywordName.equalsIgnoreCase("ClickValidArticles")){
            ClickValidArticles(locatorType,keywordParam,valueToVerifyType,valueToVerify,extraInfo);
        }
        if (keywordName.equalsIgnoreCase("SelectDropDown")){
            String visibleText = decideAndGetValue(valueToVerifyType,valueToVerify);
            SelectDropDown(locatorType,keywordParam,visibleText);
        }
        if (keywordName.equalsIgnoreCase("SendKeys")){
            String addText = decideAndGetValue(valueToVerifyType,valueToVerify);
            SendKeys(locatorType,keywordParam,addText);
        }


    }


}
