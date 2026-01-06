package services

import models.Employee
import models.ExchangeRate
import utils.MoneyUtil

import java.time.LocalDate

public class EmployeeFilterService {

    /**
     * conditions support:
     * - name (String)                           -> case 1
     * - minSalaryUsd (BigDecimal/Number)        -> case 2
     * - maxAge (Integer/Number)                 -> case 4
     * - ageContains (String)                    -> case 5
     * - startDateFrom (LocalDate)               -> case 6
     */
    static List<Employee> getEmployeeBy(List<Employee> employees, Map conditions) {
        String name = conditions.name?.toString()
        String office = conditions.office?.toString()
        BigDecimal minSalaryUsd = conditions.minSalaryUsd ? new BigDecimal(conditions.minSalaryUsd.toString()) : null
        Integer maxAge = conditions.maxAge ? Integer.parseInt(conditions.maxAge.toString()) : null
        String ageContains = conditions.ageContains?.toString()
        LocalDate startDateFrom = conditions.startDateFrom instanceof LocalDate ? conditions.startDateFrom : null

        return employees.findAll { Employee e ->
            // name
            if (name && !e.name?.trim()?.equalsIgnoreCase(name.trim())) {
                return false
            }

            // office
            if (office && !e.office?.trim()?.equalsIgnoreCase(office.trim())) {
                return false
            }

            // minSalaryUsd
            if (minSalaryUsd != null && (e.salaryUsd == null || e.salaryUsd <= minSalaryUsd)) {
                return false
            }

            // maxAge
            if (maxAge != null && (e.age == null || e.age > maxAge)) {
                return false
            }

            // ageContains
            if (ageContains && (e.age == null || !e.age.toString().contains(ageContains))) {
                return false
            }

            // startDateFrom
            if (startDateFrom != null && (e.startDate == null || e.startDate.isBefore(startDateFrom))) {
                return false
            }

            return true
        }
    }

    static BigDecimal salaryOf(List<Employee> employees, Map conditions) {
        return getEmployeeBy(employees, conditions)
            ?.first()
            ?.salaryUsd
    }

    static List<Employee> withPositionAndSalaryLessThanVnd(List<Employee> employees, List<String> positions, BigDecimal maxVnd, ExchangeRate rate) {
        return employees.findAll { e ->
            if (e.position == null || e.salaryUsd == null) {
                return false
            }

            boolean pos = positions.any { p -> e.position.trim().equalsIgnoreCase(p) }
            if (!pos) {
                return false
            }

            BigDecimal salaryVnd = MoneyUtil.usdToVnd(e.salaryUsd, rate.vnd)

            return salaryVnd < maxVnd
        }
    }

}
