import java.sql.*;
import java.util.LinkedList;

public class Selects {

    String path = "C:\\Users\\solo1\\Desktop\\DMD_Assignment3";
    String dbName = "CarSharing.db";

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + path + "/" + dbName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public String getStatistics(String date, String from, String to) {

        String sql = "SELECT COUNT(*)" +
                " FROM ShapeOfPlug " +
                " INNER JOIN ChargeEvent C on ShapeOfPlug.SID = C.SID" +
                " WHERE " +
                " C.TIME BETWEEN time('" + from + "') AND time('" + to + "')" +
                " AND " +
                " C.date = date('" + date + "')" +
                " ORDER BY COUNT(*) DESC " +
                ";";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return "Sockets occupied ["+from+" - "+to+"]: " + rs.getString("COUNT(*)");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Error";
    }

    public void firstTask(String username, String color, String plateStarting) {

        String sql = "SELECT DISTINCT Car.plate " +
                "FROM Car " +
                "INNER JOIN Orders ON Orders.plate = Car.plate " +
                "WHERE " +
                "username = '"+username+"'" +
                " AND " +
                "Car.color = '"+color+"'" +
                " AND " +
                "Car.plate LIKE '"+plateStarting+"%'";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("plate") + " - plate of the possible car ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getDate(int i) {
        if (i <= 9) return "0"+i+":00";
        else if (i == 24) return "23:59";
        else return i+":00";

    }

    public void secondTask(String date) {
        System.out.println("Statistics on" + date);
        for (int i = 0; i < 24; i++) {
            System.out.println(getStatistics(date, getDate(i), getDate(i+1)));
        }
    }

    public void fourthTask(String username) {

        String sql = "SELECT Orders.OID, COUNT(*)" +
                "FROM Orders " +
                "INNER JOIN Payment ON Payment.OID = Orders.OID " +
                "WHERE " +
                "username = '"+username+"'" +
                " AND " +
                "IsPaid = 1" +
                " AND " +

                "Payment.date BETWEEN date('now', 'start of month') AND date('now', 'localtime')" +
                "GROUP BY Orders.OID, amount " +
                "HAVING COUNT(*) > 1 " +
                ";";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("OID") + ": | Paid " + rs.getString("COUNT(*)") + " times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void fifthTask(String inputdate) {

        String query = "SELECT AVG(distance_to_init_address)" +
                "FROM Trip " +
                "WHERE " +
                "starttime BETWEEN datetime('" + inputdate + "', 'start of day') AND datetime('" + inputdate + "', '+1 day', 'start of day')" +
                ";";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("AVG(distance_to_init_address)"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        query = "SELECT strftime('%H:%M',CAST (AVG(julianday(finishtime) - julianday(starttime)) AS REAL),'12:00')" +
                "FROM Trip " +
                "WHERE " +
                "starttime BETWEEN datetime('" + inputdate + "', 'start of day') AND datetime('" + inputdate + "', '+1 day', 'start of day')" +
                ";";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("strftime('%H:%M',CAST (AVG(julianday(finishtime) - julianday(starttime)) AS REAL),'12:00')"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sixthTask() {

        System.out.println("TOP 3 Most used initial addresses [07:00 - 10:00]");
        String sql = "SELECT COUNT(*), init_address FROM Orders " +
                "WHERE time BETWEEN time('07:00') AND time('10:00')" +
                "GROUP BY init_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("init_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("TOP 3 Most used arriving addresses  [07:00 - 10:00]");
        sql = "SELECT COUNT(*), arriving_address FROM Orders " +
                "WHERE time BETWEEN time('07:00') AND time('10:00')" +
                "GROUP BY arriving_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("arriving_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("TOP 3 Most used initial addresses [12:00 - 14:00]");
        sql = "SELECT COUNT(*), init_address FROM Orders " +
                "WHERE time BETWEEN time('12:00') AND time('14:00')" +
                "GROUP BY init_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("init_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("TOP 3 Most used arriving addresses  [12:00 - 14:00]");
        sql = "SELECT COUNT(*), arriving_address FROM Orders " +
                "WHERE time BETWEEN time('12:00') AND time('14:00')" +
                "GROUP BY arriving_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("arriving_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("TOP 3 Most used initial addresses [17:00 - 19:00]");
        sql = "SELECT COUNT(*), init_address FROM Orders " +
                "WHERE time BETWEEN time('17:00') AND time('19:00')" +
                "GROUP BY init_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("init_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("TOP 3 Most used arriving addresses  [17:00 - 19:00]");
        sql = "SELECT COUNT(*), arriving_address FROM Orders " +
                "WHERE time BETWEEN time('17:00') AND time('19:00')" +
                "GROUP BY arriving_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("arriving_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void seventhTask() {
        int limit = 0;
        if (getAmountofCars() != null) limit = (int) Math.ceil(Integer.parseInt(getAmountofCars()) * 0.1);
        System.out.println(getAmountofCars());

        String sql = "SELECT COUNT(*), plate FROM Orders " +
                "WHERE date BETWEEN date('now', '-3 months') AND date('now')" +
                "GROUP BY plate " +
                "ORDER BY COUNT(*) ASC " +
                "LIMIT " + limit + "" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("plate") + ": [" + rs.getString("COUNT(*)") + " times]");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getAmountofCars() {
        String sql = "SELECT COUNT(*) FROM Car;";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            String result = null;
            while (rs.next()) {
                result = rs.getString("COUNT(*)");
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
