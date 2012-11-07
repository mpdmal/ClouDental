package com.mpdmal.cloudental.web.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(description = "Serves back pdf reports (jasper)", urlPatterns = { "/reports/*" })
public class ReportsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static final String DEFAULT_REPORTFILENAME = "default_report.pdf";
    public static final String ROOT_DIR = System.getProperty("user.home")+"/cdent/reports/";
    public static String PDF_NAME = "daily_report_$.pdf";

    public ReportsServlet() {	super();	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OutputStream os = response.getOutputStream();
		int userid = Integer.parseInt(request.getParameter("userid"));
		
		String filename = PDF_NAME.replace("$", ""+userid);
		
		System.out.println("REQUEST OF DAILY REPORT :"+filename);
		File daiyReport = new File(ROOT_DIR+filename);
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(daiyReport); 
		} catch (FileNotFoundException e) {
			daiyReport = new File(ROOT_DIR+DEFAULT_REPORTFILENAME);
			fileInputStream = new FileInputStream(daiyReport);
		}

		response.setHeader("Content-Disposition", "inline;filename="+filename);
		response.setContentLength((int)daiyReport.length());
		response.setContentType("application/pdf");

		OutputStream responseOutputStream = response.getOutputStream();
		int bytes;
		while ((bytes = fileInputStream.read()) != -1) {
			responseOutputStream.write(bytes);
		}
		os.flush();
		os.close();
		fileInputStream.close();
	}

}
