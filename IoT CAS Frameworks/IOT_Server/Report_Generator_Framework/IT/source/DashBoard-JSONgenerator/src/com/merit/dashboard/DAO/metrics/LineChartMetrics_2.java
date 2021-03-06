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
**********************************************************************
 */
package com.merit.dashboard.DAO.metrics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.merit.connection.ConnectionDAO;
import com.merit.dashboard.DBUtil.DBUtilHelper;

/**
 * *******************************************************************************************************
 * This class is giving the information from servicemetrics tables to generate
 * TimeLine Chart.
 *
 * *******************************************************************************************************
 */
public class LineChartMetrics_2 {

    static Logger log = Logger.getLogger(LineChartMetrics_2.class);

    /**
     * ***************************************************************************************************
     * This method is Query to Generate TimeLine Chart for using user Defined
     * array_agg()function for concatenating the column of a query Result
     * array_agg function in postgresql below 8 version we have to create by
     * following command:
     *
     * create aggregate array_agg ( sfunc = array_append, basetype = anyelement,
     * stype = anyarray, initcond = '{}' );
     *
     * Then sending to executeQueryForLineChart() to execute query and generate
     * HashMap for Generating Chart.
     *
     * @param resourceType Different category as
     * Desktop,Server,DataBase,Network,JVM
     * @param smilli date in second as long Timestamp(Current Time-selected Time
     * Period)
     * @param emilli date in second as long Timestamp(Current Time)
     * @param customer Selected customerID
     * ***************************************************************************************************
     */
    public String generateLineChart(String resourceType, long smilli, long emilli, String customer, String metrictype) {
        log.debug("==============================Endof LineChartMetricsSummary linechart===========================================");

        Statement stat = null;
        ResultSet rs = null;
        String szLineChartJson = "[]";
        System.out.println(">>>>>>>>>>>>>>>>>>>>> metrictype ==== " + metrictype);
        String szQueryDataPointsForBattery = "";
        if (metrictype == null || metrictype.equalsIgnoreCase("")) {
            szQueryDataPointsForBattery = ""
                    + "SELECT service,resourcetype,metrictype, array_agg(metricvalue) AS metricvalue,array_agg(sla) as slavalues,array_agg(to_char(totimestamp,'yyyy/MM/dd HH24:mi:SS')) as timestamp,host from "
                    + "( select service,resourcetype,metrictype,metricvalue,sla,to_timestamp(timestamp1) as totimestamp,host "
                    + "from servicemetrics  where "
                    + "timestamp1 between 1339046856 and 1370582856 and "
                    + "metricvalue is not null and "
                    + "resourceType='server' "
                    + "GROUP BY service,resourcetype,metrictype,metricvalue,sla,timestamp1,host "
                    + "ORDER By metrictype,totimestamp) as gh "
                    + "GROUP BY service,resourcetype,host,metrictype";

        } else {
            szQueryDataPointsForBattery = ""
                    + "SELECT service,resourcetype,metrictype, array_agg(metricvalue) AS metricvalue,array_agg(sla) as slavalues,array_agg(to_char(totimestamp,'yyyy/MM/dd HH24:mi:SS')) as timestamp,host "
                    + "from "
                    + "( select service,resourcetype,metrictype,metricvalue,sla,to_timestamp(timestamp1) as totimestamp,host "
                    + "from servicemetrics  where "
                    + "timestamp1 between " + smilli + " and " + emilli + " and "
                    + "metricvalue is not null and "
                    + "resourceType='" + resourceType + "' "
                    + "and metrictype in ( " + metrictype + ")"
                    + "GROUP BY service,resourcetype,metrictype,metricvalue,sla,timestamp1,host "
                    + "ORDER By metrictype,totimestamp) as gh "
                    + "GROUP BY service,resourcetype,host,metrictype";
        }
        try {
            log.debug("linechart query: /" + resourceType + "/ \n" + szQueryDataPointsForBattery);
            szLineChartJson = executeQueryForLineChart(szQueryDataPointsForBattery, customer);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("DashBoard LineCharMetrics  generateLineChart()  :" + e.getMessage());

        } finally {

            try {
                if (rs != null) {
//                    rs.close();
                    rs = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("DashBoard LineCharMetrics  generateLineChart()  :\n" + e.getMessage());

            }

        }
        return szLineChartJson;
    }

    /**
     * ***************************************************************************************************
     * Executing Query and making HashMap of
     * SLA,MetricValues,MetricNames,Timestamp then sending to
     * generateJsonForMultipleMetricType() to generate JSON for TimeLine
     * LineChart
     *
     * @param szQuery Query to Be Executed for Line Chart
     * @param customer Customer Id
     *
     * ***************************************************************************************************
     */
    public String executeQueryForLineChart(String szQuery, String customer) {
        Connection uniqueconnection = null;
        //Statement stat = null;
        ResultSet rs = null;
        String szLineChartJson = "[]";
        HashMap<String, String> map_metricName = new HashMap<String, String>();
        HashMap<String, String> map_metricValue = new HashMap<String, String>();
        HashMap<String, String> map_metricTimeStamp = new HashMap<String, String>();
        HashMap<String, String> map_metricSla = new HashMap<String, String>();
        try {
//            uniqueconnection = ConnectionDAO.getReaderConnection(customer);
//            stat = uniqueconnection.createStatement();
            
            

            rs = ConnectionDAO.executerQuery(szQuery, customer);//stat.executeQuery(szQuery);

            while (rs.next()) {
                if (map_metricSla.containsKey(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"))) {
                    String metricsla = map_metricSla.get(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"));
                    map_metricSla.remove(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"));
                    map_metricSla.put(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"), metricsla + "," + rs.getString("slavalues").replace("{", "[").replace("}", "]"));
                } else {
                    map_metricSla.put(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"), "[" + rs.getString("slavalues").replace("{", "[").replace("}", "]"));
                }
                if (map_metricName.containsKey(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"))) {
                    String metricname = map_metricName.get(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"));
                    map_metricName.remove(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"));
                    map_metricName.put(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"), metricname + ",\"" + DBUtilHelper.getMetrics_mapping_properties().getProperty(rs.getString("metrictype")) + "\"");
                } else {
                    map_metricName.put(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"), "[" + "\"" + DBUtilHelper.getMetrics_mapping_properties().getProperty(rs.getString("metrictype")) + "\"");
                }
                if (map_metricValue.containsKey(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"))) {
                    String metricname = map_metricValue.get(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"));
                    map_metricValue.remove(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"));
                    map_metricValue.put(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"), metricname + "," + rs.getString("metricvalue").replace("{", "[").replace("}", "]"));
                } else {
                    map_metricValue.put(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"), "[" + rs.getString("metricvalue").replace("{", "[").replace("}", "]"));
                }
                if (map_metricTimeStamp.containsKey(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"))) {
                    String sztimestamp = map_metricTimeStamp.get(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"));
                    map_metricTimeStamp.remove(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"));
                    map_metricTimeStamp.put(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"), sztimestamp + "," + rs.getString("timestamp").replace("{\"", "[\"").replace("\"}", "\"]"));
                } else {
                    map_metricTimeStamp.put(rs.getString("service") + "," + rs.getString("resourcetype") + "," + rs.getString("host"), rs.getString("timestamp").replace("{\"", "[\"").replace("\"}", "\"]"));
                }
            }
            if (map_metricName.size() != 0) {
                szLineChartJson = generateJsonForMultipleMetricType(map_metricSla, map_metricName, map_metricValue, map_metricTimeStamp);
            }
            map_metricName = null;
            map_metricValue = null;
            map_metricTimeStamp = null;
            map_metricSla = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionDAO.closeStatement();
        }
        return szLineChartJson;
    }

    /**
     * ***************************************************************************************************
     * creating JSON For Line Chart and Sending generated JSON to SendFileToJson
     * class
     *
     * @param Metricsla HashMap having SLAValues
     * @param MetricName HashMap having MetricNames
     * @param MetricValue HashMap having MetricValues
     * @param MetricTimeStamp HashMap having Timestamp
     *
     * ***************************************************************************************************
     */
    @SuppressWarnings("rawtypes")
    public String generateJsonForMultipleMetricType(HashMap<String, String> Metricsla, HashMap<String, String> MetricName, HashMap<String, String> MetricValue, HashMap<String, String> MetricTimeStamp) {
        String Json_data = "[";
        Set set = MetricName.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {

            Map.Entry entry = (Map.Entry) it.next();
            String host_key = (String) entry.getKey();
            String[] serviceResourcetypehost = host_key.split(",");
            String host_MetricName = (String) entry.getValue() + "]";
            String host_MetricValue = (String) MetricValue.get(host_key) + "]";
            String host_timestamp = (String) MetricTimeStamp.get(host_key);
            Json_data = Json_data + "{ \"ServiceName\":\"" + serviceResourcetypehost[0] + "\",\"ResourceType\":\"" + serviceResourcetypehost[1] + "\",\"ServerName\":\"" + serviceResourcetypehost[2] + "\",\"TimeStamps\":[" + host_timestamp + "],\"ResourceNames\":" + host_MetricName + ",\"ResourceValues\":" + host_MetricValue + ",\"SlaValues\":" + Metricsla.get(host_key) + "],\"ResourceID\":\"" + host_key + "\"},";
        }
        Json_data = Json_data.substring(0, Json_data.lastIndexOf(",")) + "]";

        return Json_data;
        //SendFileToJson file_send_jvmheap = new SendFileToJson(customer,timestampselection, resourcetype, json_Filename, Json_data);
    }
}