package edu.uno.csci2830;

import java.time.LocalDate;

/**
 * Represents an employee in the organization.
 *
 * <p>This class stores information about an employee, including
 * first name, last name, department, title, start date,
 * active status, and maintenance dates. Methods are provided
 * to update employee information and manage employment status.</p>
 *
 * <p>Active status is automatically determined based on the start date
 * when the employee is created. Termination and Updates refresh
 * the last maintenance date.</p>
 *
 * @author Shelby Wells
 * @version 1.0
 */
public class Employee{

    private final int id;
    private String firstName;
    private String lastName;
    private String department;
    private String title;
    private LocalDate startDate;
    private LocalDate termDate;
    private LocalDate dateLastMaint;
    private boolean active;

    /**
     * Construct new employee using ID Number,
     * First Name, Last Name, Department, Title,
     * and Start Date. DateLastMaint updates
     * to System Date.
     *
     * The employee's active status is determined based on the start date:
     * if the start date is today or in the past, the employee is active;
     * otherwise, the employee is inactive.
     *
     * @param id        the unique identifier for the employee
     * @param firstName the employee's first name
     * @param lastName  the employee's last name
     * @param department the department to which the employee belongs
     * @param title     the employee's job title
     * @param startDate the employee's start date
     *
     * @author Shelby Wells
     * @version 1.0
     */
    public Employee(int id, String firstName, String lastName, String department, String title, LocalDate startDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.title = title;
        this.startDate = startDate;
        this.dateLastMaint = LocalDate.now();
        if (startDate.isBefore(LocalDate.now()) || startDate.isEqual(LocalDate.now())) {
            this.active = true;
        } else {
            this.active = false;};
        System.out.println("Employee added successfully! Employee ID = " + id);
    }

    /**
     * Disables the employee by setting their active status to false,
     * recording the termination date, and updating the last maintenance date.
     * Also prints a termination message to the console.
     *
     * @param id the unique identifier of the employee to be disabled
     */
    public void disable(int id){
        this.active = false;
        this.termDate = LocalDate.now();
        this.dateLastMaint = LocalDate.now();
        System.out.println("Employee Terminated!");
    }

    /**
     * Returns the employee's ID number
     *
     * @return  Employee's ID Number
     */
    public int getId(){
        return this.id;
    }

    /**
     * Returns the employee's first name
     *
     * @return  Employee's First Name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Returns the employee's last name
     *
     * @return  Employee's Last Name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Updates the employee's first name
     *
     * @param firstname Employee's New First Name
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
        this.dateLastMaint = LocalDate.now();
        System.out.println("\nFirst Name Updated!\n");
    }

    /**
     * Updates the employee's last name
     *
     * @param lastName Employee's New Last Name
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
        this.dateLastMaint = LocalDate.now();
        System.out.println("Last Name Updated!\n");
    }

    /**
     * Updates the employee's title
     *
     * @param title Employee's New Title
     */
    public void setTitle(String title){
        this.title = title;
        this.dateLastMaint = LocalDate.now();
        System.out.println("Title Updated!\n");
    }

    /**
     * Updates the employee's department
     *
     * @param department Employee's New Department
     */
    public void setDepartment(String department){
        this.department = department;
        this.dateLastMaint = LocalDate.now();
        System.out.println("Department Updated!\n");
    }

    /**
     * Returns the employee's information
     *
     * @return  string of Employee's first name,
     * last name, title, department, start date,
     * term date, and active status.
     */
    @Override
    public String toString(){
        return ("Id: " + this.id + "\nFirst Name: " + this.firstName + "\nLast Name: " + this.lastName + "\n"
        + "Title: " + this.title + "\nDepartment: " + this.department + "\n"
        + "Start Date: " + this.startDate + "\nTerm Date: " + this.termDate + "\n"
        + "Active? " + this.active + "\n\n");
    }
}