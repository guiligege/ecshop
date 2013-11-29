package org.guili.ecshop.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 文件操作工具类
 * @ClassName:   FileTools 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-28 上午10:47:48 
 */
public class FileTools {
	
	/**
	 * 往文件写数据
	 * @throws IOException
	 */
	public  static void write(String filepath,String content) throws IOException {  
        File file = new File(filepath);// 指定要写入的文件  
        if (!file.exists()) {// 如果文件不存在则创建  
            file.createNewFile();  
        }  
        // 获取该文件的缓冲输出流  
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));  
        // 写入信息  
        bufferedWriter.write(content);  
        bufferedWriter.flush();// 清空缓冲区  
        bufferedWriter.close();// 关闭输出流  
    }
	
	/**
	 * 读取文件
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String read(String filepath) throws FileNotFoundException, IOException {  
		String returnstr="";
        File file = new File(filepath);// 指定要读取的文件 
        // 获得该文件的缓冲输入流  
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));  
        String line = "";// 用来保存每次读取一行的内容  
        while ((line = bufferedReader.readLine()) != null) {  
        	returnstr=line;
        }  
        bufferedReader.close();// 关闭输入流  
        return returnstr;
     }  
	
	 public static void appendToFile(String file, String conent) {  
	        BufferedWriter out = null;  
	        try {  
	            out = new BufferedWriter(new OutputStreamWriter(  
	                    new FileOutputStream(file, true)));  
	            out.write(conent);
	            out.newLine();
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            try {  
	                out.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }
	 public static void main(String[] args) {
		 String filename="E:/newProject/tmallbacktest.txt";
		 try {
				File file=new File(filename);
				if(!file.exists()){
					file.createNewFile();
				}
				FileTools.appendToFile(filename, "test");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
