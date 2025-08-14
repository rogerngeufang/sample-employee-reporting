package ch.ngeuf.employeereporting;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeParserTest {

  @Test
  void parseEmployees_shouldParseValidFile() throws Exception {
    EmployeeParser parser = new EmployeeParser();
    Path testFile = Path.of("src/test/resources/employees.csv");
    Map<String, Employee> employees = parser.parseEmployees(testFile);

    assertEquals(5, employees.size());
    assertTrue(employees.containsKey("123"));
    assertEquals("Joe", employees.get("123").firstName());
    assertEquals(60000, employees.get("123").salary());
    assertNull(employees.get("123").managerId());
  }
}