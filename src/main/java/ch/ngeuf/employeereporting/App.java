package ch.ngeuf.employeereporting;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class App {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: java -jar employee-reporting.jar <input-file>");
      return;
    }

    try {
      Path filePath = Path.of(args[0]);
      EmployeeParser parser = new EmployeeParser();
      EmployeeAnalyzer analyzer = new EmployeeAnalyzer();

      Map<String, Employee> employees = parser.parseEmployees(filePath);

      System.out.println("=== Manager Salary Issues ===");
      List<EmployeeAnalyzer.ManagerSalaryIssue> salaryIssues = analyzer.analyzeManagerSalaries(employees);
      printSalaryIssues(salaryIssues);

      System.out.println("\n=== Long Reporting Lines ===");
      List<EmployeeAnalyzer.ReportingLineIssue> reportingIssues = analyzer.analyzeReportingLines(employees);
      printReportingIssues(reportingIssues);

    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private static void printSalaryIssues(List<EmployeeAnalyzer.ManagerSalaryIssue> issues) {
    if (issues.isEmpty()) {
      System.out.println("No manager salary issues found");
      return;
    }

    for (EmployeeAnalyzer.ManagerSalaryIssue issue : issues) {
      Employee manager = issue.getManager();
      if (issue.getCurrentSalary() < issue.getExpectedMin()) {
        double difference = issue.getExpectedMin() - issue.getCurrentSalary();
        System.out.printf("%s (ID: %s) earns %.2f less than they should (average subordinate salary: %.2f)%n",
            manager.fullName(), manager.id(), difference, issue.getAverageSubordinateSalary());
      } else {
        double difference = issue.getCurrentSalary() - issue.getExpectedMax();
        System.out.printf("%s (ID: %s) earns %.2f more than they should (average subordinate salary: %.2f)%n",
            manager.fullName(), manager.id(), difference, issue.getAverageSubordinateSalary());
      }
    }
  }

  private static void printReportingIssues(List<EmployeeAnalyzer.ReportingLineIssue> issues) {
    if (issues.isEmpty()) {
      System.out.println("No long reporting lines found");
      return;
    }

    for (EmployeeAnalyzer.ReportingLineIssue issue : issues) {
      Employee employee = issue.getEmployee();
      System.out.printf("%s (ID: %s) has %d managers above them (max allowed is 4)%n",
          employee.fullName(), employee.id(), issue.getReportingLineLength());
    }
  }
}
