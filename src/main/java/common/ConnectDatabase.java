package common;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDatabase {
	private static String url = "jdbc:sqlserver://localhost:1433;databaseName=StudentPerformanceDB;encrypt=false;trustServerCertificate=true;useUnicode=true&characterEncoding=UTF-8";
	private static String username = "sa";
	private static String password = "thanh1213";

	public static Connection checkConnect() {
		Connection c = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			c = DriverManager.getConnection(url,username,password);

		} catch(Exception e) {
			e.printStackTrace();
		}
		return c;
	}
}
