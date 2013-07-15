package org.guili.ecshop.business;

import javax.annotation.Resource;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class TestMuti {
	ThreadPoolTaskExecutor taskExecutor=new ThreadPoolTaskExecutor();

	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void testmuti(){
		System.out.println("Start job");

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("thread start"+taskExecutor.getActiveCount());
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("thread end"+taskExecutor.getActiveCount());
			}
		});
		taskExecutor.execute(thread);

		for(;;){  
            int count = taskExecutor.getActiveCount();  
            System.out.println("Active Threads : " + count);  
            try{  
                    Thread.sleep(1000);  
            }catch(InterruptedException e){  
                    e.printStackTrace();  
            }  
            if(count==0){  
                    taskExecutor.shutdown();  
                    break;  
            }  
		}  
		System.out.println("end job");
	}

	public static void main(String[] args) {
		TestMuti testMuti=new TestMuti();
		testMuti.testmuti();
	}
}
