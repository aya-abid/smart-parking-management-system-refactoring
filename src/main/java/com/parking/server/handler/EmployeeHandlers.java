package com.parking.server.handler;

import com.parking.common.Protocol;
import com.parking.server.DbLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/** Add employees and list them. */
public class EmployeeHandlers {

    /** folder where the photos are saved, relative to the folder the server runs from */
    public static final String PHOTO_DIR = "src/content";

    /** Saves an uploaded file and returns the name it was saved with. */
    @FunctionalInterface
    public interface FileSaver {
        String save(Properties files, Properties params, String field, String directory);
    }

    private final FileSaver fileSaver;

    public EmployeeHandlers(FileSaver fileSaver) {
        this.fileSaver = fileSaver;
    }

    public String addEmployee(Properties params, Properties files) throws SQLException {
        String employeeid = params.getProperty("employeeid");
        String filename = fileSaver.save(files, params, "photo", PHOTO_DIR);

        ResultSet rs = DbLoader.executeQuery("select * from employee where empid='" + employeeid + "'");
        if (rs.next()) {
            return Protocol.FAILED;
        }
        rs.moveToInsertRow();
        rs.updateString("empid", employeeid);
        rs.updateString("name", params.getProperty("name"));
        rs.updateString("fathername", params.getProperty("fathername"));
        rs.updateString("password", params.getProperty("password"));
        rs.updateString("photo", PHOTO_DIR + "/" + filename);
        rs.updateString("address", params.getProperty("address"));
        rs.updateString("mobileno", params.getProperty("mobile"));
        rs.insertRow();
        return Protocol.SUCCESS;
    }

    public String allEmployees(Properties params, Properties files) throws SQLException {
        StringBuilder answer = new StringBuilder();
        ResultSet rs = DbLoader.executeQuery("select * from employee");
        while (rs.next()) {
            answer.append(String.join(Protocol.FIELD,
                            rs.getString("empid"),
                            rs.getString("name"),
                            rs.getString("fathername"),
                            rs.getString("mobileno"),
                            rs.getString("address"),
                            rs.getString("photo")))
                    .append(Protocol.ROW);
        }
        return answer.toString();
    }
}
