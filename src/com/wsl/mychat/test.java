package com.wsl.mychat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class test {
	private static int i;
	public static void main(String[] args) throws IOException, SQLException {
		MyConnection con=new MyConnection();
		
//		System.out.println(con.searchuser("1543", "123456"));
//		con.updatename("2345", "凝光");
		
//		UserData user=con.getuser("2345");
//		System.out.println(user.getId()+" "+user.getName()+" "+user.getPassword());
		
//		con.deletefriend("2222", "1111");
//		con.addfriend("1111", "2222");
		
//		con.addnews("1111", "2222", "你好啊！");
//		con.deletenews("1111", "2222");
		
//		System.out.println(con.getuserid("艾雅法拉"));
		
//		Blob b=con.getPhoto("1543");
//		con.insertImage("2345", "刻晴", "123456", b);
		byte[] Buffer = new byte[4096];
		
		Blob photo=con.getPhoto("1543");
		FileOutputStream fos = null;
		InputStream ins=null;
		
		File file= new File("F:\\2.jpg");
		if(!file.exists())
			file.createNewFile();
		
		fos=new FileOutputStream(file);
		ins=photo.getBinaryStream();
		int size=0;
		while((size=ins.read(Buffer))!=-1)
		{
			fos.write(Buffer,0,size);
		}
		/*
		 Blob b=con.getPhoto("1543");
		if(b==null)
			System.out.println("wriong....");
		else
		{
			File f = new File("d:" + File.separator + "load2.jpg") ;    // 图片文件
			 OutputStream out = null ;
			 out = new FileOutputStream(f) ;
			 out.write(b.getBytes(1,(int)b.length())) ;
			 System.out.println("写入成功.....");
			 out.close() ;
		}
		 */
	}
	

}
