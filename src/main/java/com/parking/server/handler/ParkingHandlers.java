package com.parking.server.handler;

import com.parking.common.Protocol;
import com.parking.server.DbLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/** Vehicle entry / exit, tickets, booked slots and daily sale. */
public class ParkingHandlers {

    public String entry(Properties params, Properties files) throws SQLException {
        ResultSet rs = DbLoader.executeQuery("select * from parking");
        rs.moveToInsertRow();
        rs.updateString("vehicleno", params.getProperty("vehicleno"));
        rs.updateString("vehicletype", params.getProperty("vehicletype"));
        rs.updateString("dateofentry", params.getProperty("dateofentry"));
        rs.updateString("employeeid", params.getProperty("empid"));
        rs.updateString("timeofentry", params.getProperty("timeofentry"));
        rs.updateString("parkingbay", params.getProperty("parkingbay"));
        rs.updateString("location", params.getProperty("location"));
        rs.insertRow();

        // the ticket number is the id of the row that was just added
        ResultSet last = DbLoader.executeQuery("select max(pid) pid from parking");
        return last.next() ? String.valueOf(last.getInt("pid")) : Protocol.FAILED;
    }

    public String exit(Properties params, Properties files) throws SQLException {
        int ptid = Integer.parseInt(params.getProperty("ptid"));
        ResultSet rs = DbLoader.executeQuery("select * from parking where pid = " + ptid);
        if (!rs.next()) {
            return Protocol.FAILED;
        }
        rs.updateString("dateofexit", params.getProperty("exitdate"));
        rs.updateString("timeofexit", params.getProperty("exittime"));
        rs.updateInt("rent", Integer.parseInt(params.getProperty("rent")));
        rs.updateRow();
        return Protocol.SUCCESS;
    }

    public String ticketInfo(Properties params, Properties files) throws SQLException {
        int ptid = Integer.parseInt(params.getProperty("ptid"));
        ResultSet rs = DbLoader.executeQuery("SELECT * FROM parking inner join vehiclerent on parking.vehicletype = vehiclerent.vehicletype where pid = " + ptid);
        if (!rs.next()) {
            return Protocol.FAILED;
        }
        return String.join(Protocol.TILDE,
                rs.getString("vehicleno"),
                rs.getString("vehicletype"),
                rs.getString("dateofentry"),
                rs.getString("timeofentry"),
                rs.getString("location"),
                String.valueOf(rs.getInt("rentperhour")));
    }

    public String bookedSlots(Properties params, Properties files) throws SQLException {
        String parkingbay = params.getProperty("parkingbay");
        StringBuilder answer = new StringBuilder();
        ResultSet rs = DbLoader.executeQuery("select * from parking where parkingbay ='" + parkingbay + "' and dateofexit IS NULL and timeofexit IS NULL");
        while (rs.next()) {
            answer.append(String.join(Protocol.HASH,
                            rs.getString("location"),
                            rs.getString("vehicleno"),
                            rs.getString("dateofentry"),
                            rs.getString("timeofentry"),
                            rs.getString("employeeid")))
                    .append(Protocol.TILDE);
        }
        return answer.toString();
    }

    public String dailySale(Properties params, Properties files) throws SQLException {
        String today = new SimpleDateFormat(Protocol.DATE_FORMAT).format(new Date());
        StringBuilder answer = new StringBuilder();
        ResultSet rs = DbLoader.executeQuery("SELECT parkingbay,sum(rent) rent FROM parking where dateofentry = '" + today + "' group by parkingbay ");
        while (rs.next()) {
            answer.append(rs.getString("parkingbay")).append(Protocol.FIELD)
                    .append(rs.getString("rent")).append(Protocol.ROW);
        }
        return answer.toString();
    }
}
