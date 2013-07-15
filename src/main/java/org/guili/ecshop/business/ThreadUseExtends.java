package org.guili.ecshop.business;


public class ThreadUseExtends extends Thread {
	
	private org.guili.ecshop.business.TestBussiness TestBussiness;
	//构造函数 
	public void run() 
	{ 
//	System.out.println("我是Thread子类的线程实例!"); 
//	System.out.println("我将挂起10秒!"); 
//	System.out.println("回到主线程,请稍等,刚才主线程挂起可能还没醒过来！");
		TestBussiness.test();
		TestBussiness.test1();
		TestBussiness.test2();
	try 
	{ 
	sleep(5000);//挂起5秒 
	} 
	catch (InterruptedException e) 
	{ 
	return; 
	} 
	//如果该run()方法顺序执行完了,线程将自动结束,而不会被主线程杀掉 
	//但如果休眠时间过长,则线程还存活,可能被stop()杀掉 
	} 
}
