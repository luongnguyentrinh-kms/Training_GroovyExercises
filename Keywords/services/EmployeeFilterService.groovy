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
import utils.MoneyUtil

import java.time.LocalDate

public class EmployeeFilterService {
	static BigDecimal salaryOfBradley(List<Employee> employees) {
		Employee e = employees.find { it.name?.equals("Bradley Greer") }
		return e?.salaryUsd
	}
	
	static List<Employee> salaryGreaterThan(List<Employee> employees, BigDecimal minUsd){
		return employees.findAll { it.salaryUsd != null && it.salaryUsd > minUsd }
	}
	
	static List<Employee> firstMinNumInOffice(List<Employee> employees, String office, int minNum){
		def list = employees.findAll { it.office?.equals(office) }
		return list.take(minNum)
	}
	
	static List<Employee> ageLessThan(List<Employee> employees, int maxAge){
		return employees.findAll { it.age != null && it.age <= maxAge }
	}
	
	static List<Employee> ageContainsDigit(List<Employee> employees, String digit){
		return employees.findAll { it.age != null && it.age.toString().contains(digit) }
	}
	
	static List<Employee> startDateFrom(List<Employee> employees, LocalDate fromDate){
		return employees.findAll { it.startDate != null && !it.startDate.isBefore(fromDate) }
	}
	
	static List<Employee> withPositionAndSalaryLessThanVnd(List<Employee> employees, List<String> positions, BigDecimal maxVnd, ExchangeRate rate){
		return employees.findAll { e ->
			if(e.position == null || e.salaryUsd == null) return false
			boolean pos = positions.any{p -> e.position.equals(p)}
			if(!pos) return false
			BigDecimal salaryVnd = MoneyUtil.usdToVnd(e.salaryUsd, rate.vnd)
			
			return salaryVnd < maxVnd
		}
	}
}
