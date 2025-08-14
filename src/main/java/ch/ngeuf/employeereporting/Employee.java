package ch.ngeuf.employeereporting;

public record Employee(String id,
                       String firstName,
                       String lastName,
                       double salary,
                       String managerId) {

  public String fullName() {
    return firstName + " " + lastName;
  }
}
