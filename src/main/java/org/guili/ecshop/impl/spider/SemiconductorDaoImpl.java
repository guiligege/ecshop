package org.guili.ecshop.impl.spider;

import java.util.HashMap;
import java.util.Map;

import org.guili.ecshop.bean.spider.Semiconductor;
import org.guili.ecshop.dao.spider.SemiconductorDao;
import org.guili.ecshop.util.BasicSqlSupport;

public class SemiconductorDaoImpl extends BasicSqlSupport implements SemiconductorDao {

	/**
	 * 插入Semiconductor
	 */
	@Override
	public boolean insertSemiconductor(Semiconductor semiconductor) {
		boolean flag=false;
		int count=this.session.insert("org.guili.ecshop.dao.SemiconductorDao.insertSemiconductor", semiconductor);
		if(count>0){
			flag=true;
		}
		return flag;
	}

	//test select
	@Override
	public Semiconductor selectone(Long id) {
		Semiconductor semiconductor=(Semiconductor)this.session.selectOne("org.guili.ecshop.dao.SemiconductorDao.selectone");
		return semiconductor;
	}

	@Override
	public boolean updateSemiconductor(Semiconductor semiconductor) {
		boolean flag=false;
		int count=this.session.update("org.guili.ecshop.dao.SemiconductorDao.updateSemiconductor", semiconductor);
		if(count>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public Semiconductor selectonebyCodeAndUrl(Semiconductor semiconductor) {
		if(semiconductor==null){
			return null;
		}
		Map<String,String> map=new HashMap<String, String>();
		map.put("code",semiconductor.getCode());  
		map.put("sourcesite",semiconductor.getSourcesite());
		Semiconductor miconductor=(Semiconductor)this.session.selectOne("org.guili.ecshop.dao.SemiconductorDao.selectonebyCodeAndUrl",map);
		return miconductor;
	}

}
