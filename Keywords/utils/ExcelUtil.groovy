package utils

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.CellValue
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

public class ExcelUtil {

    static Workbook openWorkbook(String filePath) {
        InputStream is = new FileInputStream(filePath)
        return new XSSFWorkbook(is)
    }

    static Map<String, Integer> getHeaderIndexs(Sheet sheet, int headerRowIdx = 0) {
        Row header = sheet.getRow(headerRowIdx)
        Map<String, Integer> map = [:]

        header.eachWithIndex { Cell c, int idx ->
            String key = c.getStringCellValue()?.trim()
            if (key) map[key] = idx
        }

        return map
    }

    static Object getCellValue(Row row, Integer colIdx) {
        if (row == null || colIdx == null) {
            return null
        }

        Cell cell = row.getCell(colIdx)

        if (cell == null) {
            return null
        }

        switch (cell.getCellTypeEnum()) {
            case CellType.STRING: return cell.getStringCellValue()
            case CellType.NUMERIC:
                // Check is Date
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue()
                }
                return cell.getNumericCellValue()
            case CellType.BOOLEAN: return cell.getBooleanCellValue()
            case CellType.FORMULA:
                // Evaluate formula
                FormulaEvaluator evaluator = row.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()
                CellValue cv = evaluator.evaluate(cell)
                switch (cv.getCellTypeEnum()) {
                    case CellType.STRING: return cv.getStringValue()
                    case CellType.NUMERIC: return cv.getNumberValue()
                    case CellType.BOOLEAN: return cv.getBooleanValue()
                    default: return null
                }
            default: return null
        }
    }

}
