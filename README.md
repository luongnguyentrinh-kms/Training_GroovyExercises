# Training - Groovy Exercises

1. Employee should have employeeId? If there are 2 employee with the same name, how to identify them -> When read employee, auto-generate a employeeId for each record
==> DONE

2. Remove unused import (Using groovy lint extesion)
==> DONE

3. Remove redundant comment (println,...)
==> DONE

4. Your data is on local, people want to run your code -> Fail.
Create a folder to store your data and your testcase call to this folder (dynamic)
Do not need separate TestDataEmployeeExcelService and ManualEmployeeExcelService -> using only ExcelDataHandle
==> DONE

5. Organization:
utils
+ ExcelUtils: handle readExcel, handle data to read,...
+ JSONUtil: handle  readJson, writeJson, handle data to read,...
services: 
+ EmployeeFilterServices: contains functions to filter data
+ EmployeeDataService: contains function readEmployee from Excel, Json,..., writeEmployee to Json, Csv,...
Models
+ Employee
+ ExchangeRate

6. Naming function should be have meaning.
headerIndexMap: To get a List of header and its index -> Rename: getHeaderIndexs
cellValue -> getCellValue
==> DONE

7. EmployeeFilterService:
salaryOfBradley -> create a function to get Salary by a Name: getSalaryByName(String name)
advance: create a function to filter employee by any condition
getEmployeeBy(Map filter = [:]) {
    if (filter.name != null) -> get all employee with name = filter.name
    if (filter.minAge != null)
    if (filter.maxAge != null)
    if (filter.office != null)
.....
} -> can filter by many condition getEmployeeBy([name: 'Bradly', minAge: 15, maxAge: 27])
==> DONE

8. Testcase:
Need assertion:
Ex: def bradleySalary = EmployeeFilterService.salaryOfBradley(employees)
-> verify bradleySalary == 400$