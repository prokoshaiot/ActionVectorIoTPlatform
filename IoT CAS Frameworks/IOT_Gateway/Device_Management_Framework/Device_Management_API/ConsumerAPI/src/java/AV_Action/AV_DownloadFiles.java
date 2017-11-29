/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AV_Action;
import java.util.logging.Level;
import java.io.*;
import javax.servlet.*;
import org.apache.log4j.Logger;
import java.io.IOException;

/**
 *
 * @author gopal
 */
public class AV_DownloadFiles extends AV_Model{
public static Logger log=Logger.getLogger(AV_DownloadFiles.class);
public void performRequestAction(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, javax.servlet.ServletContext sc) {
        Logger esclog = Logger.getLogger(AV_DownloadFiles.class.getName());
         ServletOutputStream stream = null;
        BufferedInputStream buf = null;
        String Filepath = "";
        String Filename = null;
        String Tasktype=null;
        String Taskid=null;
        FileInputStream input = null;
        String concatFileName = "";
        try
        {
            String fp = System.getProperty("file.separator");
            Tasktype=request.getParameter("tasktype");
            Taskid=request.getParameter("taskid");
            Filename = request.getParameter("filename");
            System.out.println(Filepath + "$$$$$$" + Filename);

            Filepath=Filepath+fp+request.getServerName()+fp+Tasktype+fp+Taskid;
            String pdfDir = getServletContext().getRealPath(fp);
            System.out.println(getServletContext().getRealPath(fp));
            pdfDir = pdfDir + "files" + Filepath + fp + Filename;
            System.out.println("pdfDir::::::::::::::" + pdfDir);

            stream = response.getOutputStream();
            File pdf = null;
            pdf = new File(pdfDir);
            response.setContentType("text/xml;charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment; filename=" + Filename);
            response.setContentLength((int) pdf.length());
            input = new FileInputStream(pdf);
            buf = new BufferedInputStream(input);
            int readBytes = 0;

            while ((readBytes = buf.read()) != -1)
            {
                stream.write(readBytes);
            }

        } catch (Exception e)
        {
            e.printStackTrace();

        } finally
        {
            try
            {
                stream.close();
                buf.close();
                input.close();

            } catch (IOException ex)
            {
                java.util.logging.Logger.getLogger(AV_DownloadFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
}