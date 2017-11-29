/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AV_Action;

import businessmodel.GetAllMetricUOM;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author niteshc
 */
public class AV_GetAllMetricUOM extends HttpServlet {

    static Logger log = Logger.getLogger(AV_GetAllMetricUOM.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
        String allMetricUOM = null;

        try {

            allMetricUOM = GetAllMetricUOM.getAllMetricUOM(request);
            if (allMetricUOM == null) {
                log.debug("No data found");
                result.append("{\"error\":\"error\"}");
            } else {
                if (allMetricUOM.equals("null")) {
                    allMetricUOM = "[ ]";
                } else {
                    allMetricUOM = "[" + allMetricUOM + "]";
                }
                String callback = request.getParameter("callback");
                result.append(callback + "('" + allMetricUOM + "')");
            }
            System.out.println("Response==>" + result);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error while performing action", ex);
        }
        try {
            response.getWriter().write(result.toString());
        } catch (Exception ex) {
            log.debug("exception while writing output", ex);
        } finally {
            try {
                allMetricUOM = null;
                result = null;
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
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
    }
}
