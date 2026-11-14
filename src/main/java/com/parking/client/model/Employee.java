package com.parking.client.model;

/** An employee as shown in the employee list. */
public class Employee {

    private final String empId;
    private final String name;
    private final String fatherName;
    private final String mobileNo;
    private final String address;
    private final String photo;

    public Employee(String empId, String name, String fatherName, String mobileNo, String address, String photo) {
        this.empId = empId;
        this.name = name;
        this.fatherName = fatherName;
        this.mobileNo = mobileNo;
        this.address = address;
        this.photo = photo;
    }

    public String getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoto() {
        return photo;
    }
}
