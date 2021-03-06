/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Model.DatabaseConnection;
import javax.servlet.ServletException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import sadeskInterface.StateControllerConstants;
import sadeskException.SADeskException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gopal
 */
public class CepTypeServlet extends HttpServlet
{

    static Logger log = Logger.getLogger(CepTypeServlet.class);
    private static String className = CepTypeServlet.class.getName();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws SADeskException
    {
        response.setContentType("text/html;charset=UTF-8");

        ObjectOutputStream outputStream = null;

        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;

        String hostgroup = null;
        String type = null;
        String service = null;
        String hostName = null;
        String resourceType = null;
        String subservice = null;

        System.out.println(" CepTypeServlet servlet Started .");
        try
        {
            hostName = request.getParameter(StateControllerConstants.HOSTNAME);
            resourceType = request.getParameter("resourceType").trim();
            outputStream = new ObjectOutputStream(response.getOutputStream());

            //con = DatabaseConnectionManager.getConnection("CepTypeServlet");
            con=DatabaseConnection.getAVSAConnection(request);
            stat = con.createStatement();
            String Query = "";

            //rs = stat.executeQuery("select service,subservice,hostgroup from hostinfo where config_id=(select max(config_id) from ipinfo where ipaddress='" + ipaddress + "')");
            System.out.println("Query::::" + hostName);
            if (hostName.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$") == false)
            {
                if (resourceType.equalsIgnoreCase("default"))
                {
                    rs = stat.executeQuery("select service,subservice,hostgroup from hostinfo where lower(resourceType)=lower('" + resourceType + "')");
                } else
                {
                    rs = stat.executeQuery("select service,subservice,hostgroup from hostinfo where lower(host)=lower('" + hostName + "') and lower(resourceType)=lower('" + resourceType + "')");
                }
            } else
            {
                if (resourceType.equalsIgnoreCase("default"))
                {
                    rs = stat.executeQuery("select service,subservice,hostgroup from hostinfo where lower(resourceType)=lower('" + resourceType + "')");

                } else
                {
                    System.out.println("Query::"+"select service,subservice,hostgroup from hostinfo where config_id in (select config_id from ipinfo where ipaddress='" + hostName + "') and lower(resourceType)=lower('" + resourceType + "')");
                    rs = stat.executeQuery("select service,subservice,hostgroup from hostinfo where config_id in (select config_id from ipinfo where ipaddress='" + hostName + "') and lower(resourceType)=lower('" + resourceType + "')");
                }
            }

           // System.out.println("Inside postgre sql database");
            while (rs.next())
            {
                service = (String) rs.getString(1);
                subservice = (String) rs.getString(2);
                hostgroup = (String) rs.getString(3);
            }
            stat.close();
            stat = null;
            rs.close();

            outputStream.writeObject(service + ":" + subservice + ":" + hostgroup);
            outputStream.flush();
        } catch (Exception e)
        {
            log.fatal("Could not create statement of JDBC inside TypeController servlet.");
            e.printStackTrace();
            throw new SADeskException("Could not create statement of JDBC inside TypeController servlet.", e);

        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (stat != null)
                {
                    stat.close();
                }
                if (con != null)
                {
                    con.close();
                }
                try
                {
                    outputStream.close();
                } catch (IOException ex)
                {
                    java.util.logging.Logger.getLogger(className).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex)
            {
                java.util.logging.Logger.getLogger(className).log(Level.SEVERE, null, ex);
            }
        }


    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            processRequest(request, response);
        } catch (Exception e)
        {
            System.out.println("Exception generated from doPost() method of TypeController servlet.");
            e.printStackTrace();
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            processRequest(request, response);
        } catch (Exception e)
        {
            System.out.println("Exception generated from doPost() method of TypeController servlet.");
            e.printStackTrace();
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}
