package com.parking.client.api;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.parking.client.model.BookedSlot;
import com.parking.client.model.DailySale;
import com.parking.client.model.Employee;
import com.parking.client.model.ParkingRent;
import com.parking.client.model.ParkingTicket;
import com.parking.client.model.TicketInfo;
import com.parking.common.Endpoints;
import com.parking.common.Protocol;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** All the calls from the desktop app to the parking server. */
public class ParkingApi {

    private static final String BASE_URL = System.getProperty("server.url", "http://localhost:" + Endpoints.PORT) + "/";

    // ---- accounts

    public boolean adminLogin(String username, String password) throws UnirestException {
        return isSuccess(get(Endpoints.ADMIN_LOGIN, "username", username, "password", password));
    }

    public boolean employeeLogin(String empId, String password) throws UnirestException {
        return isSuccess(get(Endpoints.EMPLOYEE_LOGIN, "empid", empId, "password", password));
    }

    public boolean adminChangePassword(String username, String oldPassword, String newPassword) throws UnirestException {
        return isSuccess(get(Endpoints.ADMIN_CHANGE_PASSWORD,
                "username", username, "oldpass", oldPassword, "newpass", newPassword));
    }

    public boolean employeeChangePassword(String empId, String oldPassword, String newPassword) throws UnirestException {
        return isSuccess(get(Endpoints.EMPLOYEE_CHANGE_PASSWORD,
                "eid", empId, "oldpassword", oldPassword, "newpassword", newPassword));
    }

    // ---- rents

    public boolean addRent(String vehicleType, int rentPerHour) throws UnirestException {
        return isSuccess(get(Endpoints.ADD_RENT, "vehicletype", vehicleType, "rentperhour", rentPerHour));
    }

    public List<ParkingRent> getRents() throws UnirestException {
        return ResponseParser.rents(get(Endpoints.GET_RENTS));
    }

    public boolean deleteRent(String vehicleType) throws UnirestException {
        return isSuccess(get(Endpoints.DELETE_RENT, "vehicletype", vehicleType));
    }

    // ---- parking

    /** @return the ticket number, or null if the entry failed */
    public String parkVehicle(ParkingTicket ticket) throws UnirestException {
        String response = get(Endpoints.PARKING_ENTRY,
                "empid", ticket.getEmployeeId(),
                "vehicleno", ticket.getVehicleNo(),
                "location", ticket.getLocation(),
                "vehicletype", ticket.getVehicleType(),
                "parkingbay", ticket.getParkingBay(),
                "dateofentry", ticket.getEntryDate(),
                "timeofentry", ticket.getEntryTime());
        return response.equals(Protocol.FAILED) ? null : response;
    }

    public List<BookedSlot> getBookedSlots(String parkingBay) throws UnirestException {
        return ResponseParser.bookedSlots(get(Endpoints.BOOKED_SLOTS, "parkingbay", parkingBay));
    }

    /** @return the ticket details, or null if the ticket number is invalid */
    public TicketInfo getTicketInfo(String ticketNo) throws UnirestException {
        return ResponseParser.ticketInfo(get(Endpoints.TICKET_INFO, "ptid", ticketNo));
    }

    public boolean exitVehicle(String ticketNo, String exitDate, String exitTime, String rent) throws UnirestException {
        return isSuccess(get(Endpoints.PARKING_EXIT,
                "ptid", ticketNo, "exitdate", exitDate, "exittime", exitTime, "rent", rent));
    }

    public List<DailySale> getDailySales() throws UnirestException {
        return ResponseParser.dailySales(get(Endpoints.DAILY_SALE));
    }

    // ---- employees

    public boolean addEmployee(int employeeId, String name, String fatherName, String address, long mobile,
                               String password, File photo) throws UnirestException {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("employeeid", employeeId);
        params.put("name", name);
        params.put("fathername", fatherName);
        params.put("address", address);
        params.put("mobile", mobile);
        params.put("password", password);
        String response = Unirest.post(BASE_URL + Endpoints.ADD_EMPLOYEE)
                .queryString(params)
                .field("photo", photo)
                .asString()
                .getBody();
        return isSuccess(response);
    }

    public List<Employee> getEmployees() throws UnirestException {
        return ResponseParser.employees(get(Endpoints.ALL_EMPLOYEES));
    }

    // ---- helpers

    /** GET request; keyValues are name, value, name, value, ... */
    private String get(String endpoint, Object... keyValues) throws UnirestException {
        Map<String, Object> params = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            params.put((String) keyValues[i], keyValues[i + 1]);
        }
        return Unirest.get(BASE_URL + endpoint).queryString(params).asString().getBody();
    }

    private static boolean isSuccess(String response) {
        return Protocol.SUCCESS.equals(response);
    }
}
