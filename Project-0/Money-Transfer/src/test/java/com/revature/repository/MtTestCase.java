package com.revature.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.revature.entity.MoneyTransfer;

public class MtTestCase {

	MoneyTransferRepository moneyTransferRepository;
	
	@BeforeEach
	public void setup() {
		moneyTransferRepository = new JdbcMoneyTransferRepository();
	}
	
	@Test
	public void getAccountBalance()
	{
		double balance = moneyTransferRepository.getABalance(57849037576L);
				assertEquals(2730.00, balance);
	}
	
	
	
}
