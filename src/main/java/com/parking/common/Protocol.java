package com.parking.common;

/** Answers and separators used in the plain text responses of the server. */
public final class Protocol {

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    public static final String DUPLICATE = "duplicate";

    /** between the fields of a ticket, and between the rows of booked slots */
    public static final String TILDE = "~~~~";
    /** between the fields of a booked slot */
    public static final String HASH = "####";
    /** between rows (rents, employees, daily sale) */
    public static final String ROW = "~!@";
    /** between the fields of an employee / daily sale row */
    public static final String FIELD = "#$%";
    /** between the fields of a rent row */
    public static final String COMMA = ",";

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "HH:mm";

    private Protocol() {
    }
}
