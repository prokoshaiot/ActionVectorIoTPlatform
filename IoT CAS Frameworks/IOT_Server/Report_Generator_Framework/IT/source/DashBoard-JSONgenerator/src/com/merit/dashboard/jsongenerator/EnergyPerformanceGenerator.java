/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merit.dashboard.jsongenerator;

import com.merit.connection.ConnectionDAO;
import com.merit.dashboard.queryexecuter.QueryExecuter;
import com.merit.dashboard.util.ResourceConfiguration;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author rekhas
 */
public class EnergyPerformanceGenerator {

    static Logger log = Logger.getLogger(EnergyPerformanceGenerator.class);

    public static String generateJSON(String resType, String metricType, String configuredMetricType,
            long sMilli, long eMilli, String timeStampSelection, String customer) {
        String resultJson = "";
        String szQuery = "";
        try {
            metricType = metricType.replaceFirst("XXX", timeStampSelection);
            configuredMetricType = configuredMetricType.replaceFirst("XXX", timeStampSelection);
            log.info("metricType==>>" + metricType);
            log.info("configuredMetricType==>>" + configuredMetricType);
            log.info("resType==>>" + resType);
            String derivedMetricType = null;
            String[] headingNamesMetric1 = null;
            szQuery = "SELECT t.service as ServiceName, t.metricvalue as Efficiency, t.metricvalue as ActualEnergy, t.resourcetype as ResourceType, t.metrictype,"
                    + " t.timestamp1 FROM (SELECT service, resourcetype, metrictype, MAX(timestamp1) as MaxTime FROM servicemetrics "
                    + "where resourcetype='" + resType + "' and metrictype='" + metricType + "' GROUP BY service,resourcetype,metrictype) r "
                    + "INNER JOIN servicemetrics t ON t.service = r.service AND t.resourcetype = r.resourcetype AND "
                    + "t.timestamp1 = r.MaxTime AND t.metrictype = r.metrictype";
            headingNamesMetric1 = new String[]{"ServiceName", "ResourceType", "Efficiency", "ActualEnergy", "ExpectedEnergy"};
            derivedMetricType = "Efficiency";
            log.info("EfficiencyGenerator query==>>" + szQuery);
            resultJson = generateDerivedJsonFromGivenQuery(szQuery, headingNamesMetric1, customer, configuredMetricType,
                    derivedMetricType);
            return resultJson;
        } catch (Exception e) {
            log.error("Error in generateJSON==>>" + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static String generateDerivedJsonFromGivenQuery(String szQuery, String[] headingNames, String customer,
            String configuredMetricType, String derivedMetricType) {
        String perfJSON = "";
        ResultSet rs4;
        try {
            String szMetricTypeValueJson = "";
            perfJSON = "[";
            rs4 = ConnectionDAO.executerQuery(szQuery, customer);
            while (rs4.next()) {
                String szConcatColumn = "";
                szMetricTypeValueJson = "";
                String confVal = "";
                String derivedVal = "";
                for (int i = 0; i < headingNames.length - 1; i++) {
                    if (rs4.getString(headingNames[i]) != null) {
                        if (headingNames[i].equals(derivedMetricType)) {
                            confVal = ResourceConfiguration.getConfValue(customer, customer, rs4.getString("ServiceName"),
                                    null, configuredMetricType);
                            log.info("confVal1==>>" + confVal);
                            derivedVal = "" + Double.parseDouble(rs4.getString(headingNames[i])) / Double.parseDouble(confVal) * 100;
                            log.info("derivedVal1==>>" + derivedVal);
                            szConcatColumn = szConcatColumn + ",\"" + headingNames[i] + "\":\"" + derivedVal + "\"";
                        } else if (headingNames[i].equals("ExpectedEnergy")) {
                            confVal = ResourceConfiguration.getConfValue(customer, customer, rs4.getString("ServiceName"),
                                    null, configuredMetricType);
                            log.info("confVal1==>>" + confVal);
                            szConcatColumn = szConcatColumn + ",\"" + headingNames[i] + "\":\"" + confVal + "\"";
                        } else {
                            szConcatColumn = szConcatColumn + ",\"" + headingNames[i] + "\":\"" + rs4.getString(headingNames[i]) + "\"";
                        }
                    }
                    szMetricTypeValueJson = szMetricTypeValueJson + "," + rs4.getString(headingNames[i]);
                }
                if (headingNames[headingNames.length - 1].equals(derivedMetricType)) {
                    confVal = ResourceConfiguration.getConfValue(customer, customer, rs4.getString("ServiceName"),
                            null, configuredMetricType);
                    log.info("confVal2==>>" + confVal);
                    derivedVal = "" + Double.parseDouble(rs4.getString(headingNames[headingNames.length - 1]))
                            / Double.parseDouble(confVal) * 100;
                    log.info("derivedVal2==>>" + derivedVal);
                } else if (headingNames[headingNames.length - 1].equals("ExpectedEnergy")) {
                    derivedVal = ResourceConfiguration.getConfValue(customer, customer, rs4.getString("ServiceName"),
                            null, configuredMetricType);
                    log.info("confVal2==>>" + derivedVal);
                } else {
                    derivedVal = rs4.getString(headingNames[headingNames.length - 1]);
                }
                szConcatColumn = szConcatColumn + ",\"" + headingNames[headingNames.length - 1] + "\":\"" + derivedVal + "\"";
                perfJSON += "{" + szConcatColumn.substring(1) + "},";
            }
            if (perfJSON.length() > 1) {
                perfJSON = perfJSON.substring(0, perfJSON.length() - 1);
            }
            perfJSON += "]";
            log.info("performance json==>>" + perfJSON);
            return perfJSON;
        } catch (Exception e) {
            log.error("Error in generateDerivedJsonFromGivenQuery" + e.toString());
            e.printStackTrace();
        } finally {
            ConnectionDAO.closeStatement();
            rs4 = null;
        }
        return null;
    }
}
