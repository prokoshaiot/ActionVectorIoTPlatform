/**
 * *********************************************************************
 * Software Developed by Merit Systems Pvt. Ltd., No. 42/1, 55/c, Nandi Mansion,
 * 40th Cross, Jayanagar 8th Block Bangalore - 560 070, India Work Created for
 * Merit Systems Private Limited All rights reserved
 *
 * THIS WORK IS SUBJECT TO INDIAN AND INTERNATIONAL COPYRIGHT LAWS AND TREATIES
 * NO PART OF THIS WORK MAY BE USED, PRACTICED, PERFORMED, COPIED, DISTRIBUTED,
 * REVISED, MODIFIED, TRANSLATED, ABRIDGED, CONDENSED, EXPANDED, COLLECTED,
 * COMPILED, LINKED, RECAST, TRANSFORMED OR ADAPTED WITHOUT THE PRIOR WRITTEN
 * CONSENT ANY USE OR EXPLOITATION OF THIS WORK WITHOUT AUTHORIZATION COULD
 * SUBJECT THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 * *********************************************************************
 */
package com.merit.connection;

import custmDBCon.ConnectionMap;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDAO {

    private static ConnectionMap conmap = new ConnectionMap();
    private static boolean initialized = true;
    private static Statement stat = null;
    private static ResultSet rs = null;
    private static Connection uniqueconnection = null;
    private static int trial = 0;

    /**
     * This method is used to get Connection by passing CustomerID in custmDBCon
     * package (API)
     */
    public static Connection getReaderConnection(String CustomerID) throws IOException, SQLException {

        try {
            if (initialized) {
                return conmap.getConntionWithID(CustomerID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Statement getConnectionStatement(String CustomerID) throws IOException, SQLException {

        try {
            if (initialized) {
                return conmap.getConntionStatementWithID(CustomerID);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized ResultSet executerQuery(String strQuery, String customerID) {
        if (trial < 3) {
            try {
                uniqueconnection = getReaderConnection(customerID);
                stat = uniqueconnection.createStatement();
                rs = stat.executeQuery(strQuery);
                System.out.println(" in trial : " + trial + " Got success while executing the query ..");
                //System.out.println(" in trial : " + trial + " Got success while executing the query [" + strQuery + "]");
                trial = 0;
                return rs;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(" in trial : " + trial + " Error Occured while executing the query ..");
                //System.out.println(" in trial : " + trial + " Error Occured while executing the query [" + strQuery + "]");
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                }
                trial++;
                conmap.updateMapforConntionWithID(customerID, null);
                return executerQuery(strQuery, customerID);
            }
        }
        return null;
    }

    public static void closeStatement() {
        try {
            if (stat != null) {
                stat.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        stat = null;
        rs = null;
    }
}