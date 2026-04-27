package edu.uno.csci2830;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Manages the collection of employees.
 * 
 * <p>This class handles all business logic for the Employee
 * Management application, including creating, searching, 
 * updating, and terminating employees.</p>
 * 
 * @author Shelby Wells
 * @version 1.0
 */
public class EmployeeManager {
    private final ArrayList<Employee> employees;
    private int nextId;

    /**
     * Constructs a new EmployeeManager with an empty
     * employee list and a starting ID of 1000.
     */
    public EmployeeManager() {
        this.employees = new ArrayList<>();
        this.nextId = 1000;
    }
    
    /**
     * Creates a new employee and adds them to the list.
     * 
     * @param firstName     the employee's first name
     * @param lastName      the employee's last name
     * @param department    the employee's department
     * @param title         the employee's job title
     * @param startDate     the employee's start date
     * @return the newly created Employee
     */
    public Employee addEmployee(String firstName, String lastName, String department, String title, LocalDate startDate) {
        Employee emp = new Employee(nextId++, firstName, lastName, department, title, startDate);
        employees.add(emp);
        return emp;                
    }

    /**
     * Searches for an employee by their ID number.
     * 
     * @param id the employee's unique identifier
     * @return the Employee if found, or null if not found
     */

    public Employee findEmployeeById(int id) {
        for ( Employee emp : employees ) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    /**
     * Searches for an employee by first and last name (case - sensitive).
     * 
     * @param firstName the employee's first name
     * @param lastName  the employee's last name
     * @return the Employee if found, or null if not found
     */
    public Employee findEmployeeByName(String firstName, String lastName) {
        for (Employee emp : employees) {
            if (emp.getFirstName().equalsIgnoreCase(firstName) && emp.getLastName().equalsIgnoreCase(lastName)) {
                return emp;
            }
        }
        return null;
    }

    /**
     * Terminates an employee by their ID number.
     * 
     * @param id the employee's unique identifier
     * @return the terminated Employee, or null if not found
     */
    public Employee terminateEmployee(int id) {
        Employee emp = findEmployeeById(id);
        if (emp != null) {
            emp.disable(id);
        }
        return emp;
    }

    /**
     * Returns all employees in the system.
     * 
     * @return the list of all employees
     */
    public ArrayList<Employee> getAllEmployees() {
        return employees;
    }
}
