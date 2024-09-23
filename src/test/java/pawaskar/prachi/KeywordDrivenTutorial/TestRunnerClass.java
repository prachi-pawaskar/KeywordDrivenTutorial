package pawaskar.prachi.KeywordDrivenTutorial;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pawaskar.prachi.KeywordDrivenTutorial.utils.Base;

import java.io.IOException;

import static pawaskar.prachi.KeywordDrivenTutorial.utils.RunTestCaseSteps.runTestCaseSteps;

public class TestRunnerClass extends Base {
    private static Logger log = LogManager.getLogger(TestRunnerClass.class);


    @DataProvider(name="masterTestRun")
    private static Object[][] listOfTestCaseToRun() throws IOException {
        return readMasterRunnerSheet("src/test/resources/TestRunner/MasterTestRunSheet.xlsx");
    }

    @Test(dataProvider = "masterTestRun")
    public void executeTests(String testcaseStatus, String excelFilename,String testCaseSheetName) throws IOException, InterruptedException {
        //Run only if the testcase is active
        if(testcaseStatus.equalsIgnoreCase("active")){
            Object[][] testCaseStep = null;
            testCaseStep = readTestCaseSheet(excelFilename,testCaseSheetName);
            //Run keywords
            runTestCaseSteps(testCaseStep,excelFilename,testCaseSheetName);
        }
    }


}
