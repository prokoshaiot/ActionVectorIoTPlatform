/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.merit.dashboard.file.SendFileToJson;
import com.merit.dashboard.queryexecuter.QueryExecuter;
import java.io.File;
import java.util.TimerTask;

/**
 *
 * @author anandkv
 */
public class QueryExecutorThread extends TimerTask {
    
    private QueryExecuter queryExecutor = null;
    private String customer = null;
    
    public QueryExecutorThread(String customer) {
        this.customer = customer;
        queryExecutor = new QueryExecuter();
    }
    
    @Override
    public void run() {
        while(true){
            String watchDogAlertJson= queryExecutor.getWatchDogAlertJson(customer);
            new SendFileToJson(customer, "", "Hour", "", ".."+File.separator+"WatchDogAlert", watchDogAlertJson);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //QueryExecuter  getWatchDogAlertJson(String customer)
    
}
