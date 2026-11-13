package com.parking.server.handler;

import com.parking.common.Protocol;
import com.parking.server.DbLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/** Login and change password, for admins and employees. */
public class AccountHandlers {

    public String adminLogin(Properties params, Properties files) throws SQLException {
        String username = params.getProperty("username");
        String password = params.getProperty("password");
        return found("select * from admin where username = '" + username + "' and password = '" + password + "'");
    }

    public String employeeLogin(Properties params, Properties files) throws SQLException {
        String empid = params.getProperty("empid");
        String password = params.getProperty("password");
        return found("select * from employee where empid = '" + empid + "' and password = '" + password + "'");
    }

    public String adminChangePassword(Properties params, Properties files) throws SQLException {
        String username = params.getProperty("username");
        String oldpass = params.getProperty("oldpass");
        String newpass = params.getProperty("newpass");
        return changePassword("select * from admin where username ='" + username + "' and password= '" + oldpass + "'", newpass);
    }

    public String employeeChangePassword(Properties params, Properties files) throws SQLException {
        String empid = params.getProperty("eid");
        String oldpassword = params.getProperty("oldpassword");
        String newpassword = params.getProperty("newpassword");
        return changePassword("select * from employee where empid ='" + empid + "' and password= '" + oldpassword + "'", newpassword);
    }

    private String found(String query) throws SQLException {
        ResultSet rs = DbLoader.executeQuery(query);
        return rs.next() ? Protocol.SUCCESS : Protocol.FAILED;
    }

    private String changePassword(String query, String newPassword) throws SQLException {
        ResultSet rs = DbLoader.executeQuery(query);
        if (!rs.next()) {
            return Protocol.FAILED;
        }
        rs.updateString("password", newPassword);
        rs.updateRow();
        return Protocol.SUCCESS;
    }
}
