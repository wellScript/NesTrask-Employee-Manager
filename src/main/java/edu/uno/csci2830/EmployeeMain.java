package edu.uno.csci2830;

import java.util.Scanner;
import java.time.LocalDate;

/**
 * Program that maintains employee information.
 *
 * <p>This class runs the main method of the Employee
 * Manager. It handles user interaction, creates new
 * Employee objects, updates employees, disables employees,
 * or searches for employees by id or name.</p>
 *
 * <p> Currently, the employees are not stored in
 * the computer's memory. Employee's may only be managed
 * in the session. </p>
 *
 * @author Shelby Wells
 * @version 2.0
 */
public class EmployeeMain {
    /**
     * Entry point for the Employee Management application.
     *
     * @param args command-line arguments (not used)
     */
    static void main(String[] args) {
        //Initialize Employee Manager
        EmployeeManager manager = new EmployeeManager();

        //Initialize Scanner
        Scanner sc = new Scanner(System.in);

        //Initialize choice variable
        int choice = 0;

        //Initialize Menu Options
        String employeeMenuOptions = """
                ** Welcome to the NesTrack Employee Database ***
                \tWhat would you like to do?
                \t\t1. Create a new employee
                \t\t2. Search for an employee
                \t\t3. Update an existing employee
                \t\t4. Terminate an employee
                \t\t5. View All Employees
                \t\t6. Exit
                """;

        //Indefinite loop while true
        while (true) {

            //Display menu options for user
            System.out.println(employeeMenuOptions);

            //Assign next int to local variable choice
            choice = sc.nextInt();
            sc.nextLine();

            //Evaluate Feedback
            switch (choice) {
                /* Create new employee. Prompt user for first name,
                 * last name, department, title, and start date.
                 * Convert start date into LocalDate format. Assign ID
                 * and increment nextId variable.
                 *
                 * Construct new employee with user feedback. Add
                 * employee to employees array list.
                 *
                 * Print employee information.*/
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
                    Employee emp = manager.addEmployee(firstName, lastName, department, title, startDate);
                    System.out.println(emp);
                    break;

                /* Search for employee
                 *
                 * Prompt user to determine if searching by name
                 * or id number. Assign user feedback to choice
                 * variable.
                 *
                 * Perform search. */
                case 2:
                    System.out.println("--------------------------------");
                    System.out.println("1. Search by name");
                    System.out.println("2. Search by Id");
                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {

                        /* Search by name
                         *
                         * Prompt user for first name and last name.
                         * Compare to employees array list by lowering
                         * all values and comparing them.
                         *
                         * If an employee is found, return the employee.
                         * Otherwise, write 'No Employee Found!' to
                         * console, signaling that there is no match. */
                        case 1:
                            System.out.println("--------------------------------");
                            System.out.println("First Name: ");
                            String firstname = sc.nextLine();
                            System.out.println("Last Name: ");
                            String lastname = sc.nextLine();
                            emp = manager.findEmployeeByName(firstname, lastname);
                            if (emp != null) {
                                System.out.println(emp);
                            }else{
                                System.out.println("No Employee Found!");
                            }
                            break;

                        /* Search by id number
                         *
                         * Search the employees array list for a record
                         * where the id matches the id provided by user.
                         *
                         * If an employee is found, return the employee.
                         * Otherwise, write 'No Employee Found!' to
                         * console, signaling that there is no match. */
                        case 2:
                            System.out.println("--------------------------------");
                            System.out.println("Employee ID Number: ");
                            int searchId = sc.nextInt();
                            sc.nextLine();
                            emp = manager.findEmployeeById(searchId);
                            if (emp != null) {
                                System.out.println(emp);
                            }else{
                                System.out.println("No Employee Found!");};
                            break;

                        //If user does not select 1 or 2, write an error to the console.
                        default:
                            System.out.println("--------------------------------");
                            System.out.println("Invalid choice. Please enter a number from 1 to 2.");
                            break;
                    }
                    break;
                /* Update employee using employee id number
                 *
                 * Prompt user for id number. Search for matching employee.
                 * If found, prompt user to determine which field to update.
                 * If not found, write message to console, signaling that
                 * no employee was found with id provided. */
                case 3:
                    System.out.println("--------------------------------");
                    System.out.println("To update an employee, please provide the Employee ID Number: ");
                    int updateId = sc.nextInt();
                    emp = manager.findEmployeeById(updateId);

                    if (emp != null) {
                        System.out.println(emp);

                        System.out.println("--------------------------------");
                        System.out.println("Please choose a field for update:");
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
                            /*
                             * Prompt user for new first name value and
                             * set the employee's first name to the new
                             * value.
                             */
                            case 1:
                                System.out.println("--------------------------------");
                                System.out.println("New First Name: ");
                                String newFirstName = sc.nextLine();
                                assert emp != null;
                                emp.setFirstName(newFirstName);
                                break;

                            /*
                             * Prompt user for new last name value and
                             * set the employee's last name to the new
                             * value.
                             */
                            case 2:
                                System.out.println("--------------------------------");
                                System.out.println("New Last Name: ");
                                String newLastName = sc.nextLine();
                                assert emp != null;
                                emp.setLastName(newLastName);
                                break;

                            /*
                             * Prompt user for new title value and
                             * set the employee's title to the new
                             * value.
                             */
                            case 3:
                                System.out.println("--------------------------------");
                                System.out.println("New Title: ");
                                String newTitle = sc.nextLine();
                                assert emp != null;
                                emp.setTitle(newTitle);
                                break;

                            /*
                             * Prompt user for new department value and
                             * set the employee's department to the new
                             * value.
                             */
                            case 4:
                                System.out.println("--------------------------------");
                                System.out.println("New Department: ");
                                String newDepartment = sc.nextLine();
                                assert emp != null;
                                emp.setDepartment(newDepartment);
                                break;

                            /*
                             * Return to main menu.
                             */
                            case 5:
                                break;

                            /*
                             * If user enters value other than 1-5, then print error
                             * message to console.
                             */
                            default:
                                System.out.println("--------------------------------");
                                System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                                break;
                        }
                    } else {
                        System.out.println("Employee not found!");
                        break;
                    }
                    break;

                /*
                 *Terminate employee
                 *
                 * Prompt user for employee id number. Search for employee
                 * with employee id number. If found, disable the matching
                 * employee. If not found, assert error. Display the
                 * employee's new information to the console.
                 */
                case 4:
                    System.out.println("--------------------------------");
                    System.out.println("To terminate an employee, please provide the Employee ID Number: ");
                    int termindateId = sc.nextInt();
                    emp = manager.findEmployeeById(termindateId);
                    assert emp != null;
                    emp.disable(termindateId);
                    System.out.println(emp);
                    break;

                // Display entire list of employees.
                case 5:
                    System.out.println("--------------------------------");
                    for (Employee e : manager.getAllEmployees()) {
                        System.out.println(e + "------------------------\n");
                    }
                    break;

                //Quit program.
                case 6:
                    System.out.println("--------------------------------");
                    System.out.println("Goodbye!");
                    sc.close();
                    return;

                /* If user enters any value other than 1-5, display error message
                to console. */
                default:
                    System.out.println("--------------------------------");
                    System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                    break;
            }
        }
    }
}
