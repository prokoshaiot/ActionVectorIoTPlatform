
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


package com.merit.dashboard.controller;

import org.apache.log4j.Logger;
import com.merit.dashboard.service.DashBoardService;

public class DashBoardController {
	static Logger log = Logger.getLogger(DashBoardController.class);

	DashBoardService dashBoardService=null;

	/** This method is used to call dashBoardService.generateMetricTypeJson(),dashBoardService.generateLineChartJson()
	 * 	for generating Metric type And Line Chart JSON for Chart
     * @param resourceType Different category as Desktop,Server,DataBase,Network,JVM
	 * @param startDate subtract(endDate-timestampselection) in Date Format
	 * @param endDate Current date in Date Format
	 * @param timestampselection  Selected Time Period means (hour, days, week, month, year)
	 * @param customer Selected customerID
     * */
	public DashBoardController(String resourceType, String selection, String startDate, String endDate,String timestampselection, String customer) {
		try {

			log.debug("****************** Starting DashBoardController, Generate MetricType and LineChart Json********************");

			dashBoardService=new DashBoardService();
            if(selection.startsWith("TimeLine")){
    			dashBoardService.generateLineChartJson( resourceType, selection, startDate, endDate, timestampselection, customer);
            }else{
    			dashBoardService.generateMetricTypeJson(resourceType, selection, startDate, endDate, timestampselection, customer);
            }

		} catch (Exception e) {
			e.printStackTrace();
			log.error("DashBoard Calling Default Service and ResourceType DashBoardController.generateMetricTypeJson and generateLineChartJson :" + e.getMessage());
		}
	}



	/** This method is used to call dashBoardService.generateDefaultServiceAndResourceTypeJson()
	 * 	for generating Default Service And ResourceType JSON for Chart
     * @param resourceType Different category as Desktop,Server,DataBase,Network,JVM
	 * @param startDate subtract(endDate-timestampselection) in Date Format
	 * @param endDate Current date in Date Format
	 * @param timestampselection  Selected Time Period means (hour, days, week, month, year)
	 * @param customer Selected customerID
     * */
	public void generateDefaultServiceAndResourceType(String resourceType,String startDate, String endDate, String timestampselection,String customer) {
		try {
			dashBoardService=new DashBoardService();
			log.debug("******************Generate Default Service and ResourceType Json***************");
			dashBoardService.generateDefaultServiceAndResourceTypeJson(resourceType, startDate,	endDate, timestampselection, customer);
			log.debug("******************End Of DashBoard  Controller********************");

		} catch (Exception e) {
			e.printStackTrace();
			log.error("DashBoard Calling Default Service and ResourceType DashBoardController.generateDefaultServiceAndResourceType() :" + e.getMessage());

		}

	}

	/** This method is used to call dashBoardService.generateDefaultServiceAndResourceTypeJson()
	 * 	for generating Default Service And ResourceType JSON for Chart
     * @param resourceType Different category as Desktop,Server,DataBase,Network,JVM
	 * @param startDate subtract(endDate-timestampselection) in Date Format
	 * @param endDate Current date in Date Format
	 * @param timestampselection  Selected Time Period means (hour, days, week, month, year)
	 * @param customer Selected customerID
     * */
	public void generateJSONForLeftGrid(String resourceType,String startDate, String endDate, String timestampselection,String customer) {
		try {
			dashBoardService=new DashBoardService();
			log.debug("******************Generate  LeftGrid Json***************");
			dashBoardService.generateJSONForLeftGrid(resourceType, startDate,	endDate, timestampselection, customer);
			log.debug("******************End Of DashBoard  Controller********************");
			dashBoardService=null;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("DashBoard Calling Default Service and ResourceType DashBoardController.generateDefaultServiceAndResourceType() :" + e.getMessage());

		}

	}
}