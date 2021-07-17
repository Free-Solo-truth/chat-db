package com.wsl.mychat;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Blob;

public class MyConnection {
	private Connection conn;
	private String url="jdbc:mysql://localhost:3306/user?serverTimezone=UTC";
	private String username="root";
	private String password="wangSONGling123";
	
	public MyConnection()
	{
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean searchuser(String userid,String password)
	{
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement("select * from userid where id=? and password=?");
			pstmt.setString(1, userid);
			pstmt.setString(2, password);
			rs=pstmt.executeQuery();
			if(rs.next())
			{
				return true;	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	//�����û���Ϣ
	public void insertImage(String id,String name,String password,Blob photo)
	{
		String path = "F:/1.jpg";
        PreparedStatement ps = null;
        FileInputStream in = null;
        try {
            in = ImageUtil.readImage(path);
            String sql = "insert into userid (id,name,password,photo) values(?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, password);
            //ps.setBinaryStream(4, in, in.available());
            ps.setBlob(4, photo);
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("����ɹ���");
            } else {
                System.out.println("����ʧ�ܣ�");
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
	}
	
	//�����û�ͷ��
	public void updateImage(String userid,Blob photo)
	{
        PreparedStatement ps = null;
        try {
            String sql = "update userid set photo=? where id=?";
            ps = conn.prepareStatement(sql);
            ps.setBlob(1, photo);
            ps.setString(2, userid);
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("����ͷ��ɹ���");
            } else {
                System.out.println("����ͷ��ʧ�ܣ�");
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
	}
	
	//�����û��س�
		public void updatename(String userid,String name)
		{
	        PreparedStatement ps = null;
	        try {
	            String sql = "update userid set name=? where id=?";
	            ps = conn.prepareStatement(sql);
	            ps.setString(1, name);
	            ps.setString(2, userid);
	            int count = ps.executeUpdate();
	            if (count > 0) {
	                System.out.println("�����سƳɹ���");
	            } else {
	                System.out.println("�����س�ʧ�ܣ�");
	            }
	        }catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (null != ps) {
	                try {
	                    ps.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        
		}
	
	//��ȡ�û�����Ƭ������Ϣ
	public UserData getuser(String userid)
	{
		UserData user=new UserData();
		PreparedStatement ps = null;
        ResultSet rs = null;
		
		
        try {
        	String sql = "select * from userid where id =?";
            ps = conn.prepareStatement(sql);
			ps.setString(1, userid);
			rs = ps.executeQuery();
	        
	        if (rs.next()) {
	        	user.setId(rs.getString(1));
	        	user.setName(rs.getString(2));
	        	user.setPassword(rs.getString(3));
	        	return user;
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	//��ȡ�û�ͷ����Ϣ
	public Blob getPhoto(String userid)
	{
		Blob photo=null;
		PreparedStatement ps = null;
        ResultSet rs = null;
		
		
        try {
        	String sql = "select photo from userid where id =?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,userid);
			rs = ps.executeQuery();
			
			if (rs.next()) {
	            photo=(Blob) rs.getBlob(1);
	            return photo;
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        return null;
	}
	
	
	
	//ɾ���û�
	public void deleteuser(String id)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from userid where id=?");
			pstmt.setString(1, id);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("ɾ���û�"+id+"�ɹ�!");
			else
				System.out.println("�޴��û�!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//ɾ������
	public void deletefriend(String userid,String friendid)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from relationship where userid=? and friendid=?");
			pstmt.setString(1, userid);
			pstmt.setString(2, friendid);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("ɾ������"+userid+"�ɹ�!");
			else
				System.out.println("�޴˺���!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//��Ӻ���
	public void addfriend(String userid,String friendid)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("insert into relationship(userid,friendid) values(?,?)");
			pstmt.setString(1, userid);
			pstmt.setString(2, friendid);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println(userid+"��Ӻ���"+friendid+"�ɹ�!");
			else
				System.out.println("���ʧ��!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//���������Ϣ
	public void addnews(String userid,String friendid,String content)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("insert into message(userid,friendid,content) values(?,?,?)");
			pstmt.setString(1, userid);
			pstmt.setString(2, friendid);
			pstmt.setString(3, content);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("�����Ϣ�ɹ�");
			else
				System.out.println("�����Ϣʧ��!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//ɾ��������Ϣ
	public void deletenews(String userid,String friendid)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from message where userid=? and friendid=?");
			pstmt.setString(1, userid);
			pstmt.setString(2, friendid);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("ɾ����Ϣ�ɹ�!");
			else
				System.out.println("ɾ����Ϣʧ��!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//�����سƻ�ȡ�û�id
	public String getuserid(String name)
	{
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("select id from userid where name=?");
			pstmt.setString(1, name);
			rs=pstmt.executeQuery();
			if(rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	public void close() {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("�ر�����ʧ�ܣ�");
                e.printStackTrace();
            }
        }
    }
	
	
	
}












