package com.prokosha.adapter.etl.ETLAdapter;

import contextSetters.GangliaContext;
import java.text.DecimalFormat;
import java.util.*;
import org.apache.log4j.Logger;


/*
 *
 * @author Anand
 */
public class GangilaParser {

    Logger log = Logger.getLogger(CEPEvent.class.getName());
    HashMap<String, ReportData> metricValueMap = new HashMap<String, ReportData>();
    HashMap<String, ReportData> metricValueMap1 = new HashMap<String, ReportData>();
    ETLAdapter etl = null;
    int insertStatus = 0;
    PersistReportData dataInsert = null;
    static Properties properties = null;
    static Enumeration enum1 = null;
    String metricValue = null;
    String eventID = null;
    static boolean mapInitialized = false;

    public double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");// like 2.35 for     // 2.356789..
        return Double.valueOf(twoDForm.format(d));
    }

    public GangilaParser(String CEPEvent, String metrics, Properties properties, String eventID) {
        initialize(CEPEvent, metrics, properties, eventID);
    }

    public void initialize(String CEPEvent, String metrics, Properties properties, String eventID) {

        String metricsToken[] = null;

        String timestamp = null;
        String startTIme = null;
        String endTime = null;
        String size = null;
        String hostName = null;
        String status = null;
        String resourceId = null;
        String customerID = null;
        String resourceType=null;


        double diskTotal = 0.0;
        double diskFree = 0.0;

        double memFree = 0.0;
        double memTotal = 0.0;
        double memCached=0.0;
        double memBuffered=0.0;
        Double mem_usg = 0.0;
        Double disk_boot = 0.0;

        double tmax = 0;
        double tn = 0;
        double cpuUser = 0.0;
        double cpuSystem = 0.0;
        double cpuUtilization = 0.0;


        ReportData reportData = null;

        String metricType = null;
        String metricToken[] = null;
        GangliaContext contextSetter = new GangliaContext();
      



        this.eventID = eventID;

        try {
                    Map<String,String> mp_string_to_map=convertStringToMap(CEPEvent);
                    String parseToken[] = CEPEvent.split(",");
                    metricsToken = metrics.split(" ");
                    dataInsert = new PersistReportData();
                    String defaultKeyConstants[]=CEPEventMetricsMapping.getSzdefaultConstants();
            
                    customerID = mp_string_to_map.get(defaultKeyConstants[3].trim());
                
                    hostName = mp_string_to_map.get(defaultKeyConstants[10]);
              
                    timestamp = mp_string_to_map.get(defaultKeyConstants[11]);
                    System.out.println("timestamp in ETL from ganglia::"+timestamp);
                 
                    tmax = Double.parseDouble(mp_string_to_map.get(defaultKeyConstants[6]));
               
                    tn = Double.parseDouble(mp_string_to_map.get(defaultKeyConstants[7]));
               
                    cpuUser = Double.parseDouble(mp_string_to_map.get(defaultKeyConstants[8]));
          
                    resourceType =mp_string_to_map.get(defaultKeyConstants[4]);
               
                    cpuSystem =Double.parseDouble(mp_string_to_map.get(defaultKeyConstants[9]));
          
                    resourceId =mp_string_to_map.get(defaultKeyConstants[2]);
               


            if (tn != -1.1 && tmax != -1.1) {//State UP Down EVENT
                if (tn > 4 * tmax) { //server is down
                    System.out.println("*******Ganglia Down Event Found*******");

                    reportData = new ReportData();

                    metricType = properties.getProperty("Availability");
                    metricToken = metricType.split(",");

                    reportData.setCategory(metricToken[1]);
                    reportData.setHost(hostName);
                    reportData.setMetricType(metricToken[0]);
                    reportData.setTimestamp1(timestamp);
                    reportData.setValue("DOWN");
                    reportData.setResourceId(resourceId);
                    reportData.setEventID(eventID);
                    reportData.setResourceType(resourceType);
                    metricValueMap1.put("Avail", reportData);

                    metricValueMap1 = contextSetter.setContextsInMap(metricValueMap1,customerID);
                    dataInsert.sendAvailToDatabse(metricValueMap1,customerID);

                } else { //server is up

                    reportData = new ReportData();

                    metricType = properties.getProperty("Availability");
                    metricToken = metricType.split(",");

                    reportData.setCategory(metricToken[1]);
                    reportData.setHost(hostName);
                    reportData.setMetricType(metricToken[0]);
                    reportData.setTimestamp1(timestamp);
                    reportData.setValue("UP");
                    reportData.setResourceId(resourceId);
                    reportData.setEventID(eventID);
                    reportData.setResourceType(resourceType);
                    metricValueMap1.put("Avail", reportData);

                    metricValueMap1 = contextSetter.setContextsInMap(metricValueMap1,customerID);
                    dataInsert.sendAvailToDatabse(metricValueMap1,customerID);

                }

            }
            


            if (cpuUser != -1.1 && cpuSystem != -1.1) {//cpuUtilization Event
                reportData = new ReportData();
                cpuUtilization = cpuUser + cpuSystem;
                //log.debug("cpuUser=="+cpuUtilization);

                metricType = properties.getProperty("cpuutilization");
                metricToken = metricType.split(",");
                reportData.setCategory(metricToken[1]);
                reportData.setMetricType(metricToken[0]);
                reportData.setHost(hostName);
                reportData.setTimestamp1(timestamp);
                reportData.setValue(Double.toString(cpuUtilization));
                reportData.setResourceId(resourceId);
                reportData.setEventID(eventID);
                reportData.setResourceType(resourceType);
                metricValueMap.put("utilization", reportData);
            }

            for (int i = 0; i < metricsToken.length; i++) {//Disk,Load1,Load5,Load15,Memory Events

                String mValue = null;
                String mName = null;

                boolean vfound = false;

                for (int j = 0; j < parseToken.length && (vfound == false); j++) {
                    
                    try{

                    String token[] = parseToken[j].trim().split("=");

                    if (metricsToken[i].equalsIgnoreCase(token[0])) {
                        reportData = new ReportData();
                        mValue = token[1];
                        mName = token[0];
                        metricType = properties.getProperty(mName);
                        // log.debug("MetricType,Category====" + metricType);

                        if (metricType != null) {
                            metricToken = metricType.split(",");
                            reportData.setCategory(metricToken[1]);
                            reportData.setHost(hostName);
                            reportData.setStatus(status);
                            reportData.setEndTime(endTime);
                            reportData.setStartTime(startTIme);
                            reportData.setSize(size);
                            reportData.setMetricType(metricToken[0]);
                            reportData.setTimestamp1(timestamp);
                            reportData.setValue(mValue);
                            reportData.setResourceType(resourceType);
                            reportData.setResourceId(resourceId);
                            reportData.setEventID(eventID);
                            // log.debug("Setting The Report Data Sucessfull");
                            metricValueMap.put(metricsToken[i], reportData);
                        } else {
                            log.debug("***Message*****Metric Type & category is not cofigures for  Mertic==" + mName);
                        }
                    } else if ("DiskTotal".equalsIgnoreCase(token[0])) {
                        System.out.println("In DiskTotal==="+token[0]);
                        mValue = token[1];
                        mName = token[0];
                        System.out.println("mValue::"+mValue);
                        System.out.println("mValue::"+mValue);
                        diskTotal = Double.parseDouble(mValue);
                        System.out.println("diskTotal::"+diskTotal);
                    } else if ("MemTotal".equalsIgnoreCase(token[0])) {
                        // System.out.println("In memTotal==="+token[0]);
                        mValue = token[1];
                        mName = token[0];
                        memTotal = Double.parseDouble(mValue);
                    }else if("MemBuffers".equalsIgnoreCase(token[0]))
                    {
                        mValue = token[1];
                        mName = token[0];
                        memBuffered = Double.parseDouble(mValue);
                        
                    }
                    else if("MemCached".equalsIgnoreCase(token[0]))
                    {
                        mValue = token[1];
                        mName = token[0];
                        memCached = Double.parseDouble(mValue);
                        
                    }
                    else if ("DiskFree".equalsIgnoreCase(token[0])) {
                        // System.out.println("In diksfree==="+token[0]);
                        //log.debug("Disk Metric Found");
                        mValue = token[1];
                        mName = token[0];
                        diskFree = Double.parseDouble(mValue);
                        //  System.out.println("In diskTotal==="+diskTotal+"Diskfree=="+diskFree);


                        if (diskTotal != -1.1 && diskFree != -1.1) {
                            reportData = new ReportData();
                            System.out.println("Diskfree=="+diskFree+"Disktotal=="+diskTotal);
                            System.out.println("Calc::"+((diskTotal-diskFree) * 100) / diskTotal);
                            disk_boot = roundTwoDecimals(((diskTotal-diskFree) * 100) / diskTotal);
                            log.debug("DiskUsage" + disk_boot);

                            metricType = properties.getProperty("disk");
                            metricToken = metricType.split(",");

                            reportData.setCategory(metricToken[1]);
                            reportData.setHost(hostName);
                            reportData.setResourceType(resourceType);
                            reportData.setMetricType(metricToken[0]);
                            reportData.setTimestamp1(timestamp);
                            reportData.setValue(disk_boot.toString());
                            reportData.setResourceId(resourceId);
                            reportData.setEventID(eventID);

                            metricValueMap.put("disk", reportData);
                        } else {
                            log.debug("***Message**** Disk Values Are Null");
                        }

                    } else if ("MemFree".equalsIgnoreCase(token[0])) {
                        log.debug("Memory Metric Found");
                        mValue = token[1];
                        mName = token[0];
                        memFree = Double.parseDouble(mValue);
                        //   System.out.println("In memTotal==="+memTotal+"memfree=="+memFree);

                        if (memTotal != -1.1 && memFree != -1.1) {
                            reportData = new ReportData();
                            //    log.debug("memFree=="+memFree+"memTotal=="+memTotal);
                            mem_usg = roundTwoDecimals(((memTotal-(memFree+memBuffered+memCached)) * 100) / memTotal);
                            log.debug("MEm_usg" + mem_usg);

                            metricType = properties.getProperty("memory");
                            metricToken = metricType.split(",");

                            reportData.setCategory(metricToken[1]);
                            reportData.setHost(hostName);
                            reportData.setResourceType(resourceType);
                            reportData.setMetricType(metricToken[0]);
                            reportData.setTimestamp1(timestamp);
                            reportData.setValue(mem_usg.toString());
                            reportData.setResourceId(resourceId);
                            reportData.setEventID(eventID);

                            metricValueMap.put("memory", reportData);
                        } else {
                            log.debug("***Message**** Memory Values Are -1.1");
                        }
                    }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                mValue = null;
                mName = null;
            }

            metricValueMap = contextSetter.setContextsInMap(metricValueMap,customerID);
            this.insertStatus = dataInsert.sendToDatabse(metricValueMap,customerID);

        } catch (Exception e) {
            log.error("Exception :" + e.getMessage());
            e.printStackTrace();
        } finally {

            metricsToken = null;
            timestamp = null;
            startTIme = null;
            endTime = null;
            size = null;
            hostName = null;
            status = null;
            reportData = null;
            metricType = null;
            metricToken = null;
            resourceId = null;

            metricValueMap = null;
            metricValueMap1 = null;
            dataInsert = null;
            log = null;
            contextSetter = null;
        }
    }
    
    public Map<String,String> convertStringToMap(String szCepEvent)
    {
        Map<String,String> metrics_map=new HashMap<String,String>();
        String splitByComma[]=szCepEvent.split(",");
        
            for(int i=0;i<splitByComma.length;i++)
            {
                String keyValueSplit[]=splitByComma[i].split("=");
                metrics_map.put(keyValueSplit[0].trim(), keyValueSplit[1].trim());
                System.out.println("Lable::"+keyValueSplit[0].trim());
                System.out.println("Value::"+keyValueSplit[1].trim());
               

            }
             
        
        
        System.out.println("After Converted=="+metrics_map);
        return metrics_map;
        
    }

    public int getInsertStatus() {
        return this.insertStatus;
    }
}