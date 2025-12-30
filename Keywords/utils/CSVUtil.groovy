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
import com.kms.katalon.core.testdata.reader.CSVReader
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

public class CSVUtil {
	static String escape(Object v) {
		if (v == null) return ""
		String s = v.toString()
		boolean needQuote = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r")
		s = s.replace("\"", "\"\"")
		return needQuote ? "\"${s}\"" : s
	}
	
	static void writeCsv(String filePath, List<String> headers, List<Map> rows) {
		File file = new File(filePath)
		file.parentFile?.mkdirs()
		
		file.withWriter("UTF-8") { w ->
			w << headers.collect { escape(it) }.join(",") << "\n"
			rows.each { r ->
				w << headers.collect { h -> escape(r[h]) }.join(",") << "\n"
			}
		}
	}
	
}
