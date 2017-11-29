/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prokosha.modbusadapter.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author Abraham
 */
public class AdapterProperties {

    private static final Logger log = Logger.getLogger(AdapterProperties.class.getName());
    private static String newline = null;
    private static String eventMapper = null;
    private static int downloadSleepInterval = 0;
    private static int retryJSONDownload = 0;
    private static int retrySendToCEP = 0;
    private static String sslHost = null;
    private static int sslPort = 0;
    private static boolean enableRT = false;
    private static String customerID = null;
    private static String xmlConfigFile = null;
    //dogwatch config
    private static int watchDogPort = 0;
    private static String watchDogHost = null;
    private static String sendToWatchDog = null;
    private static String eventStaticData = null;
    private static String[] mobileNos;
    private static String smsServerURL;
    private static int smsServerPort;
    private static String emailIDs;
    private static String fromEmailID;
    private static String smtpAddress;
    private static int smtpPort;
    private static String smtpUserName;
    private static String smtpPassword;
    private static String jsonParserMapper = null;
    private static boolean sendErrorSMS;
    private static boolean sendErrorMail;
    private static String failedEventsLog;
    private static String cCustomerID;
    private static String installationID;
    private static String modbusURL;
    private static String startTime;
    private static String endTime;
    
    public AdapterProperties() {
    }

