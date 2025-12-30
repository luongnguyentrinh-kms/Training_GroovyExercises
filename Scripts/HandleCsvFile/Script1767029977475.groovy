import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import java.time.LocalDate

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable
import services.EmployeeFilterService
import services.TestDataEmployeeCsvService

import org.openqa.selenium.Keys as Keys

// ========================== HANDLE FILE CSV BY TEST DATA IN KATALON =======================
// Read all employees from csv file
def employees = TestDataEmployeeCsvService.readEmployees("Data Files/CSVEmployeeData")
println "Number of employees: ${employees.size()}"
println "Detail employess ${employees}"

// Read the exchange rate from csv file
def rate = TestDataEmployeeCsvService.readExchangeRate("Data Files/CSVExchangeRateData")
println "Rate: ${rate}"

// ========================== FILTER DATA  ==========================
// 1) Salary of Bradley
def bradleySalary = EmployeeFilterService.salaryOfBradley(employees)
println "Bradley's salary is: ${bradleySalary}"

// 2) People with Salary > $400
def salaryGT400 = EmployeeFilterService.salaryGreaterThan(employees, new BigDecimal("400"))
println "Number of people with salary greater than 400 is: ${salaryGT400.size()}"
println "People with salary greater than 400 are: ${salaryGT400}"

// 3) First 3 people with office in Tokyo
def first3InTokyo = EmployeeFilterService.firstMinNumInOffice(employees, "Tokyo", 3)
println "First 3 people with office in Tokyo are: ${first3InTokyo}"

// 4) People <= 40 years old
def ageLT40 = EmployeeFilterService.ageLessThan(employees, 40)
println "People with age <= 40 years old: ${ageLT40}"

// 5) People with the number 3 in their age
def ageHas3 = EmployeeFilterService.ageContainsDigit(employees, "3")
println "People with age has number 3 are: ${ageHas3}"

// 6) People with start date from 1/1/2011 onwards
def startFrom2011 = EmployeeFilterService.startDateFrom(employees, LocalDate.of(2011, 1, 1))
println "People with start date from 1/1/2011 onwards: ${startFrom2011}"

// 7) Position = Accountant OR Software Engineer AND salary < 5 million VND
def posAndSalaryVndLessThan5Mil = EmployeeFilterService.withPositionAndSalaryLessThanVnd(employees, ["Accountant", "Software Engineer"], new BigDecimal("5000000"), rate)
println "People with position as Accountant or Software Engineer and salary < 5 million VND are: ${posAndSalaryVndLessThan5Mil}"