package com.revature;

import org.apache.log4j.Logger;
import java.util.Scanner;

import com.revature.customException.AmountRangeException;
import com.revature.repository.JdbcMoneyTransferRepository;
import com.revature.repository.MoneyTransferRepository;

public class MoneyTransferApp {

	
	public static void main(String[] args) {

		final Logger logger = Logger.getLogger("log4j.properties");
		Scanner sc = new Scanner(System.in);
		
		MoneyTransferRepository moneyTransferRepository  = new JdbcMoneyTransferRepository();
		logger.info("Kindly select one of the below services");
		
		logger.info("1. To do Transaction");
		logger.info("2. Enter Account Number to get Complete Transaction Details ");
		logger.info("3. Enter any two dates and account number to get their transaction history ");
		logger.info("4. Enter Account number to get their top 10 Transaction ");
		logger.info("5. Enter Account Number and month number to get till date transaction");
		logger.info(" Enter any button to exit from the service");
		int val= sc.nextInt();
		switch (val) {
		case 1:
		{
			logger.info("Enter the amount to be transfered");
			int amount = sc.nextInt();
			if(amount<0)
			{   
				logger.error("Amount you entered in invalid ");
				throw new AmountRangeException(amount);
			}
			logger.info(" Enter the Withdrawer Account Number");
			long WANumber = sc.nextLong();
			moneyTransferRepository.getFdetails(WANumber);
			logger.info("Enter the Depositor Account Number");
			long DANumber = sc.nextLong();
			moneyTransferRepository.getTdetails(DANumber);
			
			moneyTransferRepository.checkandinitiate(amount);
		
			moneyTransferRepository.UpdateFdetails();
			
			moneyTransferRepository.UpdateTdetails();
			
			moneyTransferRepository.saveTranhistory(amount);
			
			break;
		}
		    
		  case 2:
		  {
			  logger.info(" Enter the Withdrawer Account Number");
				long WANumber = sc.nextLong();
				moneyTransferRepository.getAccounts(WANumber)
				.forEach(gact->System.out.println(gact));
			  
			  
		    break;
		  }
		  case 3:
		  {
			  logger.info(" Enter the Withdrawer Account Number");
			  long WANumber = sc.nextLong();
			
			  logger.info(" Enter from date");
			  String fdate = sc.nextLine(); //"2021-07-07";
			  
			  sc.nextLine();
			  
			  logger.info(" Enter to date");
			  String tdate  = sc.nextLine(); //"2021-07-08";  
				                                              
				moneyTransferRepository.getBetDatesAccounts(WANumber, fdate, tdate)
				.forEach(bdt->System.out.println(bdt));
		    break;
		  }
		  case 4:
		  {
			  logger.info(" Enter the Withdrawer Account Number");
			  long WANumber = sc.nextLong();
				moneyTransferRepository.getTop10Accounts(WANumber)
				.forEach(t10->System.out.println(t10));
		    break;
		  }
		  case 5:
		  {
			    logger.info(" Enter the Withdrawer Account Number");
			    long WANumber = sc.nextLong();
			  
				logger.info("Enter month number");
				int month = sc.nextInt();
				
				moneyTransferRepository.getBeforMonthsAccounts(WANumber,month)
				.forEach(bm->System.out.println(bm));
		    break;
		  }
		  default:
		  {
			  logger.info("Thank you for using our services, will see you again!");
			  break;
		  }
		    
		}
		
		sc.close();	
		
	}

}
