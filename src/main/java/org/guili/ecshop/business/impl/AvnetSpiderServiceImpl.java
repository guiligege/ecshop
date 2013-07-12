package org.guili.ecshop.business.impl;

import java.util.List;

import org.guili.ecshop.bean.Semiconductor;
import org.guili.ecshop.business.ISpiderService;
import org.guili.ecshop.util.SpiderRegex;

public class AvnetSpiderServiceImpl implements ISpiderService {

	@Override
	public List<Semiconductor> analysisContent(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> analysisPrices(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String analysisPricesToString(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildDiscription(List<String> headlist, String[] class2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createSemiconductorExcel(List<Semiconductor> semiconductorList,
			String file) {
		// TODO Auto-generated method stub

	}

	@Override
	public String analysisGuige(String basehtml, SpiderRegex regex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String analysisImageUrl(String basehtml, SpiderRegex regex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String analysisPriceUrl(String basehtml, SpiderRegex regex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPageCount(String basehtml, SpiderRegex regex) {
		// TODO Auto-generated method stub
		return 0;
	}

}
