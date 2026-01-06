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

import internal.GlobalVariable

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

public class DateUtil {
    static final List<DateTimeFormatter> FORMATTERS = [
        DateTimeFormatter.ISO_LOCAL_DATE,                 // 2011-04-25
        DateTimeFormatter.ofPattern("M/d/yyyy"),          // 4/25/2011
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),        // 04/25/2011
        DateTimeFormatter.ofPattern("d/M/yyyy"),          // 25/4/2011
        DateTimeFormatter.ofPattern("dd/MM/yyyy")         // 25/04/2011
    ]
    
    
    static LocalDate toLocalDate(Object cellValue) {
        if(cellValue == null) return null
        
        if(cellValue instanceof Date) {
            return cellValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
        
        // TODO: Check more return type
        if(cellValue instanceof CharSequence) {
            String s = cellValue.toString().trim()
            
            if (!s) return null
            
            for (def fmt : FORMATTERS) {
                try {
                    return LocalDate.parse(s, fmt)
                } catch (DateTimeParseException ignored) {}
            }
            
            throw new IllegalArgumentException("Unrecognized date format: '${s}'")
        }
    }
}
