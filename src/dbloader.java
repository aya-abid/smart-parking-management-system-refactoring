
import java.sql.*;

class dbloader {

    static ResultSet executeQuery(String sqlstatement) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("driver loaded succefully");

            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/db1", "root", "");
            System.out.println("connection built");

            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("statement created");

            ResultSet rs = stmt.executeQuery(sqlstatement);
            System.out.println("result set created");
            return rs;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
       // executeQuery("select * from admin");
       String password =   ((int)(10000 + (99999 - 10000)* Math.random())) + "";
        System.out.println(password);
    }

}
