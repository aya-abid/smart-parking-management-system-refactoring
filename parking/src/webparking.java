
import com.vmm.JHTTPServer;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class webparking extends JHTTPServer {

    public webparking(int port) throws IOException {
        super(port);
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {

        Response res = null;
        String ans = null;

        System.out.println("URI " + uri);

        if (uri.contains("/GetResource")) //request should be of the form /GetResource/one.jpg
        {
            uri = uri.substring(1);
            uri = uri.substring(uri.indexOf("/") + 1);
            System.out.println(uri + " *** ");
            res = sendCompleteFile(uri);
        } else if (uri.contains("/StreamMedia")) //request should be of the form /GetResource/one.jpg
        {
            uri = uri.substring(1);
            uri = uri.substring(uri.indexOf("/") + 1);
            System.out.println(uri + " *** ");
            res = streamFile(uri, method, header);
        } else if (uri.contains("/adminlogin")) //any custom logic , except downloading and streaming 
        {
            String username = parms.getProperty("username");
            String password = parms.getProperty("password");
            ResultSet rs = dbloader.executeQuery("select * from admin where username = '" + username + "' and password = '" + password + "'");
            ans = "";
            try {
                if (rs.next()) {
                    ans = "success";
                } else {
                    ans = "failed";
                }
            } catch (SQLException ex) {
                Logger.getLogger(webparking.class.getName()).log(Level.SEVERE, null, ex);
            }
            res = new Response(HTTP_OK, "text/plain", ans);
        } else if (uri.contains("/employeelogin")) //any custom logic , except downloading and streaming 
        {
            String empid = parms.getProperty("empid");
            String password = parms.getProperty("password");
            ResultSet rs = dbloader.executeQuery("select * from employee where empid = '" + empid + "' and password = '" + password + "'");
            ans = "";
            try {
                if (rs.next()) {
                    ans = "success";
                } else {
                    ans = "failed";
                }
            } catch (SQLException ex) {
                Logger.getLogger(webparking.class.getName()).log(Level.SEVERE, null, ex);
            }
            res = new Response(HTTP_OK, "text/plain", ans);
        } else if (uri.contains("/employeechangepassword")) {
            String empid = parms.getProperty("eid");
            String oldpassword = parms.getProperty("oldpassword");
            String newpassword = parms.getProperty("newpassword");
            ResultSet rs = dbloader.executeQuery("select * from employee where empid ='" + empid + "' and password= '" + oldpassword + "'");

            try {
                if (rs.next()) {
                    rs.updateString("password", newpassword);
                    rs.updateRow();
                    ans = "success";
                } else {
                    ans = "failed";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);
        } else if (uri.contains("/adminchangepassword")) {
            String username = parms.getProperty("username");
            String oldpass = parms.getProperty("oldpass");
            String newpass = parms.getProperty("newpass");
            ResultSet rs = dbloader.executeQuery("select * from admin where username ='" + username + "' and password= '" + oldpass + "'");

            try {
                if (rs.next()) {
                    rs.updateString("password", newpass);
                    rs.updateRow();
                    ans = "success";
                } else {
                    ans = "failed";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);
        } else if (uri.contains("/Parkingrent")) {
            String vehicletype = parms.getProperty("vehicletype");
            int rentperhour = Integer.parseInt(parms.getProperty("rentperhour"));
            ResultSet rs = dbloader.executeQuery("select * from vehiclerent where vehicletype ='" + vehicletype + "'");
            try {
                if (rs.next()) {
                    ans = "duplicate";
                } else {
                    rs.moveToInsertRow();
                    rs.updateString("vehicletype", vehicletype);
                    rs.updateInt("rentperhour", rentperhour);
                    rs.insertRow();
                    ans = "success";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);
        } else if (uri.contains("/Parkingentry")) {
            String vehicletype = parms.getProperty("vehicletype");
            String empid = parms.getProperty("empid");
            String vehicleno = parms.getProperty("vehicleno");
            String dateofentry = parms.getProperty("dateofentry");
            String timeofentry = parms.getProperty("timeofentry");
            String parkingbay = parms.getProperty("parkingbay");
            String location = parms.getProperty("location");
            ResultSet rs = dbloader.executeQuery("select * from parking");
            try {
                rs.moveToInsertRow();
                rs.updateString("vehicleno", vehicleno);
                rs.updateString("vehicletype", vehicletype);
                rs.updateString("dateofentry", dateofentry);
                rs.updateString("employeeid", empid);
                rs.updateString("timeofentry", timeofentry);
                rs.updateString("parkingbay", parkingbay);
                rs.updateString("location", location);

                rs.insertRow();

                ResultSet rs2 = dbloader.executeQuery("select max(pid) pid from parking");
                if (rs2.next()) {
                    ans = rs2.getInt("pid") + "";
                } else {
                    ans = "failed";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);

        } else if (uri.contains("/Parkingexit")) {
            int ptid = Integer.parseInt(parms.getProperty("ptid"));
            String exitdate = parms.getProperty("exitdate");
            String exittime = parms.getProperty("exittime");
            int rent = Integer.parseInt(parms.getProperty("rent"));
            ResultSet rs = dbloader.executeQuery("select * from parking where pid = " + ptid);
            try {
                if (rs.next()) {
                    rs.updateString("dateofexit", exitdate);
                    rs.updateString("timeofexit", exittime);
                    rs.updateInt("rent", rent);
                    rs.updateRow();
                    ans = "success";
                } else {
                    ans = "failed";
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);

        } else if (uri.contains("/getticketinfo")) {
            int ptid = Integer.parseInt(parms.getProperty("ptid"));
            ans = "";
            ResultSet rs = dbloader.executeQuery("SELECT * FROM parking inner join vehiclerent on parking.vehicletype = vehiclerent.vehicletype where pid = " + ptid);
            try {
                if (rs.next()) {
                    ans += rs.getString("vehicleno") + "~~~~"
                            + rs.getString("vehicletype") + "~~~~"
                            + rs.getString("dateofentry") + "~~~~"
                            + rs.getString("timeofentry") + "~~~~"
                            + rs.getString("location") + "~~~~"
                            + rs.getInt("rentperhour");
                } else {
                    ans = "failed";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);

        } else if (uri.contains("/getbookedslots")) {
            String parkingbay = parms.getProperty("parkingbay");
            ans = "";
            ResultSet rs = dbloader.executeQuery("select * from parking where parkingbay ='" + parkingbay + "' and dateofexit IS NULL and timeofexit IS NULL");
            try {
                while (rs.next()) {
                    ans += rs.getString("location") + "####"
                            + rs.getString("vehicleno") + "####"
                            + rs.getString("dateofentry") + "####"
                            + rs.getString("timeofentry") + "####"
                            + rs.getString("employeeid") + "~~~~";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);
        } else if (uri.contains("/GetParkingrent")) {
            ans = "";
            ResultSet rs = dbloader.executeQuery("select * from vehiclerent");
            try {
                while (rs.next()) {
                    ans = ans + rs.getString("vehicletype") + "," + rs.getString("rentperhour") + "~!@";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);
        } else if (uri.contains("/deleteparkingrent")) {
            ans = "";
            String vehicletype = parms.getProperty("vehicletype");
            ResultSet rs = dbloader.executeQuery("select * from vehiclerent where vehicletype = '" + vehicletype + "'");
            try {
                if (rs.next()) {
                    rs.deleteRow();
                    ans = "success";
                } else {
                    ans = "failed";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);
        } else if (uri.contains("/AddEmployee")) {
            String employeeid = parms.getProperty("employeeid");
            String name = parms.getProperty("name");
            String fathername = parms.getProperty("fathername");
            String address = parms.getProperty("address");
            String mobile = parms.getProperty("mobile");
            String password = parms.getProperty("password");

            String filename = "";
            filename = saveFileOnServerWithRandomName(files, parms, "photo", "src/content");

            ans = "";
            try {
                String emloyeeid = null;
                ResultSet rs = dbloader.executeQuery("select * from employee where empid='" + emloyeeid + "'");
                if (rs.next()) {
                    ans = "failed";
                } else {
                    rs.moveToInsertRow();
                    rs.updateString("empid", employeeid);
                    rs.updateString("name", name);
                    rs.updateString("fathername", fathername);
                    rs.updateString("password", password);
                    rs.updateString("photo", "src/content/" + filename);
                    rs.updateString("address", address);
                    rs.updateString("mobileno", mobile);

                    rs.insertRow();
                    ans = "success";
                }
                res = new Response(HTTP_OK, "text/plain", ans);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (uri.contains("/getallemployees")) {
            ans = "";
            try {
                String emloyeeid = null;
                ResultSet rs = dbloader.executeQuery("select * from employee");
                while (rs.next()) {
                    ans += rs.getString("empid") + "#$%" + rs.getString("name") + "#$%" + rs.getString("fathername") + "#$%" + rs.getString("mobileno") + "#$%" + rs.getString("address") + "#$%" + rs.getString("photo") + "~!@";
                }
                res = new Response(HTTP_OK, "text/plain", ans);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (uri.contains("/getdailysale")) {
            try {
                String today = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
                ResultSet rs = dbloader.executeQuery("SELECT parkingbay,sum(rent) rent FROM parking where dateofentry = '" + today + "' group by parkingbay ");
                ans = "";
                while (rs.next()) {
                        ans += rs.getString("parkingbay") + "#$%" + rs.getString("rent") + "~!@";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            res = new Response(HTTP_OK, "text/plain", ans);
        } else if (uri.contains("SignUp")) {
            String email = parms.getProperty("email");
            String pass = parms.getProperty("pass");
            String name = parms.getProperty("name");

            String filename = "";
//            filename = saveFileOnServerWithOriginalName(files, parms, "photo", "E:\\megha\\uploaded_files");
            filename = saveFileOnServerWithRandomName(files, parms, "photo", "src/uploaded_pics");

            ans = "";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/abc", "root", "system");
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery("select * from users where email='" + email + "'");
                if (rs.next()) {
                    ans = "User Already Exists";
                } else {
                    rs.moveToInsertRow();
                    rs.updateString("email", email);
                    rs.updateString("name", name);
                    rs.updateString("pass", pass);
                    rs.updateString("photo", "src/uploaded_pics/" + filename);
                    rs.insertRow();
                    ans = "Signup successfull";
                }
                res = new Response(HTTP_OK, "text/plain", ans);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (uri.contains("/FetchProfile")) {
            String email = parms.getProperty("email");
            String result = "";
            try {

                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/abc", "root", "system");
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery("select * from users where email='" + email + "'");
                if (rs.next()) {
                    String name = rs.getString("name");
                    String password = rs.getString("pass");
                    String photo = rs.getString("photo");

                    result = result + name + ";#" + password + ";#" + photo;

                } else {
                    result = "No Such User";
                }

                res = new Response(HTTP_OK, "text/plain", result);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (uri.contains("/UploadMultipleFiles")) {
            String title = parms.getProperty("title");
            String desc = parms.getProperty("desc");

            ans = "";
            ans = ans + " title : " + title + "\n";
            ans = ans + " desc : " + desc + "\n";
            String filename = "";

            Enumeration e = files.propertyNames();
            while (e.hasMoreElements()) {
                String name_of_1_file = e.nextElement().toString();

                filename = saveFileOnServerWithRandomName(files, parms, name_of_1_file, "src/uploaded_pics");

                ans = ans + name_of_1_file + " : " + filename + "\n";

            }
            res = new Response(HTTP_OK, "text/plain", "Files Uploaded successfully\n" + ans);

            return res;
        } else {
            res = new Response(HTTP_OK, "text/html", "<body style='background: #D46A6A; color: #fff'><center><h1>Hello</h1><br> <h3>Welcome To JHTTP Server (Version 1.0)</h3></body></center>");
        }

        return res;

    }
}
