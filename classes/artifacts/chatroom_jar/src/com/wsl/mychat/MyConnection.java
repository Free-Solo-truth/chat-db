package com.wsl.mychat;

import com.example.foodrecipes.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class MyConnection {
	private Connection conn;
	/*ע�������useSSL���Ե�ʹ��*/
	private String url="jdbc:mysql://localhost:3306/chat3?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
	private String username="root";
	private String password="123456";
	
	public MyConnection()
	{
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean searchuser(String username,String password)
	{
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement("select * from userid where username=? and password=?");
			pstmt.setString(1, username);
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
	public void insertImage(UserData user)
	{
        PreparedStatement ps = null;
        FileInputStream in = null;
        try {
//			in = new FileInputStream(user.getPhoto());
            String sql = "insert into userid (username,email,phone,password,photo) values(?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2,user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4,user.getPassword());
            ps.setString(5,"http://8.130.11.202:8080/pictures/1.jpg");
            /*�����ݿ�֮�в���Blob�����͵�����*/
//            ps.setBinaryStream(5, in, (long)in.available());
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

	//��ȡ���е��û�
	public SendUserData queryAlluser()
	{
		SendUserData AlluserList = new SendUserData();
		List<UserData> ArrayfavMes = new ArrayList<UserData>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select * from userid";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			//ע������������ʹ��
			//��������Ӷ�������ǲ�ͬ�ĵ�ַ�ռ�
			while (rs.next()){
				UserData User = new UserData();
				User.setId(rs.getString(1));
				User.setName(rs.getString(2));
				User.setEmail(rs.getString(3));
				User.setPhone(rs.getString(4));
				User.setPassword(rs.getString(5));
				User.setPhoto(rs.getString(6));
				ArrayfavMes.add(User);
			}
			AlluserList.setListUserData(ArrayfavMes);
			return AlluserList;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	//�����û�ͷ��
	public String updateImage(UpdateUser newUser) {
        PreparedStatement ps = null;
        try {
            String sql = "update userid set photo=? where email=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newUser.getPhoto());
            ps.setString(2, newUser.getEmail());
            int count = ps.executeUpdate();
            if (count > 0) {
                return "ͷ����³ɹ�";
            } else {
				return "ͷ�����ʧ��";
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
        return null;
	}
	
	//�����û��س�
		public void updatename(String username,String name)
		{
	        PreparedStatement ps = null;
	        try {
	            String sql = "update userid set name=? where name =?";
	            ps = conn.prepareStatement(sql);
	            ps.setString(1, name);
	            ps.setString(2, username);
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
	
	//��ȡ�û�����Ƭ������Ϣ ͨ��email
	public UserData getuser(String email)
	{
		UserData user=new UserData();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
        try {
        	String sql = "select * from userid where email =?";
            ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();
	        
	        if (rs.next()) {
	        	user.setId(rs.getString(1));
	        	user.setName(rs.getString(2));
	        	user.setEmail(rs.getString(3));
	        	user.setPhone(rs.getString(4));
	        	user.setPassword(rs.getString(5));
	        	user.setPhoto(rs.getString(6));
//	        	user.setPhoto(ImageUtil.readBin2Image(rs.getBinaryStream("photo"),"F:\\1.jpg"));
	        	return user;
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/*��ȡ�û�ϲ��*/
	public SendList getfavority(){
		SendList favorityMessage = new SendList("litenfei",new ArrayList<OneFavorityMessage>());
		List<OneFavorityMessage> ArrayfavMes = new ArrayList<OneFavorityMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select * from favority";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			//ע������������ʹ��
			//��������Ӷ�������ǲ�ͬ�ĵ�ַ�ռ�
			while (rs.next()){
				OneFavorityMessage favorityMessage1 = new OneFavorityMessage();
				favorityMessage1.setFavorityimage(rs.getString(2));
				favorityMessage1.setFavoritytitle(rs.getString(3));
				favorityMessage1.setFavoritysubit(rs.getString(4));
				favorityMessage1.setFavoritynumber(rs.getInt(5));
				favorityMessage1.setFavoritytime(rs.getInt(6));
				favorityMessage1.setFavoritytype(rs.getBoolean(7));
				ArrayfavMes.add(favorityMessage1);
			}
			favorityMessage.setFavMeg(ArrayfavMes);
			return favorityMessage;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	/*ɾ���û�ϲ������*/
	public String deleteFavorityInfo(String FavorityTitle){
		PreparedStatement ps = null;
		System.out.print(FavorityTitle+"\n");
		String result = null;
		try{
			String sql = "delete from favority where favorityTitle=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1,FavorityTitle);
			int rs = ps.executeUpdate();
			if (rs>0) {
				result = "ȡ���ɹ�";
				System.out.print(result);
			}else{
				result = "ȡ��ʧ��";
				System.out.print(result);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	/*�����û�ϲ������*/
	public String InsertFavorityInfo(OneFavorityMessage FavorityInfo) {
		String result = null;
		PreparedStatement ps = null;
		try {
			String sql = "insert into " +
					"favority(favorityImage,favorityTitle,favoritySubit,favoritynumber,favoritytime,favoritytype) " +
					"values(?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1,FavorityInfo.getFavorityimage());
			ps.setString(2,FavorityInfo.getFavoritytitle());
			ps.setString(3,FavorityInfo.getFavoritysubit());
			ps.setInt(4,FavorityInfo.getFavoritynumber());
			ps.setInt(5,FavorityInfo.getFavoritytime());
			ps.setBoolean(6,FavorityInfo.getFavoritytype());
			int count = ps.executeUpdate();
			if (count > 0) {
				result = "����ɹ�";
				System.out.println(result);
			} else {
				result = "����ʧ��";
				System.out.println(result);
			}
		} catch (Exception e) {
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
		return result;
	}
	
	
	//��ȡ�û�ͷ����Ϣ
	public Blob getPhoto(String email)
	{
		Blob photo=null;
		PreparedStatement ps = null;
        ResultSet rs = null;
		
		
        try {
        	String sql = "select photo from userid where email =?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,email);
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
	/*��ȡ�û�������̬*/
	public Carrier_DynamicMessage queryDynamic_message(){
		Carrier_DynamicMessage DnyMsg = new Carrier_DynamicMessage("litenfei",new ArrayList<DynamicMessage>());
		List<DynamicMessage> oneList = new ArrayList<DynamicMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			ps = conn.prepareStatement("select * from dynamic_message ");
			rs = ps.executeQuery();

			while(rs.next()){
				DynamicMessage one = new DynamicMessage();
				one.setUserIamge(rs.getString(2));
				one.setUserName(rs.getString(3));
				one.setDynamicText(rs.getString(4));
				one.setDynamicImage(rs.getString(5));
				oneList.add(one);

			}
			DnyMsg.setDynMsg(oneList);
		}catch (SQLException e){
			e.printStackTrace();
		}
		return DnyMsg;
	}
	/*�����û���̬*/
	public String insertDynamic_Message(Carrier_DynamicMessage dynamicMessage){
		String result = null;
		PreparedStatement ps = null;
		try {
			String sql = "insert into " +
					"dynamic_message(user_image,user_name,dynamic_text,dynamic_image) " +
					"values(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1,dynamicMessage.getDynMsg().get(0).getUserIamge());
			ps.setString(2,dynamicMessage.getDynMsg().get(0).getUserName());
			ps.setString(3,dynamicMessage.getDynMsg().get(0).getDynamicText());
			ps.setString(4,dynamicMessage.getDynMsg().get(0).getDynamicImage());
			int count = ps.executeUpdate();
			if (count > 0) {
				result = "����ɹ�";
				System.out.println(result);
			} else {
				result = "����ʧ��";
				System.out.println(result);
			}
		} catch (Exception e) {
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
		return result;
	}
	//ɾ���û�
	public void deleteuser(String email)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from userid where email =?");
			pstmt.setString(1, email);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("ɾ���û�"+email+"�ɹ�!");
			else
				System.out.println("�޴��û�!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public SendRelationship queryfriend(String email){
		SendRelationship DnyMsg = new SendRelationship(email,new ArrayList<Relationship>());
		List<Relationship> oneList = new ArrayList<Relationship>();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			ps = conn.prepareStatement("select * from relationship where user_email=?");
			ps.setString(1,email);
			rs = ps.executeQuery();

			while(rs.next()){
				Relationship one = new Relationship(rs.getString(2),rs.getString(3),rs.getString(4));
				oneList.add(one);
			}
			DnyMsg.setListChatPerson(oneList);
		}catch (SQLException e){
			e.printStackTrace();
		}
		return DnyMsg;
	}
	//ɾ������
	public void deletefriend(String username,String friendname)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from Relationship where username=? and friendname=?");
			pstmt.setString(1, username);
			pstmt.setString(2, friendname);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("ɾ������"+friendname+"�ɹ�!");
			else
				System.out.println("�޴˺���!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//��Ӻ���
	public String insertfriend(Relationship friend)
	{
		System.out.print(friend.getFriendname());

		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("insert into Relationship(user_email,friend_email,friendname,phone) values(?,?,?,?),(?,?,?,?) ");
			pstmt.setString(1, friend.getUseremail());
			pstmt.setString(2, friend.getFriendemail());
			pstmt.setString(3,friend.getFriendname());
			pstmt.setString(4,friend.getFriendphone());

			pstmt.setString(5, friend.getFriendemail());
			pstmt.setString(6, friend.getUseremail());
			pstmt.setString(7,friend.getUsername());
			pstmt.setString(8,friend.getUserphoto());
			int result=pstmt.executeUpdate();
			if(result>0){
				System.out.println(username+"��Ӻ���"+friend.getFriendname()+"�ɹ�!");
				return "����ɹ�";
			}
			else{
				System.out.println("���ʧ��!");
				return "����ʧ��";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//���������Ϣ
	public void addnews(String username,String friendname,String content)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("insert into message(username,friendname,content) values(?,?,?)");
			pstmt.setString(1, username);
			pstmt.setString(2, friendname);
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
	public void deletenews(String username,String friendname)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from message where username=? and friendname=?");
			pstmt.setString(1, username);
			pstmt.setString(2, friendname);
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












