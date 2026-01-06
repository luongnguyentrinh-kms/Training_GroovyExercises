package services

import java.time.LocalDate

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

import models.Employee
import models.ExchangeRate
import utils.CSVUtil
import utils.DateUtil
import utils.ExcelUtil
import utils.JSONUtil
import utils.MoneyUtil
import utils.RandomUtils

public class EmployeeDataService {

    // ============== EXCEL IMPORT ==============
    static List<Employee> readEmployeesFromExcel(Workbook wb) {
        Sheet sheet = wb.getSheet('employee')
        if (sheet == null) {
            throw new IllegalArgumentException('Missing sheet: employee')
        }

        Map<String, Integer> h = ExcelUtil.getHeaderIndexs(sheet, 0)

        List<Employee> employees = []
        int cntNull = 0
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            if (cntNull > 3) {
                break
            }

            Row row = sheet.getRow(r)

            if (row == null) continue

            def name = ExcelUtil.getCellValue(row, h['Name'])?.toString()?.trim()

            if (!name) {
                cntNull++
                continue
            }

            Employee e = new Employee(
                id: RandomUtils.randomId(),
                name: name,
                position: ExcelUtil.getCellValue(row, h['Position'])?.toString()?.trim(),
                office: ExcelUtil.getCellValue(row, h['Office']).toString()?.trim(),
                age: (ExcelUtil.getCellValue(row, h['Age']) as Number)?.intValue(),
                startDate: DateUtil.toLocalDate(ExcelUtil.getCellValue(row, h['Start date'])),
                salaryUsd: MoneyUtil.parseUsd(ExcelUtil.getCellValue(row, h['Salary']))
            )

            employees << e
        }

        return employees
    }

    static ExchangeRate readExchangeRateFromExcel(Workbook wb) {
        Sheet sheet = wb.getSheet('exchange rate')

        if (sheet == null) throw new IllegalArgumentException('Missing sheet: exchange rate')

        Row row2 = sheet.getRow(1)
        if (row2 == null) throw new IllegalArgumentException('exchange rate sheet missing row 2')

        def usdValue = ExcelUtil.getCellValue(row2, 0)
        BigDecimal usd = usdValue != null ? new BigDecimal(usdValue.toString()) : null

        def vndValue = ExcelUtil.getCellValue(row2, 1)
        BigDecimal vnd = vndValue != null ? new BigDecimal(vndValue.toString()) : null

        return new ExchangeRate(usd: usd, vnd: vnd)
    }

    // ============== JSON IMPORT ==============
    static List<Employee> readEmployeesFromJson(String jsonPath) {
        def root = JSONUtil.readJson(jsonPath)

        def list = root?.employees
        if (!(list instanceof List)) {
            throw new IllegalArgumentException("Field 'employees' must be a List")
        }

        return list.collect { m ->
            new Employee(
                    id: m.id,
                    name: m.name,
                    position: m.position,
                    office: m.office,
                    age: m.age as Integer,
                    startDate : m.startDate ? LocalDate.parse(m.startDate.toString()) : null,
                    salaryUsd : m.salaryUsd != null ? new BigDecimal(m.salaryUsd.toString()) : null
                )
        }
    }

    static ExchangeRate readExchangeRateFromJson(String jsonPath) {
        def root = JSONUtil.readJson(jsonPath)
        def rate = root?.rate?.one_usd
        if (rate == null) {
            throw new IllegalArgumentException('Cannot find rate.one_usd in JSON')
        }

        BigDecimal usd = 1
        BigDecimal vnd = new BigDecimal(rate.toString())

        return new ExchangeRate(usd: usd, vnd: vnd)
    }
    
    // ============== CSV IMPORT ==============
//    static final List<String> headersEmployee =
//    ["id", "name","position","office","age","startDate","salaryUsd"]
//    static final List<String> headersExchangeRate = ["USD", "VND"]
    
    static List<Employee> readEmployeesFromCsv(String csvPath) {
        def rows = CSVUtil.readCsv(csvPath)
		println(rows)
        
        List<Employee> employees = []
        
        rows.each { r ->
            if (!r['id']) return
    
            employees << new Employee(
                id        : r['id']?.isLong() ? r['id'].toLong() : null,
                name      : r['name'],
                position  : r['position'],
                office    : r['office'],
                age       : r['age']?.isInteger() ? r['age'].toInteger() : null,
                startDate : r['startDate'] ? LocalDate.parse(r['startDate']) : null,
                salaryUsd : r['salaryUsd'] ? new BigDecimal(r['salaryUsd']) : null
            )
        }
        return employees
    }
	
	static ExchangeRate readExchangeRateFromCsv(String csvPath) {
		def rows = CSVUtil.readCsv(csvPath)
		
		BigDecimal usd = new BigDecimal(rows[0]['USD'].toString())
		
		BigDecimal vnd = new BigDecimal(rows[0]['VND'].toString())
		
		return new ExchangeRate(usd: usd, vnd: vnd)
	}
    
    static void exportEmployeesToCsv(String filePath, List<Employee> employees) {
        if (employees == null) employees = []
		List<String> headersEmployee = CSVUtil.readHeaders(filePath)
                
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
		List<String> headersExchangeRate = CSVUtil.readHeaders(filePath)
		
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
