package com.sachin.qa.app;

import com.sachin.qa.spider.SpiderController;

public class ThreadManager {
    public static void manageThreads(String string, SpiderController spiderController) {
	spiderController.getExecutor().execute(new Runnable(){

	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		
	    }});
    }
}
