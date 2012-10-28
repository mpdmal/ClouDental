package com.mpdamal.cloudental.external.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.FileResolver;

public class DailyReportsGeneratorTool {
    public static final String ROOT_DIR = System.getProperty("user.home")+"/cdent/";
    public static final String REPORTS_RELATIVEDIR = "reports/";
    public static final String RESOURCES_RELATIVEDIR = "resources/";
    public static final String PDF_NAME = "daily_report_$.pdf";
    
    public static String _dailyreport_compiled = "daily_report.jasper";
    private static String _dbstring = "jdbc:postgresql://localhost:5432/CloudentDB",
    						_dbuname = "cloudent", _dbpwd = "cloudent";
    
    private static boolean _launchUI = false;
    private static File _compiledreport = null;
    private static Connection _conn = null;
    
    //MAIN
	public static void main(String[] args) {
		for (String arg : args) {
			if (arg.startsWith("-dbstr=")) {
				_dbstring = arg.substring(7, arg.length());
			} else
			if (arg.startsWith("-dbuname=")) {
				_dbuname = arg.substring(9, arg.length());
			} else
			if (arg.startsWith("-dbpwd=")) {
				_dbpwd = arg.substring(7, arg.length());
			} else			
			if (arg.startsWith("-jasper=")) {
				_dailyreport_compiled = arg.substring(8, arg.length());
			} else				
			if (arg.equals("-ui")) {
				_launchUI = true;
			}
		}
		setupConnection();
		checkForJasper();

		if (_launchUI) {
			ReportsGeneratorToolUI f = new ReportsGeneratorToolUI();
			f.setVisible(true);
		} else {
			autoGenerateReports();
		}
	}
	
	//PRIVATE
	public static ResultSet getDentistsInfo() throws SQLException {
		Statement st;
		try {
			st = _conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT id, username, surname, name FROM Dentist ORDER BY id");
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	private static void autoGenerateReports() {
		try {
			ResultSet rs = getDentistsInfo();
			while ( rs.next() )	{
				printReport(rs.getInt(1));//dentistid column
			}
			rs.getStatement().close();
			rs.close();
			_conn.close();
		} catch (SQLException se) {
			System.err.println("Threw a SQLException creating the list of Dentist.");
			System.err.println(se.getMessage());
		}				
	}
	private static void checkForJasper() {
		//check the reports dir is where its supposed to be ...
		File f = new File(ROOT_DIR+REPORTS_RELATIVEDIR);
		if (f.exists() && f.isDirectory()) {
			System.out.println("reports dir found");
		} else {
			System.out.println("reports dir NOT found, exiting");
			System.exit(0);
		}
		
		//check the jasper compiled report (.jasper) is where its supposed to be ... 
		_compiledreport = new File(ROOT_DIR+_dailyreport_compiled);
		if (!_compiledreport.exists() || !_compiledreport.isFile()) {
			System.out.println("daily compiled report ("+_dailyreport_compiled+") NOT found, exiting");
			System.exit(0);
		}
	}
	private static void setupConnection() {
		//try connection to postgres
		try {
			Class.forName("org.postgresql.Driver");
			_conn = DriverManager.getConnection(_dbstring, _dbuname, _dbpwd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Connection to postgres established :"+_dbstring);
	}
	
	public static void printReport(int dentistid) {
		try {
			FileResolver fileResolver = new FileResolver() {
				@Override
				public File resolveFile(String fileName) {
					return new File(ROOT_DIR+RESOURCES_RELATIVEDIR+fileName);
				}
			};
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("REPORT_FILE_RESOLVER", fileResolver);
			parameters.put("DENTISTID", new Integer(dentistid));
			
			JasperPrint jprint = JasperFillManager.fillReport(
					new FileInputStream(_compiledreport),
					parameters, _conn);
			String outname = ROOT_DIR+REPORTS_RELATIVEDIR+PDF_NAME;
			outname = outname.replace("$", ""+dentistid);
			JasperExportManager.exportReportToPdfFile(jprint, outname);
			System.out.println("CREATED REPORT "+outname);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}


