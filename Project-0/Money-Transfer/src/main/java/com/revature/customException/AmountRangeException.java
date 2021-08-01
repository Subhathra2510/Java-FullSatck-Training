package com.revature.customException;

public class AmountRangeException extends RuntimeException {

	public AmountRangeException(int str)
	{
		System.out.println(str);
	}
}
