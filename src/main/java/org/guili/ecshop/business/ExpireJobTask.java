package org.guili.ecshop.business;

import org.apache.log4j.Logger;
/**
 * 定时任务测试
 * @author guili
 */
public class ExpireJobTask {
    /** Logger */
    private static final Logger logger = Logger.getLogger(ExpireJobTask.class);
 
    /**
     * 业务逻辑处理
     */
    public void doBiz() {
  // 执行业务逻辑
  // ........
    	System.out.println("test quartz..");
    	logger.debug("test quartz");
    }
}