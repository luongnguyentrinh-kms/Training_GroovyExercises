package utils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.xssf.usermodel.XSSFWorkbook

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

public class ExcelUtil {
	static Workbook openWorkbook(String filePath) {
		InputStream is = new FileInputStream(filePath)
		return new XSSFWorkbook(is)
	}
	
	// Get name column and its index in header
	static Map<String, Integer> headerIndexMap(Sheet sheet, int headerRowIdx = 0){
		Row header = sheet.getRow(headerRowIdx)
		Map<String, Integer> map = [:]
		
		header.eachWithIndex { Cell c, int idx -> 
			String key = c.getStringCellValue()?.trim()
			if(key) map[key] = idx
		}
		
		return map
	}
	
	static Object cellValue(Row row, Integer colIdx) { 
//		println "Index receive: ${colIdx}"
		if(row == null || colIdx == null) {
			return null
		}
		
		Cell cell = row.getCell(colIdx)
//		println "Cell: ${cell}"
		if(cell == null) {
			return null
		}
		
//		println "Celltype String: ${CellType.STRING}"
		switch(cell.getCellTypeEnum()) {
			case CellType.STRING: return cell.getStringCellValue()
			case CellType.NUMERIC: 
				// Check is Date
				if(DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue()
				}
				return cell.getNumericCellValue()
			case CellType.BOOLEAN: return cell.getBooleanCellValue()
			case CellType.FORMULA: 
				// Evaluate formula
				FormulaEvaluator evaluator = row.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()
				CellValue cv = evaluator.evaluate(cell)
				switch(cv.getCellTypeEnum()) {
					case CellType.STRING: return cv.getStringValue()
					case CellType.NUMERIC: return cv.getNumberValue()
					case CellType.BOOLEAN: return cv.getBooleanValue()
					default: return null
				}
			default: return null
		}
	}
}
