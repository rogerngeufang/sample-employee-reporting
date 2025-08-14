package ch.ngeuf.employeereporting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeParser {
  public Map<String, Employee> parseEmployees(Path filePath) throws IOException {
    List<String> lines = Files.readAllLines(filePath);
    Map<String, Employee> employees = new HashMap<>();

    // Skip header if present
    int startLine = lines.getFirst().toLowerCase().contains("id") ? 1 : 0;

    for (int i = startLine; i < lines.size(); i++) {
      String line = lines.get(i).trim();
      if (isNotValidLine(line)) continue;

      String[] parts = line.split(",");
      String id = parts[0].trim();
      String firstName = parts[1].trim();
      String lastName = parts[2].trim();
      double salary = Double.parseDouble(parts[3].trim());
      String managerId = parts.length > 4 ? parts[4].trim() : null;

      employees.put(id, new Employee(id, firstName, lastName, salary, managerId));
    }

    return employees;
  }

  private boolean isNotValidLine(String line) {
    if (line.isEmpty()) return true;
    return line.split(",").length < 4;
  }
}