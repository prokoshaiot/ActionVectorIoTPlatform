/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prokosha.froniusadapter;

import com.prokosha.emailsmsutility.EMailSMSUtility;
import com.prokosha.froniusadapter.configuration.AdapterProperties;
import com.prokosha.froniusadapter.configuration.FroniusEventParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author rekhas
 */
class FroniusJSONDocParser {

    private static Logger logger = Logger.getLogger(FroniusJSONDocParser.class.getName());
    private static String jsonData;
    private static JSONParser jsonParser = new JSONParser();

    public static String parseDoc(String json, ArrayList paramList, String timePeriod) {
        String evtData = "";
        String labelName = "";
        String labelValue = "";
        String szBody;
        try {
            //logger.info("JSON==>>" + json);
            jsonData = json;
            for (int i = 0; i < paramList.size(); i++) {
                FroniusEventParam szEvtParam = (FroniusEventParam) paramList.get(i);
                //logger.info("paramList:"+i+"==>>" + szEvtParam.toString());
                labelName = szEvtParam.getEventField();
                labelValue = (String) getParamValue(szEvtParam);
                evtData += "," + labelName + "=" + labelValue;
                //logger.info("evtData:"+i+"==>>" + evtData.toString());
            }
            labelName = null;
            labelValue = null;
            jsonData = null;
        } catch (Exception e) {
            logger.error("Error in parsing json==>>" + e.toString());
            logger.error("Send error event to CEP, msg and e-mail");
            FroniusError.sendErrorEvent("Error in parsing Fronius JSON", e.toString());
            if (AdapterProperties.getSendErrorSMS()) {
                EMailSMSUtility.sendSMS("Error in parsing Fronius JSON" + e.toString());
            }
            String szSubject = "FroniusAdapterError";
            if (AdapterProperties.getSendErrorMail()) {
                szBody = "Error in parsing Fronius JSON" + e.toString();
                EMailSMSUtility.sendMail(szSubject, szBody);
                szBody = null;
            }
            e.printStackTrace();
        } finally {
            jsonData = null;
        }
        return evtData;
    }

    private static String getParamValue(FroniusEventParam fEventParam) {
        String jsonValue = "";
        JSONObject jsonObject = null, bodyObject = null, dataObj = null, paramObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonData);
            bodyObject = (JSONObject) jsonObject.get("Body");
            //logger.info("json Body object==>>"+bodyObject);
            dataObj = (JSONObject) bodyObject.get("Data");
            //dataObj = (JSONObject) jsonObject.get("Data");
            //logger.info("json Data object==>>"+dataObj.toString());
            paramObject = (JSONObject) dataObj.get(fEventParam.getFroField());
            //logger.info("param Data object==>>"+paramObject.toString());
            jsonValue = paramObject.get(fEventParam.getFroValue()).toString();
            jsonObject = null;
            paramObject = null;
            dataObj = null;
            bodyObject = null;
            return jsonValue;
        } catch (Exception e) {
            logger.error("Error in getParamValue for param==>>" + fEventParam.toString());
            logger.error("Send error event to CEP, msg and e-mail" + e.toString());
            FroniusError.sendErrorEvent("Error in parsing Fronius JSON", e.toString());
            if (AdapterProperties.getSendErrorSMS()) {
                EMailSMSUtility.sendSMS("Error in parsing Fronius JSON" + e.toString());
            }
            String szSubject = "FroniusAdapterError";
            if (AdapterProperties.getSendErrorMail()) {
                String szBody = "Error in parsing Fronius JSON" + e.toString();
                EMailSMSUtility.sendMail(szSubject, szBody);
            }
            e.printStackTrace();
        } finally {
            jsonObject = null;
            paramObject = null;
            dataObj = null;
            bodyObject = null;
            jsonValue = null;
        }
        return null;
    }

    /*public static HashMap parseDocForSystem(String json, ArrayList paramList, String timePeriod) {
        HashMap<String, String> evtsMap = new HashMap();
        String labelName = "";
        JSONObject labelValues;
        try {
            //logger.info("JSON==>>" + json);
            jsonData = json;
            for (int i = 0; i < paramList.size(); i++) {
                FroniusEventParam szEvtParam = (FroniusEventParam) paramList.get(i);
                //logger.info("paramList:"+i+"==>>" + szEvtParam.toString());
                labelName = szEvtParam.getEventField();
                labelValues = (JSONObject) jsonParser.parse(getParamValue(szEvtParam));
                Set deviceIDs = labelValues.keySet();
                Iterator valuesItr = deviceIDs.iterator();
                while (valuesItr.hasNext()) {
                    String deviceID = valuesItr.next().toString();
                    logger.info("deviceID in system json values==>>" + deviceID);
                    String value = labelValues.get(deviceID).toString();
                    String evtData = labelName + "=" + value + ",";
                    if (evtsMap.containsKey(deviceID)) {
                        String tmpData = evtsMap.get(deviceID).toString();
                        tmpData += evtData;
                        evtsMap.remove(deviceID);
                        evtsMap.put(deviceID, tmpData);
                    } else {
                        evtsMap.put(deviceID, evtData);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error in parsing json==>>" + e.toString());
            logger.error("Send error event to CEP, msg and e-mail");
            FroniusError.sendErrorEvent("Error in parsing Fronius JSON", e.toString());
            FroniusMailModule.sendSMS("Error in parsing Fronius JSON" + e.toString());

            String szSubject = "FroniusAdapterError";
            String szBody = "Error in parsing Fronius JSON" + e.toString();
            FroniusMailModule.sendMail(szSubject, szBody);
            e.printStackTrace();
        } finally {
            jsonData = null;
        }
        return evtsMap;
    }*/
}