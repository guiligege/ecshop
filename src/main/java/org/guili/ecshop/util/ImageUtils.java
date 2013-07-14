package org.guili.ecshop.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * 图片处理工具类
 * @author guili
 */
public class ImageUtils {
	//图片根目录
	private static String IMAGEHOME=ResourceUtil.getValue(ResourceUtil.FILEPATH,"IMAGEHOME");
	private static String ERRORURL=ResourceUtil.getValue(ResourceUtil.FILEPATH,"ERRORURL");
	private static Logger log=Logger.getLogger(ImageUtils.class);
	    /** 
	     * 将图片写入到磁盘 
	     * @param img 图片数据流 
	     * @param fileName 文件保存时的名称 
	     */  
	    public static void writeImageToDisk(byte[] img, String fileName){  
	        try {
	        	//防止文件夹没创建
	        	File folder = new File(IMAGEHOME);
	        	if(!folder.isDirectory()){
	        		folder.mkdir();
	        	}
	            File file = new File(IMAGEHOME+fileName);  
	            FileOutputStream fops = new FileOutputStream(file);  
	            fops.write(img);  
	            fops.flush();  
	            fops.close();  
	            log.debug("图片已经写入到"+IMAGEHOME);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    /** 
	     * 根据地址获得数据的字节流 
	     * @param strUrl 网络连接地址 
	     * @return 
	     */  
	    public static byte[] getImageFromNetByUrl(String strUrl){  
	        try {  
	            URL url = new URL(strUrl);  
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	            conn.setRequestMethod("GET");  
	            conn.setConnectTimeout(5 * 1000);  
	            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
	            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
	            return btImg;  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
	    /** 
	     * 从输入流中获取数据 
	     * @param inStream 输入流 
	     * @return 
	     * @throws Exception 
	     */  
	    public static byte[] readInputStream(InputStream inStream) throws Exception{  
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        while( (len=inStream.read(buffer)) != -1 ){  
	            outStream.write(buffer, 0, len);  
	        }  
	        inStream.close();  
	        return outStream.toByteArray();  
	    }  
	    
	    /***
	     * 根据url抓取图片
	     * @param url
	     */
	    public static void writeImage(String url){
	    	try {
	    		//防止错误链接
//	    		if(ERRORURL.indexOf(url)>0){
//	    			return;
//	    		}
	    		if(!url.isEmpty()){
		    		byte[] btImg = getImageFromNetByUrl(url);  
			        if(null != btImg && btImg.length > 0){  
			            log.debug("读取到：" + btImg.length + " 字节");
//			            int last=url.lastIndexOf("/");
			            String fileName = url.substring(url.lastIndexOf("/")+1);
			            System.out.println("fileName--->"+fileName);
			            writeImageToDisk(btImg, fileName);
			        }else{  
			            log.debug("没有从该连接获得内容");  
			        }  
		    	}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	    /** 
	     * 测试 
	     * @param args 
	     */  
	    public static void main(String[] args) {  
//	        String url = "http://media.digikey.com/Photos/NoPhoto/NoPhoto_TMB.jpg";  
//	        byte[] btImg = getImageFromNetByUrl(url);  
//	        if(null != btImg && btImg.length > 0){  
//	            log.debug("读取到：" + btImg.length + " 字节");  
//	            String fileName = "NoPhoto_TMB.jpg";  
//	            writeImageToDisk(btImg, fileName);  
//	        }else{  
//	            log.debug("没有从该连接获得内容");  
//	        }
	    	try {
	    		ImageUtils.writeImage("http://media.digikey.com/Photos/APEM%20Comp%20Photos/E10SW6A_tmb.jpg");
			} catch (Exception e) {
				e.getStackTrace();
			}
	    }  
}
