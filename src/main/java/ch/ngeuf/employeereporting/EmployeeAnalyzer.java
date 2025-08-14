package ch.ngeuf.employeereporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeAnalyzer {

  public static final double PERCENTAGE_MINIMUM = 1.2; // min 20%
  public static final double PERCENTAGE_MAXIMUN = 1.5; // max 50%

  public record ManagerSalaryIssue(
      Employee manager,
      double expectedMin,
      double expectedMax,
      double currentSalary,
      double averageSubordinateSalary) {
  }

  public record ReportingLineIssue(
      Employee employee,
      int reportingLineLength
  ) {
  }

  public List<ManagerSalaryIssue> analyzeManagerSalaries(Map<String, Employee> employees) {
    List<ManagerSalaryIssue> issues = new ArrayList<>();

    // Group employees by manager
    Map<String, List<Employee>> subordinatesByManager = new HashMap<>();
    for (Employee employee : employees.values()) {
      if (employee.managerId() != null && !employee.managerId().isEmpty()) {
        subordinatesByManager
            .computeIfAbsent(employee.managerId(), k -> new ArrayList<>())
            .add(employee);
      }
    }

    // Check each manager's salary
    for (Map.Entry<String, List<Employee>> entry : subordinatesByManager.entrySet()) {
      String managerId = entry.getKey();
      List<Employee> subordinates = entry.getValue();
      Employee manager = employees.get(managerId);

      if (manager == null) continue;

      double averageSalary = subordinates.stream()
          .mapToDouble(Employee::salary)
          .average()
          .orElse(0);

      double expectedMin = averageSalary * PERCENTAGE_MINIMUM;
      double expectedMax = averageSalary * PERCENTAGE_MAXIMUN;

      if (manager.salary() < expectedMin || manager.salary() > expectedMax) {
        issues.add(new ManagerSalaryIssue(manager, expectedMin, expectedMax,
            manager.salary(), averageSalary));
      }
    }

    return issues;
  }

  public List<ReportingLineIssue> analyzeReportingLines(Map<String, Employee> employees) {
    List<ReportingLineIssue> issues = new ArrayList<>();
    Map<String, Integer> reportingLineCache = new HashMap<>();

    for (Employee employee : employees.values()) {
      int lineLength = 0;
      Employee current = employee;

      // Iteratively traverse up the management chain
      while (current != null) {
        // Check cache first
        if (reportingLineCache.containsKey(current.id())) {
          lineLength += reportingLineCache.get(current.id());
          break;
        }

        if (current.managerId() == null || current.managerId().isEmpty()) {
          // Reached CEO (no manager)
          break;
        }

        lineLength++;
        current = employees.get(current.managerId());
      }

      // Cache the result for this employee
      reportingLineCache.put(employee.id(), lineLength);

      if (lineLength > 4) {
        issues.add(new ReportingLineIssue(employee, lineLength));
      }
    }

    return issues;
  }
}