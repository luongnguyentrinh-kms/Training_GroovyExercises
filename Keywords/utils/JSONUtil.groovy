package utils

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

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import internal.GlobalVariable

public class JSONUtil {
	static Object readJson(String filePath) {
		File file = new File(filePath)
		
		if(!file.exists()) {
			throw new IllegalArgumentException("JSON file not found: " + filePath)
		}
		return new JsonSlurper().parse(file)
	}
	
	static String toPrettyJson(Object data) {
		return JsonOutput.prettyPrint(JsonOutput.toJson(data))
	}
	
	static void writeJsonToFile(Object data, String filePath) {
		String json = toPrettyJson(data)
		
		File file = new File(filePath)
		file.parentFile?.mkdir()
		
		file.withWriter("UTF-8") { writer ->
			writer << json
		}
	}
}
