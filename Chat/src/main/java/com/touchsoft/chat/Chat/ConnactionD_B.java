package com.touchsoft.chat.Chat;
import java.sql.*;

import org.apache.log4j.Logger;
public class ConnactionD_B {

	public static Connection cn;
	public static Statement statmt;
	public static ResultSet resSet;
	private static final Logger log = Logger.getLogger(ConnactionD_B.class);
	public static boolean  getDBConnection() {
		Connection dbConnection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			 log.error(e);
			return false;
		}
		try {
			cn = DriverManager.getConnection(
					"jdbc:mysql://localhost/TouchSoftChat", "root", "");
			//return dbConnection;


		} catch (SQLException e) {
			log.error(e);
			return false;

		}return true;
		//return dbConnection;

	}

	public static void WriteDBClient(String table,String name, String status){
		try {
			statmt = cn.createStatement();
			statmt.executeUpdate("INSERT INTO `"+table+"` ( `name`, `status` ) VALUES ('" +name+ "','" +status+ "')");
		} catch (SQLException e) {
			log.error(e);
		}
	}
	public static void WriteDBAgent(String table,String name, String status, int port){
		try {
			statmt = cn.createStatement();
			statmt.executeUpdate("INSERT INTO `"+table+"` ( `name`, `status`, `port`) VALUES ('" +name+ "','" +status+ "'," +port+ ")");
		} catch (SQLException e) {
			log.error(e);
		}
	}
	public static int  selectFromOnline()  {
		String query = "SELECT * FROM `agent` WHERE `status` LIKE 'online'";
		try {
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(query);int id=0;
			if (rs.next()) {id = Integer.parseInt(rs.getString("id"));return id;}

		}catch (SQLException e) {
			log.error(e);
		}return -1;
	}
	public static int  getLastID()  {
		String query = "SELECT * FROM `agent` WHERE `id`=(SELECT MAX(`id`) FROM `agent`)";
		try {
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(query);int id=0;
			if (rs.next()) {
				id = Integer.parseInt(rs.getString("id"));
				return id;
			}

		}catch (SQLException e) {
			log.error(e);
		}return -1;
	}
	public static boolean  clearTable()  {
		String query = "DELETE  FROM `agent` ";
		try {
			Statement st = cn.createStatement();
			int r = st.executeUpdate(query);
			return true;
		}catch (SQLException e) {
			log.error(e);
			return false;
		}
	}

	public static boolean  setWork(int id,String status )  {
		String query = "UPDATE `agent` SET `status` = '"+status+"' WHERE `agent`.`id` = "+id;
		try {
			Statement st = cn.createStatement();
			int rs = st.executeUpdate(query);
			return true;
		}catch (SQLException e) {
			log.error(e);
			return false;

		}
	}
	public static boolean  delete(int id ) {
		String query =  "DELETE FROM `agent` WHERE `agent`.`id` = "+id;
		try {
			Statement st = cn.createStatement();
			int rs = st.executeUpdate(query);
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;

		}
	}
}
