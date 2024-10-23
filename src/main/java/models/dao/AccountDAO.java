package models.dao;

import java.sql.SQLException;
import java.sql.PreparedStatement;

import common.ConnectDatabase;
import models.bean.Account;

public class AccountDAO {
	public static boolean checkAccount(Account account) {
		String queryAccount = "SELECT * FROM Accounts WHERE username = ? AND password = ?";
		
		boolean ok = false;
		try {
			PreparedStatement stmt = ConnectDatabase.checkConnect().prepareStatement(queryAccount);
			stmt.setString(1, account.getUsername());
			stmt.setString(2, account.getPassword());
			if( stmt.executeQuery().next())
				ok = true;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return ok;
	}
	
}
