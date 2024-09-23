package pawaskar.prachi.KeywordDrivenTutorial.utils;

import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

import static pawaskar.prachi.KeywordDrivenTutorial.utils.Base.decideAndCallKeyword;
import static pawaskar.prachi.KeywordDrivenTutorial.utils.Base.extent;


public class RunTestCaseSteps {
    private static Logger log = LogManager.getLogger(RunTestCaseSteps.class);

    public static ExtentTest testCaseReport;

    public static void runTestCaseSteps(Object[][] testCaseStep, String excelFilename, String testCaseSheetName) throws IOException, InterruptedException {
        //Set Testreport
        testCaseReport = extent.createTest("Running testcase: " +testCaseSheetName + " from " + excelFilename);
        //Read object
        log.info("Reading from runTestCaseSteps");
        for (Object[] r : testCaseStep) {
            //Execute only if active step
            if(r[1].toString().equalsIgnoreCase("active")){
                //print test step desc in the report
                testCaseReport.info("Test step desc: " + r[2].toString());
                //perform operation
                decideAndCallKeyword(r[3].toString(),r[4].toString(),r[5].toString()
                        ,r[6].toString(),r[7].toString(),r[8].toString(),r[9].toString());
            }
        }

    }

}
