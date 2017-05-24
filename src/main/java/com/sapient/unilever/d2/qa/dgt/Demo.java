package com.sapient.unilever.d2.qa.dgt;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
	protected static final Logger logger = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) {
		// try {
		// File file = new File("data");
		// List<String> list = new ArrayList<>();
		// getList(file, list);
		// Collections.sort(list);
		// Collections.reverse(list);
		// for (String s : list) {
		// System.out.println(s);
		// }
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		System.out.print("H" + "a");
		System.out.print('H' + 'a');
	}

	private static void getList(File file, List<String> list) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				getList(f, list);
			}
		} else {
			list.add(file.getAbsolutePath());
		}
	}

}
