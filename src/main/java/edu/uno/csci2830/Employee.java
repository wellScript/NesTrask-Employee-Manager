package edu.uno.csci2830;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
        } else {this.active = false;};
        System.out.println("Employee added successfully! Employee ID = " + id);
    }

    public void disable(int id){
        this.active = false;
        this.termDate = LocalDate.now();
        this.dateLastMaint = LocalDate.now();
        System.out.println("Employee Terminated!");
    }

    public int getId(){
        System.out.println("Employee ID Number: " + this.id + "\n");
        return this.id;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public void setFirstName(String firstname){
        this.firstName = firstName;
        this.dateLastMaint = LocalDate.now();
        System.out.println("First Name Updated!\n");
    }
    public void setLastName(String lastname){
        this.lastName = lastName;
        this.dateLastMaint = LocalDate.now();
        System.out.println("Last Name Updated!\n");
    }
    public void setTitle(String title){
        this.title = title;
        this.dateLastMaint = LocalDate.now();
        System.out.println("Title Updated!\n");
    }
    public void setDepartment(String department){
        this.department = department;
        this.dateLastMaint = LocalDate.now();
        System.out.println("Department Updated!\n");
    }

    @Override
    public String toString(){
        return ("First Name: " + this.firstName + "\nLast Name: " + this.lastName + "\n"
        + "Title: " + this.title + "\nDepartment: " + this.department + "\n"
        + "Start Date: " + this.startDate + "\nTerm Date: " + this.termDate + "\n"
        + "Active? " + this.active + "\n\n");
    }
}