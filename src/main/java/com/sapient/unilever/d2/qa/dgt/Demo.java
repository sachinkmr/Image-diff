package com.sapient.unilever.d2.qa.dgt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
	protected static final Logger logger = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) {
		try {
			System.out.println(1 % 3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
