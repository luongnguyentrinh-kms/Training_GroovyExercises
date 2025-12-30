package services

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import java.time.LocalDate

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
import utils.JSONUtil

public class EmployeeJsonService {
	static List<Employee> readEmployees(String jsonPath){
		def root = JSONUtil.readJson(jsonPath)
		
		def list = root?.employees
		if (!(list instanceof List)) {
            throw new IllegalArgumentException("Field 'employees' must be a List")
        }
		
		return list.collect { m -> 
			new Employee(
					name: m.name,
					position: m.position,
					office: m.office,
					age: m.age as Integer,
					startDate : m.startDate ? LocalDate.parse(m.startDate.toString()) : null,
					salaryUsd : m.salaryUsd != null ? new BigDecimal(m.salaryUsd.toString()) : null
				)
		}
	}
	
	static BigDecimal readUsdToVndRate(String jsonPath) {
		def root = JSONUtil.readJson(jsonPath)
		def rate = root?.rate?.usdToVnd
		if(rate == null) {
			throw new IllegalArgumentException("Cannot find rate.usdToVnd in JSON")
		}
		
		return new BigDecimal(rate.toString())
	}
}