    public static boolean loadProperties(String fileName) throws IOException {
        String tmp;

        log.debug("Loading properties file: " + fileName);

        Properties props = null;
        FileInputStream fps = null;
        try {
            props = new Properties();
            fps = new FileInputStream(fileName);
        props.load(fps);
        fps.close();
            fps = null;

        //SSL host address or ip
        sslHost = props.getProperty("SSL-host");
        if (sslHost == null || sslHost.trim().length() == 0) {
            log.error("*** ERROR *** SSL-host property not specified - cannot continue!!");
            return false;
        }

        //SSL port number
        tmp = props.getProperty("SSL-port");
        if (tmp != null) {
            sslPort = Integer.parseInt(tmp.trim());
        } else {
            log.debug("SSL-port property not specified - using 7102");
            sslPort = 7102; //default gmond port
        }

        //SSL event protocol requires a line terminator after every SSL event
        newline = props.getProperty("SSL.line-separator"); //SSL socket protocol line terminator
        if (newline == null) {
            log.debug("SSL.line-seperator property not specified - using newline (\\n)");
            newline = "\n";
        }

        //number of seconds to sleep between each modbus JSON file collection and parsing cycle
        tmp = props.getProperty("modbus.download-sleep");
        if (tmp != null) {
            downloadSleepInterval = Integer.parseInt(tmp) * 1000; // sleep between gmond reads
        } else {//default threshold is no sleep
            log.debug("modbus.download-sleep property not specified - using 60 seconds");
            downloadSleepInterval = 60 * 1000; //sleep for 1 minute
        }

        //ModBus Requests XML
        xmlConfigFile = props.getProperty("modbus.requests");
        if (xmlConfigFile == null || xmlConfigFile.trim().length() == 0) {
            log.error("*** ERROR *** modbus.requests property not specified - cannot continue!!");
            return false;
        }

        //ModBus JSON to CEP event mapping defintion XML
        eventMapper = props.getProperty("modbus.cep-event-mapper");
        if (eventMapper == null || eventMapper.trim().length() == 0) {
            log.error("*** ERROR *** modbus.cep-event-mapper property not specified - cannot continue!!");
            return false;
        }

        tmp = props.getProperty("modbus.retry-count");
        if (tmp != null) {
            retryJSONDownload = Integer.parseInt(tmp); // retry XML download
        } else {//default no retries
            log.debug("modbus.retry-count property not specified - will not retry on error");
            retryJSONDownload = 0;
        }

        tmp = props.getProperty("cep.retry-count");
        if (tmp != null) {
            retrySendToCEP = Integer.parseInt(tmp); // retry XML download
        } else {//default no retries
            log.debug("cep.retry-count property not specified - will not retry on error");
            retrySendToCEP = 0;
        }

        tmp = props.getProperty("eventRT.enable");
        if (tmp != null) {
            enableRT = Boolean.valueOf(tmp);
        } else {
            log.debug("eventRT.enable property not specified - using false");
            enableRT = false;
        }
        tmp = props.getProperty("CustomerID");
        if (tmp != null) {
            customerID = tmp;
        } else {
            log.debug("CustomerID property not specified - using null");
            customerID = null;
        }

        //watchdog properties(host, port) reading

        tmp = props.getProperty("WatchDog-port");
        if (tmp != null) {
            watchDogPort = Integer.parseInt(tmp); // watchdog port
        } else {//default no retries
            log.debug("WatchDog-port property not specified - will not retry on error");
            watchDogPort = 0;
        }
        watchDogHost = props.getProperty("WatchDog-host");
        if (watchDogHost == null || watchDogHost.trim().length() == 0) {
            log.error("*** ERROR *** WatchDog-host property not specified - cannot continue!!");
            return false;
        }
        tmp = props.getProperty("Send-WatchDog");
        if (tmp != null) {
            sendToWatchDog = tmp;
        } else {
            log.debug("Send-WatchDog property not specified - will not retry on error");
            sendToWatchDog = "false";
        }
        tmp = props.getProperty("EventStaticData");
        if (tmp != null) {
            eventStaticData = tmp;
        } else {
            log.debug("EventStaticData property not specified - will not retry on error");
            eventStaticData = null;
        }
        tmp = props.getProperty("JSONParserMapper");
        if (tmp != null) {
            jsonParserMapper = tmp;
        } else {
            log.debug("JSONParserMapper property not specified - will not retry on error");
            jsonParserMapper = null;
        }
        tmp = props.getProperty("AlertMobileNos");
        if (tmp != null) {
            if (tmp.contains(",")) {
                mobileNos = tmp.split(",");
            } else {
                mobileNos = new String[1];
                mobileNos[0] = tmp;
            }
        } else {
            log.info("AlertMobileNos property not specified");
            mobileNos = null;
        }
        tmp = props.getProperty("SMSServerURL");
        if (tmp != null) {
            smsServerURL = tmp;
        } else {
            log.debug("SMSServerURL property not specified");
            smsServerURL = null;
        }
        tmp = props.getProperty("SMSServerPort");
        if (tmp != null) {
            smsServerPort = Integer.parseInt(tmp);
        } else {
            log.debug("SMSServerPort property not specified");
            smsServerPort = -1;
        }
        tmp = props.getProperty("AlertEmailIDs");
        if (tmp != null) {
            emailIDs = tmp;
        } else {
            log.info("AlertEmailIDs property not specified");
            emailIDs = null;
        }
        tmp = props.getProperty("FromEmailID");
        if (tmp != null) {
            fromEmailID = tmp;
        } else {
            log.debug("FromEmailID property not specified");
            fromEmailID = null;
        }
        tmp = props.getProperty("SMTPAddress");
        if (tmp != null) {
            smtpAddress = tmp;
        } else {
            log.debug("SMTPAddress property not specified");
            smtpAddress = null;
        }
        tmp = props.getProperty("SMTPPort");
        if (tmp != null) {
            smtpPort = Integer.parseInt(tmp);
        } else {
            log.debug("SMTPPort property not specified");
            smtpPort = -1;
        }
        tmp = props.getProperty("SMTPUsername");
        if (tmp != null) {
            smtpUserName = tmp;
        } else {
            log.debug("SMTPUsername property not specified");
            smtpUserName = null;
        }
        tmp = props.getProperty("SMTPPassword");
        if (tmp != null) {
            smtpPassword = tmp;
        } else {
            log.debug("SMTPPassword property not specified");
            smtpPassword = null;
        }
        tmp = props.getProperty("SendErrorSMS");
        if (tmp != null) {
            sendErrorSMS = Boolean.parseBoolean(tmp);
        } else {
            log.debug("SendErrorSMS property not specified");
            sendErrorSMS = false;
        }
        tmp = props.getProperty("SendErrorMail");
        if (tmp != null) {
            sendErrorMail = Boolean.parseBoolean(tmp);
        } else {
            log.debug("SendErrorMail property not specified");
            sendErrorMail = false;
        }
        tmp = props.getProperty("modbus.failedEvents.log");
        if (tmp != null) {
            failedEventsLog = tmp;
        } else {
            log.error("modbus.failedEvents.log property not specified");
            failedEventsLog = "";
            return false;
        }
        tmp = props.getProperty("cCustomerID");
        if (tmp != null) {
            cCustomerID = tmp;
        } else {
            log.error("cCustomerID property not specified");
            cCustomerID = "";
            return false;
        }
        tmp = props.getProperty("installationID");
        if (tmp != null) {
            installationID = tmp;
        } else {
            log.error("installationID property not specified");
            installationID = "";
            return false;
        }
        tmp = props.getProperty("modbusURL");
        if (tmp != null) {
            modbusURL = tmp;
        } else {
            log.error("modbusURL property not specified");
            modbusURL = "";
            return false;
        }
         
            tmp = props.getProperty("startTime");
            if (tmp != null) {
                startTime = tmp;
            } else {
                log.error("startTime property not specified");
                startTime = "";
                return false;
            }

            tmp = props.getProperty("endTime");
            if (tmp != null) {
                endTime = tmp;
            } else {
                log.error("endTime property not specified");
                endTime = "";
                return false;
            }

        props.clear();
        props = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                props = null;
                if (fps != null) {
                    fps.close();
        fps = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //watchdog event Setup 25-07-2013
    public static boolean sendToWatchDog() {
        if (sendToWatchDog.equalsIgnoreCase("false")) {
            return false;
        }
        return true;
    }

    public static String getNewline() {
        return newline;
    }

    public static String getSSLHost() {
        return sslHost;
    }

    public static String getXMLConfigFile() {
        return xmlConfigFile;
    }

    public static String getEventMapper() {
        return eventMapper;
    }

    public static int getSleepInterval() {
        return downloadSleepInterval;
    }

    public static int getSSLPort() {
        return sslPort;
    }

    public static int getRetryCount() {
        return retryJSONDownload;
    }

    public static int getSendRetryCount() {
        return retrySendToCEP;
    }

    public static boolean getEnableRT() {
        return enableRT;
    }

    public static String getCustomerID() {
        return customerID;
    }

    public static String getWatchDogHost() {
        return watchDogHost;
    }

    public static int getWatchDogPort() {
        return watchDogPort;
    }

    public static String getEventStaticData() {
        return eventStaticData;
    }

    public static String getParserMapper() {
        return jsonParserMapper;
    }

    public static String[] getMobileNos() {
        return mobileNos;
    }

    public static String getSMSServerURL() {
        return smsServerURL;
    }

    public static int getSMSServerPort() {
        return smsServerPort;
    }

    public static String getEmailIDs() {
        return emailIDs;
    }

    public static String getFromEmailID() {
        return fromEmailID;
    }

    public static String getSMTPAddress() {
        return smtpAddress;
    }

    public static int getSMTPPort() {
        return smtpPort;
    }

    public static String getSMTPUserName() {
        return smtpUserName;
    }

    public static String getSMTPPassword() {
        return smtpPassword;
    }

    public static boolean getSendErrorSMS() {
        return sendErrorSMS;
    }

    public static boolean getSendErrorMail() {
        return sendErrorMail;
    }

    public static String getFailedEventsLog() {
        return failedEventsLog;
    }

    public static String getcCustomerID() {
        return cCustomerID;
    }

    public static String getInstallationID() {
        return installationID;
    }

    public static String getModBusURL() {
        return modbusURL;
    }

    public static String getEndTime() {
        return endTime;
}

    public static String getStartTime() {
        return startTime;
    }
}