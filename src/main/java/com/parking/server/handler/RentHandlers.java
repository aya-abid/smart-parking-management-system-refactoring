package com.parking.server.handler;

import com.parking.common.Protocol;
import com.parking.server.DbLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/** Rent per hour of each vehicle type. */
public class RentHandlers {

    public String addRent(Properties params, Properties files) throws SQLException {
        String vehicletype = params.getProperty("vehicletype");
        int rentperhour = Integer.parseInt(params.getProperty("rentperhour"));

        ResultSet rs = DbLoader.executeQuery("select * from vehiclerent where vehicletype ='" + vehicletype + "'");
        if (rs.next()) {
            return Protocol.DUPLICATE;
        }
        rs.moveToInsertRow();
        rs.updateString("vehicletype", vehicletype);
        rs.updateInt("rentperhour", rentperhour);
        rs.insertRow();
        return Protocol.SUCCESS;
    }

    public String getRents(Properties params, Properties files) throws SQLException {
        StringBuilder answer = new StringBuilder();
        ResultSet rs = DbLoader.executeQuery("select * from vehiclerent");
        while (rs.next()) {
            answer.append(rs.getString("vehicletype")).append(Protocol.COMMA)
                    .append(rs.getString("rentperhour")).append(Protocol.ROW);
        }
        return answer.toString();
    }

    public String deleteRent(Properties params, Properties files) throws SQLException {
        String vehicletype = params.getProperty("vehicletype");
        ResultSet rs = DbLoader.executeQuery("select * from vehiclerent where vehicletype = '" + vehicletype + "'");
        if (!rs.next()) {
            return Protocol.FAILED;
        }
        rs.deleteRow();
        return Protocol.SUCCESS;
    }
}
