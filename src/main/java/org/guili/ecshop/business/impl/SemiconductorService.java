package org.guili.ecshop.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.guili.ecshop.bean.Semiconductor;
import org.guili.ecshop.business.ISemiconductorService;
import org.guili.ecshop.dao.SemiconductorDao;

/**
 * Semiconductor服务类
 * @author zhengdong.xiao
 */
public class SemiconductorService implements ISemiconductorService {

	private static final int PAGECOUNT=1000;
	private SemiconductorDao semiconductorDao=null;
	
	@Override
	public void saveSemiconductor(Semiconductor semiconductor) {
		semiconductorDao.insertSemiconductor(semiconductor);
	}

	@Override
	public void modifySemiconductor(Semiconductor semiconductor) {
		semiconductorDao.updateSemiconductor(semiconductor);
	}

	@Override
	public Semiconductor findonebycodeAndurl(Semiconductor semiconductor) {
		if(semiconductor==null){
			return null;
		}
		return semiconductorDao.selectonebyCodeAndUrl(semiconductor);
	}
	/**
	 * 批量保存
	 * @param semiconductorList
	 */
	public void saveSemiconductorList(List<Semiconductor> semiconductorList){
		if(semiconductorList==null || semiconductorList.size()==0){
			return ;
		}
		for(Semiconductor semiconductor:semiconductorList){
			semiconductorDao.insertSemiconductor(semiconductor);
		}
	}
	/**
	 * 批量更新
	 * @param semiconductorList
	 */
	public void updateSemiconductorList(List<Semiconductor> semiconductorList){
		if(semiconductorList==null || semiconductorList.size()==0){
			return ;
		}
		for(Semiconductor semiconductor:semiconductorList){
			semiconductorDao.updateSemiconductor(semiconductor);
		}
	}

	/**
	 * 批量处理Semiconductor
	 */
	public void pageSemiconductorService(List<Semiconductor> semiconductorList){
		if(semiconductorList==null || semiconductorList.size()==0){
			return ;
		}
		List<Semiconductor> saveList=new ArrayList<Semiconductor>();
		List<Semiconductor> updateList=new ArrayList<Semiconductor>();
		for(Semiconductor semiconductor:semiconductorList){
			if(findonebycodeAndurl(semiconductor)==null){
				saveList.add(semiconductor);
			}else{
				updateList.add(semiconductor);
			}
		}
		if(saveList!=null && saveList.size()>0){
			this.saveSemiconductorList(saveList);
		}
		if(updateList!=null && updateList.size()>0){
			this.updateSemiconductorList(semiconductorList);
		}
	}
	
	public SemiconductorDao getSemiconductorDao() {
		return semiconductorDao;
	}

	public void setSemiconductorDao(SemiconductorDao semiconductorDao) {
		this.semiconductorDao = semiconductorDao;
	}
	/**
	 * 分页处理接口
	 * @param semiconductorList
	 */
	public void pageservice(List<Semiconductor> semiconductorList){
		if(semiconductorList==null || semiconductorList.size()==0){
			return;
		}
		int page=semiconductorList.size()/PAGECOUNT+1;
		for(int i=0;i<page;i++){
			int start=i*PAGECOUNT;
			int end=(i+1)*PAGECOUNT;
			if(end>semiconductorList.size()){
				end=semiconductorList.size();
			}
			List<Semiconductor> semiconductorsubList=semiconductorList.subList(start, end);
			//具体处理
			this.pageSemiconductorService(semiconductorsubList);
		}
	}

}
