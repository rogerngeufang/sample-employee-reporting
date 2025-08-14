package ch.ngeuf.employeereporting;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeAnalyzerTest {
  @Test
  void analyzeManagerSalaries_shouldDetectUnderpaidManagers() {
    Map<String, Employee> employees = new HashMap<>();
    employees.put("1", new Employee("1", "CEO", "Smith", 70000, null));
    employees.put("2", new Employee("2", "Manager", "Jones", 55000, "1"));
    employees.put("3", new Employee("3", "Worker", "Doe", 50000, "2"));
    employees.put("4", new Employee("4", "Worker", "Johnson", 50000, "2"));

    EmployeeAnalyzer analyzer = new EmployeeAnalyzer();
    List<EmployeeAnalyzer.ManagerSalaryIssue> issues = analyzer.analyzeManagerSalaries(employees);

    assertEquals(1, issues.size());
    assertEquals("Manager Jones", issues.getFirst().getManager().fullName());
    assertTrue(issues.getFirst().getCurrentSalary() < issues.getFirst().getExpectedMin());
  }

  @Test
  void analyzeReportingLines_shouldDetectLongChains() {
    Map<String, Employee> employees = new HashMap<>();
    employees.put("1", new Employee("1", "CEO", "Smith", 100000, null));
    employees.put("2", new Employee("2", "L1", "Jones", 90000, "1"));
    employees.put("3", new Employee("3", "L2", "Brown", 80000, "2"));
    employees.put("4", new Employee("4", "L3", "Green", 70000, "3"));
    employees.put("5", new Employee("5", "L4", "White", 60000, "4"));
    employees.put("6", new Employee("6", "L5", "Black", 50000, "5"));

    EmployeeAnalyzer analyzer = new EmployeeAnalyzer();
    List<EmployeeAnalyzer.ReportingLineIssue> issues = analyzer.analyzeReportingLines(employees);

    assertEquals(1, issues.size());
    assertEquals("L5 Black", issues.getFirst().getEmployee().fullName());
    assertEquals(5, issues.getFirst().getReportingLineLength());
  }
}