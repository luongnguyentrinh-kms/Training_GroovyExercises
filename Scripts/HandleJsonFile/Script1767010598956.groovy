import java.nio.file.Paths

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import java.time.LocalDate
import net.minidev.json.JSONUtil
import services.EmployeeFilterService
import services.EmployeeDataService
import utils.ExcelUtil

import org.apache.poi.ss.usermodel.Workbook

import com.kms.katalon.core.configuration.RunConfiguration

import utils.JSONUtil

'PREPARATION'
String dataFolder = Paths.get(RunConfiguration.getProjectDir(), 'DataStorage').toString()
String jsonPath = Paths.get(dataFolder, 'JsonEmployeeData.json').toString()

'Read employees from Json file'
def employees = EmployeeDataService.readEmployeesFromJson(jsonPath)
println "Number of employees: ${employees.size()}"
println "Detail employess ${employees}"

'Read the exchange rate from Json file'
def rate = EmployeeDataService.readExchangeRateFromJson(jsonPath)
println "Rate: ${rate}"

'FILTER DATA AND PRINT THE RESULT'
'1) Salary of Bradley'
def bradleySalary = EmployeeFilterService.salaryOf(employees, [name: 'Bradley Greer'])
println "Bradley's salary is: ${bradleySalary}"

'2) People with Salary > 400'
def salaryGT400 = EmployeeFilterService.getEmployeeBy(employees, [minSalaryUsd: 400])
println "Number of people with salary > 400: ${salaryGT400.size()}"
println "People with salary > 400: ${salaryGT400}"

'3) First 3 people with office in Tokyo'
def first3InTokyo = EmployeeFilterService.getEmployeeBy(employees, [office: 'Tokyo']).take(3)
println "First 3 people with office in Tokyo: ${first3InTokyo}"

'4) People <= 40 years old'
def ageLE40 = EmployeeFilterService.getEmployeeBy(employees, [maxAge: 40])
println "Number of people with age <= 40: ${ageLE40.size()}"
println "People with age <= 40: ${ageLE40}"

'5) People with the number 3 in their age'
def ageHas3 = EmployeeFilterService.getEmployeeBy(employees, [ageContains: '3'])
println "Number of people with age contains '3': ${ageHas3.size()}"
println "People with age contains '3': ${ageHas3}"

'6) People with start date from 1/1/2011 onwards'
def startFrom2011 = EmployeeFilterService.getEmployeeBy(employees, [startDateFrom: LocalDate.of(2011, 1, 1)])
println "Number of people with start date from 2011-01-01: ${startFrom2011.size()}"
println "People with start date from 2011-01-01: ${startFrom2011}"

'7) Position = Accountant OR Software Engineer AND salary < 5 million VND'
def posAndSalaryVndLessThan5Mil = EmployeeFilterService.withPositionAndSalaryLessThanVnd(employees, ['Accountant', 'Software Engineer'], new BigDecimal('5000000'), rate)
println "People with position as Accountant or Software Engineer and salary < 5 million VND are: ${posAndSalaryVndLessThan5Mil}"

'8) Write all data to CSV file'
String csvEmployeesPath = Paths.get(dataFolder, 'CsvEmployeeData.csv').toString()
EmployeeDataService.exportEmployeesToCsv(csvEmployeesPath, employees)
String csvExchangeRatePath = Paths.get(dataFolder, 'CsvExchangeRateData.csv').toString()
EmployeeDataService.exportExchangeRateToCsv(csvExchangeRatePath, rate)

