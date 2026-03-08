package edu.uno.csci2830;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

public class EmployeeMain {
    public static void main(String[] args) {
        //Create employee list
        ArrayList<Employee> employees = new ArrayList<>();
        int nextId = 1000;

        //Initialize Scanner
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        String employeeMenuOptions = """
                ** Welcome to the Mav Employee Database ***
                \tWhat would you like to do?
                \t\t1. Create a new employee
                \t\t2. Search for an employee
                \t\t3. Update an existing employee
                \t\t4. Terminate an employee
                \t\t5. View All Employees
                \t\t6. Exit
                """;
        while (true) {

            System.out.println(employeeMenuOptions);
            choice = sc.nextInt();
            sc.nextLine();

            //Evaluate Feedback
            switch (choice) {
                case 1:
                    System.out.println("--------------------------------");
                    System.out.println("To create a new employee, please provide the following information.");
                    System.out.println("First Name: ");
                    String firstName = sc.nextLine();
                    System.out.println("Last Name: ");
                    String lastName = sc.nextLine();
                    System.out.println("Department: ");
                    String department = sc.nextLine();
                    System.out.println("Title: ");
                    String title = sc.nextLine();
                    System.out.println("Start Date (YYYY-MM-DD): ");
                    String startDateInput = sc.nextLine();
                    LocalDate startDate = LocalDate.parse(startDateInput);
                    int id = nextId++;
                    Employee emp = new Employee(id, firstName, lastName, department, title, startDate);
                    employees.add(emp);
                    System.out.println(emp);
                    break;
                case 2:
                    System.out.println("--------------------------------");
                    System.out.println("1. Search by name");
                    System.out.println("2. Search by Id");
                    choice = sc.nextInt();
                    sc.nextLine();
                    switch (choice) {
                        case 1:
                            System.out.println("--------------------------------");
                            System.out.println("First Name: ");
                            String firstname = sc.nextLine();
                            firstname = firstname.toLowerCase();
                            System.out.println("Last Name: ");
                            String lastname = sc.nextLine();
                            lastname = lastname.toLowerCase();
                            emp = findEmployeeByName(employees, firstname, lastname);
                            if (emp != null) {
                                System.out.println(emp);
                            }else{
                                System.out.println("No Employee Found!");};
                            break;
                        case 2:
                            System.out.println("--------------------------------");
                            System.out.println("Employee ID Number: ");
                            id = sc.nextInt();
                            sc.nextLine();
                            emp = findEmployeeById(employees, id);
                            if (emp != null) {
                                System.out.println(emp);
                            }else{
                                System.out.println("No Employee Found!");};
                            break;
                        default:
                            System.out.println("--------------------------------");
                            System.out.println("Invalid choice. Please enter a number from 1 to 2.");
                            break;
                    }
                    break;
                case 3:
                    System.out.println("--------------------------------");
                    System.out.println("To update an employee, please provide the Employee ID Number: ");
                    id = sc.nextInt();
                    emp = findEmployeeById(employees, id);

                    System.out.println(emp);

                    System.out.println("--------------------------------");
                    System.out.println("What would you like to update?");
                    System.out.print("""
                            \t\t1. First Name
                            \t\t2. Last Name
                            \t\t3. Title
                            \t\t4. Department
                            \t\t5. Cancel
                            """);

                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice){
                        case 1:
                            System.out.println("--------------------------------");
                            System.out.println("New First Name: ");
                            String newFirstName = sc.nextLine();
                            assert emp != null;
                            emp.setFirstName(newFirstName);
                            break;
                        case 2:
                            System.out.println("--------------------------------");
                            System.out.println("New Last Name: ");
                            String newLastName = sc.nextLine();
                            assert emp != null;
                            emp.setLastName(newLastName);
                            break;
                        case 3:
                            System.out.println("--------------------------------");
                            System.out.println("New Title: ");
                            String newTitle = sc.nextLine();
                            assert emp != null;
                            emp.setTitle(newTitle);
                            break;
                        case 4:
                            System.out.println("--------------------------------");
                            System.out.println("New Department: ");
                            String newDepartment = sc.nextLine();
                            assert emp != null;
                            emp.setDepartment(newDepartment);
                            break;
                        case 5:
                            break;
                        default:
                            System.out.println("--------------------------------");
                            System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                            break;
                    }
                    break;
                case 4:
                    System.out.println("--------------------------------");
                    System.out.println("To terminate an employee, please provide the Employee ID Number: ");
                    id = sc.nextInt();
                    emp = findEmployeeById(employees, id);
                    assert emp != null;
                    emp.disable(id);
                    System.out.println(emp);
                    break;
                case 5:
                    System.out.println("--------------------------------");
                    for (Employee e : employees) {
                        System.out.println(e + "------------------------\n");
                    }
                    break;
                case 6:
                    System.out.println("--------------------------------");
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("--------------------------------");
                    System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                    break;
            }
        }
    }

    private static Employee findEmployeeById(ArrayList<Employee> employees, int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    private static Employee findEmployeeByName(ArrayList<Employee> employees, String firstname, String lastname) {
        for (Employee emp : employees) {
            if (emp.getFirstName().equalsIgnoreCase(firstname) &&
                    emp.getLastName().equalsIgnoreCase(lastname)) {
                System.out.println("-------------------------------");
                System.out.println("Employee Found! \n\n");
                return emp;
            }
        }
        return null;
    }
}
