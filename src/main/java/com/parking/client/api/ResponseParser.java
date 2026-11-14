package com.parking.client.api;

import com.parking.client.model.BookedSlot;
import com.parking.client.model.DailySale;
import com.parking.client.model.Employee;
import com.parking.client.model.ParkingRent;
import com.parking.client.model.TicketInfo;
import com.parking.common.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/** Turns the plain text answers of the server into model objects. */
public final class ResponseParser {

    private ResponseParser() {
    }

    public static List<ParkingRent> rents(String response) {
        List<ParkingRent> rents = new ArrayList<>();
        StringTokenizer rows = new StringTokenizer(response, Protocol.ROW);
        while (rows.hasMoreTokens()) {
            String[] row = rows.nextToken().split(Protocol.COMMA);
            rents.add(new ParkingRent(row[0], Integer.parseInt(row[1])));
        }
        return rents;
    }

    public static List<BookedSlot> bookedSlots(String response) {
        List<BookedSlot> slots = new ArrayList<>();
        StringTokenizer rows = new StringTokenizer(response, Protocol.TILDE);
        while (rows.hasMoreTokens()) {
            StringTokenizer st = new StringTokenizer(rows.nextToken(), Protocol.HASH);
            slots.add(new BookedSlot(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken()));
        }
        return slots;
    }

    /** @return the ticket details, or null if the server answered "failed" */
    public static TicketInfo ticketInfo(String response) {
        if (response.equals(Protocol.FAILED)) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(response, Protocol.TILDE);
        return new TicketInfo(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(),
                Integer.parseInt(st.nextToken()));
    }

    public static List<DailySale> dailySales(String response) {
        List<DailySale> sales = new ArrayList<>();
        StringTokenizer rows = new StringTokenizer(response, Protocol.ROW);
        while (rows.hasMoreTokens()) {
            StringTokenizer st = new StringTokenizer(rows.nextToken(), Protocol.FIELD);
            String parkingBay = st.nextToken();
            String sale = st.nextToken();
            // SUM(rent) is NULL while the vehicles of the day have not exited yet
            sales.add(new DailySale(parkingBay, sale.equals("null") ? 0 : Integer.parseInt(sale)));
        }
        return sales;
    }

    public static List<Employee> employees(String response) {
        List<Employee> employees = new ArrayList<>();
        StringTokenizer rows = new StringTokenizer(response, Protocol.ROW);
        while (rows.hasMoreTokens()) {
            StringTokenizer st = new StringTokenizer(rows.nextToken(), Protocol.FIELD);
            employees.add(new Employee(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(),
                    st.nextToken(), st.nextToken()));
        }
        return employees;
    }
}
