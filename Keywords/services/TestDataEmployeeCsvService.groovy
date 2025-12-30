package services

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

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

public class TestDataEmployeeCsvService {
	static List<Employee> readEmployees(String testDataId){
		TestData data = findTestData(testDataId)
		
		if (data == null) {
			throw new IllegalArgumentException("CSV TestData not found: " + testDataId)
		}
		
		List<Employee> employees = []
		int rows = data.getRowNumbers()
		
		for (int r = 1; r <= rows; r++) {
			def name = data.getValue("name", r)?.trim()
			if (!name) continue

			employees << new Employee(
				name      : name,
				position  : data.getValue("position", r)?.trim(),
				office    : data.getValue("office", r)?.trim(),
				age       : data.getValue("age", r)?.toInteger(),
				startDate : DateUtil.toLocalDate(data.getValue("startDate", r)),
				salaryUsd : MoneyUtil.parseUsd(data.getValue("salaryUsd", r))
			)
		}

		return employees
	}
	
	static ExchangeRate readExchangeRate(String testDataId) {
		TestData data = findTestData(testDataId)
		
		if(data == null) {
			throw new IllegalArgumentException("Exchange rate data not found: " + testDataId)
		}
		
		def usdValue = data.getValue("USD", 1)
		println "USD value: ${usdValue}"
		BigDecimal usd = new BigDecimal(usdValue.trim())
		
		def vndValue = data.getValue("VND", 1)
		println "VND value: ${vndValue}"
		BigDecimal vnd = new BigDecimal(vndValue.trim())
		
		return new ExchangeRate(usd: usd, vnd: vnd)
	}
}
