package custmDBCon;

import custmDBCon.ConnectionMap;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author vijayasri
 */
public class ConnectionDAO {

    private static ConnectionMap conmap = new ConnectionMap();
    private static boolean initialized = false;
    private static final Logger log = Logger.getLogger(ConnectionDAO.class.getName());

    /*private static Connection con = null;
    static Properties properties = null;
    static FileInputStream fis = null;
    static String driverName = null;
    static String urlPath = null;
    static String userID = null;
    static String pwd = null;*/
    public static Connection getConnection(String CustomerID) throws IOException, SQLException {

        try {
            if (!initialized) {
                Connection con = conmap.getConntionWithID(CustomerID);;
                con.commit();
                return con;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
