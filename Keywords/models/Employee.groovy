package models

import java.time.LocalDate

public class Employee {

    Long id
    String name
    String position
    String office
    Integer age
    LocalDate startDate
    BigDecimal salaryUsd

    @Override
    String toString() {
        return 'Employee(' +
            "id='${id}', " +
            "name='${name}', " +
            "position='${position}', " +
            "office='${office}', " +
            "age=${age}, " +
            "startDate=${startDate}, " +
            "salaryUsd=${salaryUsd}" +
            ')'
    }

    Map toJson() {
        return [
            id: id,
            name: name,
            position: position,
            office: office,
            age: age,
            startDate: startDate?.toString(),
            salaryUsd: salaryUsd
            ]
    }

}
