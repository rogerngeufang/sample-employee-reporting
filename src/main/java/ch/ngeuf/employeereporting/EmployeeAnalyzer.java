package ch.ngeuf.employeereporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeAnalyzer {
  public static class ManagerSalaryIssue {
    private final Employee manager;
    private final double expectedMin;
    private final double expectedMax;
    private final double currentSalary;
    private final double averageSubordinateSalary;

    public ManagerSalaryIssue(Employee manager, double expectedMin, double expectedMax,
                              double currentSalary, double averageSubordinateSalary) {
      this.manager = manager;
      this.expectedMin = expectedMin;
      this.expectedMax = expectedMax;
      this.currentSalary = currentSalary;
      this.averageSubordinateSalary = averageSubordinateSalary;
    }

    public Employee getManager() {
      return manager;
    }

    public double getExpectedMin() {
      return expectedMin;
    }

    public double getExpectedMax() {
      return expectedMax;
    }

    public double getCurrentSalary() {
      return currentSalary;
    }

    public double getAverageSubordinateSalary() {
      return averageSubordinateSalary;
    }
  }

  public static class ReportingLineIssue {
    private final Employee employee;
    private final int reportingLineLength;

    public ReportingLineIssue(Employee employee, int reportingLineLength) {
      this.employee = employee;
      this.reportingLineLength = reportingLineLength;
    }

    public Employee getEmployee() {
      return employee;
    }

    public int getReportingLineLength() {
      return reportingLineLength;
    }
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

      double expectedMin = averageSalary * 1.2;
      double expectedMax = averageSalary * 1.5;

      if (manager.salary() < expectedMin) {
        issues.add(new ManagerSalaryIssue(manager, expectedMin, expectedMax,
            manager.salary(), averageSalary));
      } else if (manager.salary() > expectedMax) {
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
      int lineLength = calculateReportingLineLength(employee, employees, reportingLineCache);
      if (lineLength > 4) {
        issues.add(new ReportingLineIssue(employee, lineLength));
      }
    }

    return issues;
  }

  private int calculateReportingLineLength(Employee employee, Map<String, Employee> employees,
                                           Map<String, Integer> cache) {
    if (employee.managerId() == null || employee.managerId().isEmpty()) {
      return 0; // CEO has no managers
    }

    if (cache.containsKey(employee.id())) {
      return cache.get(employee.id());
    }

    Employee manager = employees.get(employee.managerId());
    if (manager == null) {
      return 0; // Invalid manager reference
    }

    int length = 1 + calculateReportingLineLength(manager, employees, cache);
    cache.put(employee.id(), length);
    return length;
  }
}