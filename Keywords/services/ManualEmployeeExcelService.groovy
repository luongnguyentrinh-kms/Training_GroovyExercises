package services

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE

import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

import models.Employee
import models.ExchangeRate
import utils.ExcelUtil
import utils.MoneyUtil
import utils.DateUtil

public class ManualEmployeeExcelService {
	// GET DATA FROM employee TAB
	static List<Employee> readEmployees(Workbook wb){
		// Select the employee sheet to get data
		Sheet sheet = wb.getSheet("employee")
		if(sheet == null) throw new IllegalArgumentException("Missing sheet: employee")
		
		// Get the header name with key is index
		Map<String, Integer> h = ExcelUtil.headerIndexMap(sheet, 0)
		println "Map header: ${h}"
		
		List<Employee> employees = []
		println "Last row index: ${sheet.getLastRowNum()}" // It returns 999
		for(int r = 1; r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r)

			if(row == null) continue
			
			def name = ExcelUtil.cellValue(row, h["Name"])?.toString()?.trim()

			if(!name) continue
			
			Employee e = new Employee(
				name: name,
				position: ExcelUtil.cellValue(row, h["Position"])?.toString()?.trim(),
				office: ExcelUtil.cellValue(row, h["Office"]).toString()?.trim(),
				age: (ExcelUtil.cellValue(row, h["Age"]) as Number)?.intValue(),
				startDate: DateUtil.toLocalDate(ExcelUtil.cellValue(row, h["Start date"])),
				salaryUsd: MoneyUtil.parseUsd(ExcelUtil.cellValue(row, h["Salary"]))
			)
			
			employees << e
		}
		
		return employees
	}
	
	// GET DATA FROM exchange rate TAB
	static ExchangeRate readExchangeRate(Workbook wb) {
		Sheet sheet = wb.getSheet("exchange rate")
		
		if(sheet == null) throw new IllegalArgumentException("Missing sheet: exchange rate")
		
		Row row2 = sheet.getRow(1)
		if(row2 == null) throw new IllegalArgumentException("exchange rate sheet missing row 2")
			
		def usdValue = ExcelUtil.cellValue(row2, 0)
		BigDecimal usd = usdValue != null ? new BigDecimal(usdValue.toString()) : null
		
		def vndValue = ExcelUtil.cellValue(row2, 1)
		BigDecimal vnd = vndValue != null ? new BigDecimal(vndValue.toString()) : null

		
		return new ExchangeRate(usd: usd, vnd: vnd)
	}
}
