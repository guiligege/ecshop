package org.guili.ecshop.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderRegex {
	public Vector<String> results = null;

	//获取网页源码
	public String gethtmlContent(String url,String charset) {
		String temp = "";
		String htmltext = "";
		StringBuffer htmls = new StringBuffer();
		try {
			URL Url = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
			conn.setConnectTimeout(30000);
			InputStreamReader isr = new InputStreamReader(conn.getInputStream(),charset);
			BufferedReader br = new BufferedReader(isr);
			while ((temp = br.readLine()) != null) {
				htmls.append(temp);
			}
			br.close();
			isr.close();
			htmltext = htmls.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmltext;
	}
	
	//通过正则从网页源码中匹配出想要的内容
	public String[] htmlregex(String htmltext, String regExp,boolean haslabel) {
		results = new Vector<String>();
		String[] resstr = null;
		if (htmltext == null)
			return null;
		StringBuffer str = new StringBuffer();
		char[] arrays = htmltext.toCharArray();
		for (int i = 0; i < arrays.length; i++) {
			if (arrays[i] != '\r' && arrays[i] != '\n')
				str.append(arrays[i]);
		}
		htmltext = str.toString();
		try {
			Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
			int readn = 0;
			int len = htmltext.length();
			while (len > 0) {
				Matcher matcher = pattern.matcher(htmltext);
				if (!matcher.find())
					break;	
				if (matcher.groupCount() == 0) {
					break;
				}
				int groupCount = matcher.groupCount();
				for (int i = groupCount; i > 0; i--) {
					String result = matcher.group(i);
					results.add(result);
				}
				readn = matcher.end();
				len -= readn;
				htmltext = htmltext.substring(readn);
			}
		} catch (Throwable e) {
			try {
				throw e;
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(!haslabel){
			resstr = getHtmlregexResults();
		}else{
			resstr = getHtmlregexResultsLabel();
		}
		return resstr;
	}
	
	//正则后获取去的内容不带标签
	public String[] getHtmlregexResults(){
		int size = 0;
		if (results.toArray() != null) {
			size = results.size();
		}
		String[] resStrs = new String[size];
		for (int i = 0; i < results.size(); i++) {
			String res =  (String) results.get(i);
			if(Html2Text(res)!=null){
				resStrs[i] = Html2Text(res);
			}else{
				return null;
			}
		}
		return resStrs;
	}
	
	//正则后获取去的内容带标签
	public String[] getHtmlregexResultsLabel(){
		int size = 0;
		if (results.toArray() != null) {
			size = results.size();
		}
		String[] resStrs = new String[size];
		for (int i = 0; i < results.size(); i++) {
			String res =  (String) results.get(i);
			if(res!=null){
				resStrs[i] = res;
			}else{
				return null;
			}
		}
		return resStrs;
	}
	
	//网页内容去标签
	public String Html2Text(String inputString) {
		String htmlStr = inputString;
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			String regEx_html = "<[^>]+>";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll("");
			textStr = htmlStr;
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll("");
			textStr = htmlStr;
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll("");
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage() + " with:"
					+ inputString);
			return null;
		}
		return textStr;
	}
}
