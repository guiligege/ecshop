package org.guili.ecshop.dao.spider;

import org.guili.ecshop.bean.spider.Semiconductor;


/**
 * 操作Semiconductor接口
 * @author zhengdong.xiao
 */
public interface SemiconductorDao {
	/**
	 * 插入数据库
	 * @param semiconductor
	 * @return
	 */
	public boolean insertSemiconductor(Semiconductor semiconductor);
	
	public Semiconductor selectone(Long id);
	/**
	 * 更新数据
	 * @param semiconductor
	 * @return
	 */
	public boolean updateSemiconductor(Semiconductor semiconductor);
	/**
	 * 验证当前网站是否有该code
	 * @param semiconductor
	 * @return
	 */
	public Semiconductor selectonebyCodeAndUrl(Semiconductor semiconductor);
	
}
