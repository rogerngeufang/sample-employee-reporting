# Employee reporting tool

## Overview

This Java application analyzes employee data to identify:
- Managers earning less than 20% more than their subordinates' average salary
- Managers earning more than 50% more than their subordinates' average salary
- Employees with reporting lines longer than 4 managers

## Input format
```csv
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
```

## Requirements

- Java 21 or later
- Maven 3.6.0 or later

## Installation & Usage

1. Clone the repository:
```bash
git clone https://github.com/your-repo/sample-employee-reporting.git
cd sample-employee-reporting
```

2. Build and run:
```bash
mvn package
java -jar target/sample-employee-reporting-1.0-SNAPSHOT.jar data/employees.csv
```