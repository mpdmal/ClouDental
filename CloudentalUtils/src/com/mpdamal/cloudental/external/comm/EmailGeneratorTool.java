package com.mpdamal.cloudental.external.comm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailGeneratorTool {
    private static String _dbstring = "jdbc:postgresql://localhost:5432/CloudentDB",
    						_dbuname = "cloudent", _dbpwd = "cloudent",
    						_cloudent_account = "cloudental@gmail.com" , _cloudent_pwd = "cloudental123!";
    		
    private static boolean _launchUI = false;
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
			if (arg.equals("-ui")) {
				_launchUI = true;
			}
		}
		setupConnection();

		if (_launchUI) {
			EmailGeneratorToolUI f = new EmailGeneratorToolUI();
			f.setVisible(true);
		} else {
			notifyEveryone();
		}
	}

	public static ResultSet getDentistsInfo() throws SQLException {
		Statement st;
		try {
			st = _conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT id, username, surname, name, p.emailnotification FROM Dentist d," +
					" userpreferences p where d.id=p.userid ORDER BY id");
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static void notifyEveryone() {
		try {
			ResultSet rs = getDentistsInfo();
			while ( rs.next() )	{
				if (rs.getBoolean(5)) //notify flag
					sendEmailNotifications(rs.getInt(1));//dentistid
				else 
					System.out.println("email notify OFF for "+rs.getString(2));
			}
			rs.getStatement().close();
			rs.close();
			_conn.close();
		} catch (SQLException se) {
			System.err.println("Threw a SQLException creating the list of Dentist.");
			System.err.println(se.getMessage());
		}				
	}
	
	public static void sendEmailNotifications(int dentistid) {
		System.out.println("NOTIFYING PATIENTS OF "+dentistid);
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getInstance(props);
		try{
			MimeMessage message = new MimeMessage(session);
			message.setSubject("Cloudental - Reminder");
			message.setContent(getCustomNotificationText(dentistid), "text/plain");
	         
			for (Integer id : getPatientIDs(dentistid)) {
				String email = getPatientEmail(id);
				if (!verifyEmail(email))
					continue;
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
				Transport transport = session.getTransport("smtp");
				transport.connect("smtp.gmail.com", 587, _cloudent_account, _cloudent_pwd);
				transport.sendMessage(message, message.getAllRecipients());
			}
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
		System.out.println("NOTIFIED PATIENTS ----------------------");
	}
	//PRIVATE
	private static String getCustomNotificationText(int dentistid) {
		String ans = "";
		try {
			ResultSet rs = _conn.createStatement().executeQuery("SELECT emailcontent FROM userpreferences where userid="+dentistid);
			rs.next();
			ans = rs.getString(1);
			rs.getStatement().close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("\tCANNOT get mail content from preferences for dentist id:"+dentistid);
		}
		return ans;
	}
	
	private static boolean verifyEmail(String email) {
		if (email.length() <= 0)
			return false;
		
		return true;
	}
	private static String getPatientEmail(int patientid) {
		String ans = "";
		try {
			ResultSet rs = _conn.createStatement().executeQuery("SELECT info FROM contactinfo where id="+patientid+" and infotype=0");
			rs.next();
			ans = rs.getString(1);
			rs.getStatement().close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("\tCANNOT get email info for patient id:"+patientid);
		}
		return ans;
	}
	
	private static ArrayList<Integer> getPatientIDs(int dentistid) {
		ArrayList<Integer> ans = new ArrayList<Integer>();
		try {
			ResultSet rs = _conn.createStatement().executeQuery("SELECT id FROM patient where dentistid="+dentistid);
			while (rs.next()) 
				ans.add(rs.getInt(1));
			rs.getStatement().close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("\tCANNOT get patient ids for dentist id:"+dentistid);
		}
		return ans;
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
}


