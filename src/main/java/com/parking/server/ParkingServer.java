package com.parking.server;

import com.parking.common.Endpoints;
import com.parking.common.Protocol;
import com.parking.server.handler.AccountHandlers;
import com.parking.server.handler.EmployeeHandlers;
import com.parking.server.handler.ParkingHandlers;
import com.parking.server.handler.RentHandlers;
import com.parking.server.handler.RequestHandler;
import com.vmm.JHTTPServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** HTTP server used by the desktop app. Every URL path has its own {@link RequestHandler}. */
public class ParkingServer extends JHTTPServer {

    private final Map<String, RequestHandler> handlers = new HashMap<>();

    public ParkingServer(int port) throws IOException {
        super(port);
        registerHandlers();
    }

    private void registerHandlers() {
        AccountHandlers accounts = new AccountHandlers();
        RentHandlers rents = new RentHandlers();
        ParkingHandlers parking = new ParkingHandlers();
        EmployeeHandlers employees = new EmployeeHandlers(ParkingServer::saveUploadedFile);

        handlers.put(Endpoints.ADMIN_LOGIN, accounts::adminLogin);
        handlers.put(Endpoints.EMPLOYEE_LOGIN, accounts::employeeLogin);
        handlers.put(Endpoints.ADMIN_CHANGE_PASSWORD, accounts::adminChangePassword);
        handlers.put(Endpoints.EMPLOYEE_CHANGE_PASSWORD, accounts::employeeChangePassword);

        handlers.put(Endpoints.ADD_RENT, rents::addRent);
        handlers.put(Endpoints.GET_RENTS, rents::getRents);
        handlers.put(Endpoints.DELETE_RENT, rents::deleteRent);

        handlers.put(Endpoints.PARKING_ENTRY, parking::entry);
        handlers.put(Endpoints.PARKING_EXIT, parking::exit);
        handlers.put(Endpoints.TICKET_INFO, parking::ticketInfo);
        handlers.put(Endpoints.BOOKED_SLOTS, parking::bookedSlots);
        handlers.put(Endpoints.DAILY_SALE, parking::dailySale);

        handlers.put(Endpoints.ADD_EMPLOYEE, employees::addEmployee);
        handlers.put(Endpoints.ALL_EMPLOYEES, employees::allEmployees);
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        System.out.println("URI " + uri);
        // the app calls some URLs with two slashes, e.g. http://localhost:8888//adminlogin
        String path = uri.replaceAll("^/+", "");
        RequestHandler handler = handlers.get(path);
        if (handler == null) {
            return new Response(HTTP_OK, MIME_HTML, "<body style='background: #D46A6A; color: #fff'><center><h1>Hello</h1><br> <h3>Welcome To JHTTP Server (Version 1.0)</h3></body></center>");
        }

        String answer;
        try {
            answer = handler.handle(parms, files);
        } catch (Exception e) {
            e.printStackTrace();
            answer = Protocol.FAILED;
        }
        return new Response(HTTP_OK, MIME_PLAINTEXT, answer);
    }

    /**
     * Saves an uploaded file as "current time + original extension" in the given folder.
     * JHTTPServer has saveFileOnServerWithRandomName for this, but it joins the folder and the file
     * name with "\", so it only works on Windows.
     */
    private static String saveUploadedFile(Properties files, Properties params, String field, String directory) {
        String tempFile = files.getProperty(field);
        String originalName = params.getProperty(field, "");
        int dot = originalName.lastIndexOf('.');
        String fileName = System.currentTimeMillis() + (dot >= 0 ? originalName.substring(dot) : "");
        try {
            Path target = Path.of(directory, fileName);
            Files.createDirectories(target.getParent());
            Files.copy(Path.of(tempFile), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
