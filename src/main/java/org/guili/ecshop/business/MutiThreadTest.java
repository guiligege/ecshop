package org.guili.ecshop.business;

import org.apache.log4j.Logger;

/**
 * 测试多线程
 * @author guili
 */
public class MutiThreadTest implements Runnable {
	private TestBussiness testBusiness;
	String name;  
	
    public void setName(String name,TestBussiness testBusiness) {  
            this.name = name;  
            this.testBusiness=testBusiness;
    }
    MutiThreadTest(){
    }
    
    public void setTestBusiness(TestBussiness testBusiness) {
		this.testBusiness = testBusiness;
	}
	MutiThreadTest(String name,TestBussiness testBusiness){
    	this.name=name;
    	this.testBusiness=testBusiness;
    }
	/** Logger */
    private static final Logger logger = Logger.getLogger(MutiThreadTest.class);
	@Override
	public void run() {
		System.out.println(name + " is running.");  
		
//        try{  
        	testBusiness.test();
        	testBusiness.test1();
        	testBusiness.test2();
        	
////                Thread.sleep(5000);  
//        }catch(InterruptedException e){  
//                e.printStackTrace();  
//        }  
        System.out.println(name + " is running again.");
	}

}
