/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AV_Action;

import Model.DatabaseConnection;
import businessmodel.GetResources;
import com.adminAPI.DeviceManagment;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author niteshc
 */
public class AV_GetResources extends HttpServlet {

    static Logger log = Logger.getLogger(AV_GetResources.class);

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        StringBuffer result = new StringBuffer();
        String resourceInfo = null;
        String customer = null;
        String service = null;
        try {
            customer = request.getParameter("customer");
            service = request.getParameter("service");
            Connection con = DatabaseConnection.getAVSAConnection(request);
            resourceInfo =new DeviceManagment().getResources(con, customer, service);
            /** commented by ananddw
            resourceInfo = GetResources.getResourcesInfo(request, customer, service);
            if (resourceInfo == null) {
                log.debug("No data found");
                result.append("{\"error\":\"error\"}");
            } else {
                resourceInfo = "[" + resourceInfo + "]";
                
                result.append(callback + "('" + resourceInfo + "')");
            }
            **/
            String callback = request.getParameter("callback");
            result.append(callback + "('" + resourceInfo + "')");
           // System.out.println("Response==>" + result);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error while performing action", e);
        }
        try {
            response.getWriter().write(result.toString());
        } catch (Exception e) {
            log.debug("exception while writing output", e);
        } finally {
            try {
                if (result != null) {
                    result = null;
                }
                if (resourceInfo != null) {
                    resourceInfo = null;
                }
                if (customer != null) {
                    customer = null;
                }
                if (service != null) {
                    service = null;
                }
                if (out != null) {
                    out.close();
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
