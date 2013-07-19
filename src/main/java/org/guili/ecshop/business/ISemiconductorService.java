package org.guili.ecshop.business;

import java.util.List;

import org.guili.ecshop.bean.Semiconductor;

/**
 * Semiconductor服务类接口
 * @author zhengdong.xiao
 *
 */
public interface ISemiconductorService {
	public void saveSemiconductor(Semiconductor semiconductor);
	public void modifySemiconductor(Semiconductor semiconductor);
	public Semiconductor findonebycodeAndurl(Semiconductor semiconductor);
	/**
	 * 批量保存
	 * @param semiconductorList
	 */
	public void saveSemiconductorList(List<Semiconductor> semiconductorList);
	/**
	 * 批量更新
	 * @param semiconductorList
	 */
	public void updateSemiconductorList(List<Semiconductor> semiconductorList);
	/**
	 * 分页处理
	 * @param semiconductorList
	 */
	public void pageSemiconductorService(List<Semiconductor> semiconductorList);
	
	/**
	 * 分页处理接口
	 * @param semiconductorList
	 */
	public void pageservice(List<Semiconductor> semiconductorList);
}
