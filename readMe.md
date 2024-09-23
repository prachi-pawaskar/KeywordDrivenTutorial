# Keyword Driven Automation Framework Tutorial

The project demonstrates use of Keyword driven automation framework.

## About Keyword driven framework

Keyword driven approach is to create test cases using a sequence of keywords. These keywords are custom-made.
The framework is mainly useful for a project which do not have programming background and can directly use custom keywords to create test cases.


## Getting Started

These instructions will help you get started.

### Prerequisites

1. Java
2. Maven
3. Need MS excel or Libre office type of apps to read and write test cases.

## Tests

### Where to write test cases?

* All test cases are written in excel file format under folder: /test/resources/TestCases/*.xlsx
  - Each excel can hold multiple sheet.
  - Each sheet represents a test case.
* User can create multiple excel files to help segrate the test cases into different modules, sites etc.
* These excel files can have any name.
* Steps can be marked "active", "inactive" to enable and disable the step from execution.

### How to write test cases?

* Above sheet should always have below columns, if any column is not in used the value should hold NA:
    - StepNo: Step serial number
    - Status: active/inactive
    - TestStepDesc: Normal text describing the step working
    - Keyword: This is predefined list:
      - OpenUrl: To navigate to the url
      - Click: To click the element
      - SelectDropDown: Select visible text value from the drop down
      - Verify_text: Verify text on the element
      - Verify_element: Verify element is visible
      - ClickValidArticles: Custom keyword for the application
      - SelectDropDown: Select visible text value from dropdown
      - SendKeys: Add text to element
    - LocatorType: Element locator which can be - class, xpath, id, tagName, name, linkText, partialLinkText, css
    - ValueType: Can be the following:
      - EnvFile: equivalent value is fetched from /test/resources/EnvRepo/{envFileName}.properties
      - ElementFile: equivalent value is fetched from /test/resources/ObjectRepo/uiObjectRepo.properties
      - raw: no equivalent
    - Value:
      - Depending on the "ValueType" this key is used.
      - If ValueType is raw then this value is taken as is.
    - ValueToVerifyType: 
      - TextFile: equivalent value is fetched from /test/resources/ObjectRepo/textObjectRepo.properties
      - raw: no equivalent
    - ValueToVerify
      - Depending on the "ValueType" this key is used.
      - If ValueType is raw then this value is taken as is.
    - ExtraInfo
      - Used for custom keywords

### Which test cases will be run?

* Running status of test cases are maintained in master sheet: /test/resources/TestRunner/MasterTestRunSheet.xlsx
* IMP: This file should have the same name always.
* First sheet of the excel will be read to determine which test cases will be executed.
* Sheet format: Status,ExcelFilename,SheetName
  - Status: active,inactive
  - ExcelFilename: Test sheet name from location /test/resources/TestCases/*.xlsx
  - SheetName: Sheet name to be run from above excel test case file.

    
### How to execute test

* Right click on any on the TestNG xml file and run as testng suite.
* All the active test cases from /test/resources/TestRunner/MasterTestRunSheet.xlsx will be run.


### Reports

Extent reports are generated under /reports folder.
/images hold the screenshots from failed steps
Report name: SparkReport_{Date-Time}.html


