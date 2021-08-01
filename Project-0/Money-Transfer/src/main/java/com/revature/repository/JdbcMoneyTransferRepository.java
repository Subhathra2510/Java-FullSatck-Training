package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.customException.AmountRangeException;
import com.revature.db.ConnectionFactory;
import com.revature.entity.MoneyTransfer;
import com.revature.entity.TransactionHistory;

public class JdbcMoneyTransferRepository implements MoneyTransferRepository {
	
	MoneyTransfer mt1;
	MoneyTransfer mt2;
	
	final Logger logger = Logger.getLogger("log4j.properties");
	@Override
	public void getFdetails(long Act_Num) {
		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
		
			String sql = "select * from Accounts where customer_ActNum = ?;";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, Act_Num);
			ResultSet rs =  ps.executeQuery();
			rs.next();
			
			 mt1 = new MoneyTransfer(rs.getInt(1) , rs.getString(2), rs.getLong(3) , rs.getInt(4));
			System.out.println(mt1.toString());	
	
			 logger.info("Got W details");
			 System.out.println();
	  }
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void getTdetails(long dANumber) {
		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
		
			String sql = "select * from Accounts where customer_ActNum = ?;";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, dANumber);
			ResultSet rs =  ps.executeQuery();
			rs.next();
			
		    mt2 = new MoneyTransfer(rs.getInt(1) , rs.getString(2), rs.getLong(3) , rs.getInt(4));
			System.out.println(mt2.toString());	
		  
		    logger.info("Got D details");
		    System.out.println();
	  }
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void checkandinitiate(int amount)  {
		
		if(amount <= mt1.getAct_balance())
		{
			
			int n = mt1.getAct_balance();
			int m = mt2.getAct_balance();
			mt1.setAct_balance(mt1.getAct_balance() - amount);
			
			System.out.println(amount + " transfered");
			
			System.out.println( n + " -> " + mt1.getAct_balance() + " Debited" );
			
			mt2.setAct_balance(mt2.getAct_balance() + amount);
			
			System.out.println( m + " -> " + mt2.getAct_balance() + " Credited");
	
			logger.info("Transfered successfully");
			System.out.println();
		}
		  
		else {
			
			logger.error("Transfer Unsuccessful : Entered amount is invalid");
			throw new AmountRangeException(amount);
			
		}
	}


	@Override
	public void UpdateFdetails() {
		
		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
		
			String sql = "update Accounts set Act_balance =? where customer_ActNum  = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, mt1.getAct_balance());
			ps.setDouble(2, mt1.getCustomer_ActNum());
			ps.executeUpdate();	
			 
			 logger.info("updated Withdrawer Account");
			 System.out.println();
	  }
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void UpdateTdetails() {
		
		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
		
			String sql = "update Accounts set Act_balance =? where customer_ActNum = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, mt2.getAct_balance());
			ps.setDouble(2, mt2.getCustomer_ActNum());
			ps.executeUpdate();	
		
			 logger.info("updated Depositor Account");
			System.out.println();
	  }
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void saveTranhistory(int amount) {

		try (Connection con = ConnectionFactory.getConnection()) {

			String sql = "insert into TransactionHistory (from_act_num,to_Act_num,date_of_trans,time_of_trans,Amt_transfered) values(?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, mt1.getCustomer_ActNum());
			ps.setLong(2, mt2.getCustomer_ActNum());
			ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
			ps.setTimestamp(4, java.sql.Timestamp.from(java.time.Instant.now()));
			ps.setDouble(5, amount);

			int Count = ps.executeUpdate();
			if (Count == 1) {
				 logger.info("transaction history saved..");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	
	@Override
	public List<TransactionHistory>getAccounts(long act) {

		Connection con = null;

		List<TransactionHistory> acts = new ArrayList<TransactionHistory>();

		try {
			con = ConnectionFactory.getConnection();
	
			String sql = "select * from TransactionHistory where from_act_num = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1,act);
	
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				TransactionHistory th = new TransactionHistory();
				th.setId(rs.getInt(1));
				th.setFromActNum(rs.getLong(2));
				th.setToActNum(rs.getLong(3));
				th.setDate(rs.getString(4));
				th.setTime(rs.getString(5));
				acts.add(th);
			}
			
			 logger.info("Getting Transaction History based on Account number");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return acts;
	}
	
	public List<TransactionHistory>getBetDatesAccounts(long act, String fdate , String tdate) {
		
		Connection con = null;

		List<TransactionHistory> dates = new ArrayList<TransactionHistory>();

		try {
			con = ConnectionFactory.getConnection();
	
			String sql = "select * from TransactionHistory where from_act_num = ? and date_of_trans between '"+fdate+"' and '"+tdate+"' order by date_of_trans";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1,act);
			
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				TransactionHistory th = new TransactionHistory();
				th.setId(rs.getInt(1));
				th.setFromActNum(rs.getLong(2));
				th.setToActNum(rs.getLong(3));
				th.setDate(rs.getString(4));
				th.setTime(rs.getString(5));
				dates.add(th);
			}
			
			logger.info("Got Accounts Between Dates");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dates;
	}
	
	
public List<TransactionHistory>getTop10Accounts(long act) {
		
		Connection con = null;

		List<TransactionHistory> dates = new ArrayList<TransactionHistory>();

		try {
			con = ConnectionFactory.getConnection();
	
			String sql = "select * from TransactionHistory where from_act_num = ? order by id desc limit 10 ";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1,act);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				TransactionHistory th = new TransactionHistory();
				th.setId(rs.getInt(1));
				th.setFromActNum(rs.getLong(2));
				th.setToActNum(rs.getLong(3));
				th.setDate(rs.getString(4));
				th.setTime(rs.getString(5));
				dates.add(th);
			}
			
			logger.info("Got top 10 accounts");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dates;
	}

public List<TransactionHistory>getBeforMonthsAccounts(long act ,  int val) {
	
	Connection con = null;

	List<TransactionHistory> months = new ArrayList<TransactionHistory>();

	try {
		con = ConnectionFactory.getConnection();

		String sql = "select * from TransactionHistory where from_act_num = ? and date_of_trans >= now() - interval '"+val+"' month ";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1,act);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			TransactionHistory th = new TransactionHistory();
			th.setId(rs.getInt(1));
			th.setFromActNum(rs.getLong(2));
			th.setToActNum(rs.getLong(3));
			th.setDate(rs.getString(4));
			th.setTime(rs.getString(5));
			months.add(th);
		}
		System.out.println("Got " + val + " months account");

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return months;
}

@Override
public int getABalance(long act_Num) {
	Connection con = null;
	try {
		con = ConnectionFactory.getConnection();
	
		String sql = "select Act_balance from Accounts where customer_ActNum = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, act_Num);
		ResultSet rs =  ps.executeQuery();
		rs.next();
		 
		int value = rs.getInt(1);
		
		return value;
  }
	catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	return 0;
}


	
	
	
	
	
	
	
}
