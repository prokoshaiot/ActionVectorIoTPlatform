/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merit.dashboard.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 *
 * @author rekhas
 */
public class ResourceConfiguration {

    static Logger log = Logger.getLogger(ResourceConfiguration.class);
    static HashMap<String, ArrayList> resConfMap;

    public static boolean initialize(List<String> customerList) {
        ArrayList ccResConfList = null;
        try {
            resConfMap = new HashMap();
            for (String customer : customerList) {
                //call clouduser api to get resourceconfig from DB as XML
                String APIURL = "http://192.168.1.2:8084/CloudUserAPI/GetResourceConfig";
                URL obj = new URL(APIURL);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                int responseCode = con.getResponseCode();
                System.out.println("GetResourceConfig Response Code : " + responseCode);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String szResourceConfigXML = response.toString();
                //convert xml to string
                ccResConfList = getListFromXML(szResourceConfigXML);
                log.info("Adding configured value for==>>" + customer);
                resConfMap.put(customer, ccResConfList);
            }
            return true;
        } catch (Exception e) {
            log.error("Error in initialize()==>>" + e.toString());
            e.printStackTrace();
        }
        return false;
    }

    private static ArrayList getListFromXML(String szXML) {
        ArrayList resMap = new ArrayList();
        try {
            //SAXReader reader = new SAXReader();
            //Document document = reader.read(szXML);
            Document document = DocumentHelper.parseText(szXML);
            System.out.println("Total Configuration==>> " + document.selectNodes("//resourceconfig/customer").size());
            List nodeList = document.selectNodes("//resourceconfig/customer");
            Iterator nodeItr = nodeList.iterator();
            String ccustomer;
            String service;
            String resourceid;
            String paramName;
            String paramValue;
            Configuration szConf = null;
            while (nodeItr.hasNext()) {
                Node custNode = (Node) nodeItr.next();
                ccustomer = custNode.selectSingleNode("name").getText();
                service = custNode.selectSingleNode("service").getText();
                resourceid = custNode.selectSingleNode("resourceid").getText();
                paramName = custNode.selectSingleNode("paramname").getText();
                paramValue = custNode.selectSingleNode("paramvalue").getText();
                szConf = new Configuration(ccustomer, service, resourceid, paramName, paramValue);
                log.info("Adding configured value for==>>" + ccustomer + ":" + service + ":" + resourceid + ":" + paramName + "==>>" + paramValue);
                resMap.add(szConf);
            }
            return resMap;
        } catch (Exception e) {
            log.error("Error in getMapFromXML==>>" + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static String getConfValue(String customer, String ccustomer, String service, String resourceid, String paramName) {
        try {
            log.info("Get configured value for==>>" + customer + ":" + ccustomer + ":" + service + ":" + resourceid + ":" + paramName);
            ArrayList ccResConfList = (ArrayList) resConfMap.get(customer);
            log.info("ccResConfList==>>" + ccResConfList.size());
            for (Object szConfObj : ccResConfList) {
                Configuration szConf = (Configuration) szConfObj;
                if (szConf.compare(ccustomer, service, resourceid, paramName)) {
                    return szConf.confVal;
                }
            }
        } catch (Exception e) {
            log.error("Error in getConfValue==>>" + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    private static class Configuration {

        private String confKey;
        private String confVal;

        Configuration(String szCust, String szServ, String szRes, String pName, String pVal) {
            this.confKey = szCust + ":" + szServ + ":" + szRes + ":" + pName;
            this.confVal = pVal;
        }

        private boolean compare(String szCust, String szServ, String szRes, String pName) {
            try {
                if (confKey.equals(szCust + ":" + szServ + ":" + szRes + ":" + pName)) {
                    return true;
                }
            } catch (Exception e) {
                log.error("Error in compare()==>>" + e.toString());
                e.printStackTrace();
            }
            return false;
        }
    }
}