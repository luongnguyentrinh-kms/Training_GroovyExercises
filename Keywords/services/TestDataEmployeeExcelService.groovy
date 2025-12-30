package services

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

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
import utils.DateUtil
import utils.MoneyUtil

public class TestDataEmployeeExcelService {
	static List<Employee> readEmployees(String testDataId){
		TestData data = findTestData(testDataId)
		
		if(data == null) {
			throw new IllegalArgumentException("Employee data not found: " + testDataId)
		}
		
		int rowCount = data.getRowNumbers()
		List<Employee> employees = []
		
		for(int r = 1; r <= rowCount; r++) {
			def name = data.getValue("Name", r)?.toString()?.trim()
			if(!name) continue
			
			 employees << new Employee(
                name      : name,
                position  : data.getValue("Position", r)?.toString()?.trim(),
                office    : data.getValue("Office", r)?.toString()?.trim(),
                age       : data.getValue("Age", r) ? data.getValue("Age", r).toString().trim().toInteger() : null,
                startDate : DateUtil.toLocalDate(data.getValue("Start date", r)),
                salaryUsd : MoneyUtil.parseUsd(data.getValue("Salary", r))
            )
		}
		
		return employees
	}
	
	static ExchangeRate readExchangeRate(String testDataId) {
		TestData data = findTestData(testDataId)
		
		if(data == null) {
			throw new IllegalArgumentException("Exchange rate data not found: " + testDataId)
		}
		
		def vndValue = data.getValue("VND", 1)
		println "VND value: ${vndValue}"
		
		BigDecimal rate = new BigDecimal(vndValue.trim())
		
		return new ExchangeRate(usdToVnd: rate)
	}
}
