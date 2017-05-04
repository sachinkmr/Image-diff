package com.sapient.unilever.d2.qa.dgt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.utils.HelperUtils;

public class Demo {
    protected static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
	System.out.println(HelperUtils.getUniqueName("Sachin"));
	System.out.println(HelperUtils.getUniqueName("Sachin"));
	System.out.println(HelperUtils.getUniqueName("Sachin"));
	System.out.println(HelperUtils.getUniqueName("Bhumika"));
    }
}
