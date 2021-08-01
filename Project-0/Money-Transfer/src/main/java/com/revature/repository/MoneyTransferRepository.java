package com.revature.repository;

import java.util.List;

import com.revature.entity.MoneyTransfer;
import com.revature.entity.TransactionHistory;

public interface MoneyTransferRepository {

	int getABalance( long act_Num);
	
	void checkandinitiate(int amount);
	
	void getFdetails(long wANumber);
	
	void getTdetails(long dANumber);
	
	void UpdateFdetails();
	
	void UpdateTdetails();
	
	void saveTranhistory(int amount);

	List<TransactionHistory> getAccounts(long act);

	List<TransactionHistory>getBetDatesAccounts(long act, String fdate , String tdate);
	
	List<TransactionHistory> getTop10Accounts(long act);
	
	List<TransactionHistory>getBeforMonthsAccounts(long act , int val);
	

}
