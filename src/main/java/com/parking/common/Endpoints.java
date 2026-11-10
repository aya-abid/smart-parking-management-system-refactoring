package com.parking.common;

/** Port and URL paths of the parking server, shared by the server and the desktop app. */
public final class Endpoints {

    public static final int PORT = 8888;

    public static final String ADMIN_LOGIN = "adminlogin";
    public static final String EMPLOYEE_LOGIN = "employeelogin";
    public static final String ADMIN_CHANGE_PASSWORD = "adminchangepassword";
    public static final String EMPLOYEE_CHANGE_PASSWORD = "employeechangepassword";

    public static final String ADD_RENT = "Parkingrent";
    public static final String GET_RENTS = "GetParkingrent";
    public static final String DELETE_RENT = "deleteparkingrent";

    public static final String PARKING_ENTRY = "Parkingentry";
    public static final String PARKING_EXIT = "Parkingexit";
    public static final String TICKET_INFO = "getticketinfo";
    public static final String BOOKED_SLOTS = "getbookedslots";
    public static final String DAILY_SALE = "getdailysale";

    public static final String ADD_EMPLOYEE = "AddEmployee";
    public static final String ALL_EMPLOYEES = "getallemployees";

    private Endpoints() {
    }
}
