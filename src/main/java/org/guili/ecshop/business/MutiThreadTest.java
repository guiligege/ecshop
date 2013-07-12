package org.guili.ecshop.business;

import org.apache.log4j.Logger;

/**
 * 测试多线程
 * @author guili
 */
public class MutiThreadTest implements Runnable {
	String name;  
    public void setName(String name) {  
            this.name = name;  
    }
    MutiThreadTest(){
    }
    MutiThreadTest(String name){
    	this.name=name;
    }
	/** Logger */
    private static final Logger logger = Logger.getLogger(MutiThreadTest.class);
	@Override
	public void run() {
		System.out.println(name + " is running.");  
        try{  
                Thread.sleep(5000);  
        }catch(InterruptedException e){  
                e.printStackTrace();  
        }  
        System.out.println(name + " is running again.");
	}

}
