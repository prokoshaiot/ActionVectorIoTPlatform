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
package com.dashboard.linechart.moving;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.merit.connection.ConnectionDAO;
import com.merit.dashboard.DBUtil.DBUtilHelper;
import com.merit.dashboard.util.PropertyUtil;

/**
 * Servlet implementation class TestListChart
 */
public class DynamicChart extends HttpServlet {

    private static final long serialVersionUID = 1L;
    Connection con = null;

    public void init() throws ServletException {
        try {
            System.out.println("Init( ) method Called....");
            //con = ConnectionDAO.getConnection(DBUtilHelper.getMetrics_mapping_properties().getProperty("domainName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    /**
     * ******************************************************************************
     * This method is used to get Json for generating live linechart
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return jsonResult for generating Live LineChart
     * *******************************************************************************
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Statement st;
        ResultSet rs;
        PropertyUtil propertyUtil = null;
        try {
            propertyUtil = new PropertyUtil();
            String maxTimeStamp = "";
            String host = request.getParameter("HostName");
            String resourceId = request.getParameter("ResourceId");
            String metricslistfromjs = request.getParameter("MetricsList");
            String[] metricslistSplit = metricslistfromjs.split(",");
            String metricslist = "";
            for (int i = 0; i < metricslistSplit.length; i++) {
                metricslist = metricslist + ",\'" + propertyUtil.getreverseMapping_Properties().get(metricslistSplit[i]) + "\'";
                if (maxTimeStamp.equals("")) {
                    String szExecuteQuery = "select max(timestamp1) as timestamp from servicemetrics where metrictype in ('" 
                            + propertyUtil.getreverseMapping_Properties().get(metricslistSplit[i]) + "\'" + ") and host = '" + host 
                            + "' and resourceid = '" + resourceId + "'";
                    maxTimeStamp = getMaxTimestampOfMetrictype(szExecuteQuery, request);
                }
            }
            String metricNames_array = "";
            String metricValue_array = "";
            String timestamp_array = "";

            st = ConnectionDAO.getConnection(request.getServerName()).createStatement();
            /*String szQuery = "select metrictype,round(cast(metricvalue as numeric),2) as metricvalue,to_char(to_timestamp(timestamp1) at time zone " 
                    + DBUtilHelper.getMetrics_mapping_properties().getProperty("ServerTimeZone_timestamp") + ",'HH24:mi:SS') as timestamp from "
                    + "servicemetrics where metrictype in (" + metricslist.substring(1) + ") and host = '" + host + "' and resourceid='" + resourceId 
                    + "' and timestamp1 = " + maxTimeStamp;*/
            String szQuery = "select metrictype,round(cast(metricvalue as numeric),2) as metricvalue,to_char(to_timestamp(timestamp1),'HH24:mi:SS') "
                    + "as timestamp from servicemetrics where metrictype in (" + metricslist.substring(1) + ") and host = '" + host + "' and resourceid='" 
                    + resourceId + "' and timestamp1 = " + maxTimeStamp;
            rs = st.executeQuery(szQuery);
            System.out.println(szQuery);
            String jsonResult = "[";
            while (rs.next()) {

                metricNames_array += "\"" + rs.getString("metrictype") + "\",";
                metricValue_array += rs.getString("metricvalue") + ",";
                timestamp_array = "\"" + rs.getString("timestamp") + "\"";
            }
            if (metricNames_array != "") {
                jsonResult = jsonResult + "{\"MetricValue\":[" + metricValue_array.substring(0, (metricValue_array.length() - 1)) + "],\"timestamp\":[" 
                        + timestamp_array + "],\"MetricName\":[" + metricNames_array.substring(0, (metricNames_array.length() - 1)) + "]}";
                jsonResult = jsonResult + "]";
            } else {
                jsonResult = "[{}]";
            }
            System.out.println("Json ==  = " + jsonResult);
            PrintWriter pw = response.getWriter();
            response.setContentType("text/html");
            pw.write(jsonResult);
            propertyUtil = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ******************************************************************************
     * This method is used to get Max Timestamp for generating live linechart
     *
     * @param szExecuteQuery Query to get max Timestamp
     * @return szExecutedString returning Max Timestamp
     * *******************************************************************************
     */
    public String getMaxTimestampOfMetrictype(String szExecuteQuery, HttpServletRequest request) throws IOException, SQLException {
        String szExecutedString = "";
        try {
            Statement stat1 = ConnectionDAO.getConnection(request.getServerName()).createStatement();
            ResultSet rs2 = stat1.executeQuery(szExecuteQuery);
            while (rs2.next()) {
                szExecutedString = rs2.getString("timestamp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if (szExecutedString == null) {
            szExecutedString = "";
        }
        return szExecutedString;
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    }
    
}