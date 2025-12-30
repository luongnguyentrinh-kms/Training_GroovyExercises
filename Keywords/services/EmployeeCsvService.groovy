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
import utils.CSVUtil

public class EmployeeCsvService {
	static final List<String> headersEmployee =
	["name","position","office","age","startDate","salaryUsd"]
	
	static final List<String> headersExchangeRate = ["USD", "VND"]
	
	static void exportEmployeesToCsv(String filePath, List<Employee> employees) {
		if (employees == null) employees = []
		
//		List<String> headers = []
//		if (!employees.isEmpty()) {
//			headers = employees[0].properties
//				.keySet()
//				.findAll { !(it in ["class", "metaClass"]) }
//				.toList()
//		}
		
		List<Map> rows = employees.collect { e ->
			Map m = [:]
			headersEmployee.each { h ->
				def v = e.properties[h]
				m[h] = (v != null ? v.toString() : null)
			}
			return m
		}

		CSVUtil.writeCsv(filePath, headersEmployee, rows)
	}
	
	static void exportExchangeRateToCsv(String filePath, ExchangeRate rate) {
		if (rate == null) {
			CSVUtil.writeCsv(filePath, headersExchangeRate, [])
			return
		}
		
		Map row = [
            "USD": rate.usd,
            "VND": rate.vnd
        ]

		CSVUtil.writeCsv(filePath, headersExchangeRate, [row])
	}
	
	
}
