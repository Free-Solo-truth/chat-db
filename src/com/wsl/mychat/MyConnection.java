

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
	/*注意这里的useSSL属性的使用*/
	private String url="jdbc:mysql://云数据库ip:3306/chat3?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
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
	
	//插入用户信息
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
	//初始化头像数据
            ps.setString(5,"http://服务器ip:8080/pictures/1.jpg");
            /*向数据库之中插入Blob的类型的数据*/
//            ps.setBinaryStream(5, in, (long)in.available());
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("插入成功！");
            } else {
                System.out.println("插入失败！");
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

	//获取所有的用户
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
			//注意这里的链表的使用
			//向其中添加对象必须是不同的地址空间
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
	
	//更改用户头像
	public String updateImage(UpdateUser newUser) {
        PreparedStatement ps = null;
        try {
            String sql = "update userid set photo=? where email=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newUser.getPhoto());
            ps.setString(2, newUser.getEmail());
            int count = ps.executeUpdate();
            if (count > 0) {
                return "头像更新成功";
            } else {
				return "头像更新失败";
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
	
	//更改用户呢称
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
	                System.out.println("更新呢称成功！");
	            } else {
	                System.out.println("更新呢称失败！");
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
	
	//读取用户除照片所有信息 通过email
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

	/*获取用户喜欢*/
	public SendList getfavority(){
		SendList favorityMessage = new SendList("litenfei",new ArrayList<OneFavorityMessage>());
		List<OneFavorityMessage> ArrayfavMes = new ArrayList<OneFavorityMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select * from favority";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			//注意这里的链表的使用
			//向其中添加对象必须是不同的地址空间
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
	/*删除用户喜欢数据*/
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
				result = "取消成功";
				System.out.print(result);
			}else{
				result = "取消失败";
				System.out.print(result);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	/*插入用户喜欢数据*/
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
				result = "插入成功";
				System.out.println(result);
			} else {
				result = "插入失败";
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
	
	
	//获取用户头像信息
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
	/*获取用户发布动态*/
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
	/*插入用户动态*/
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
				result = "插入成功";
				System.out.println(result);
			} else {
				result = "插入失败";
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
	//删除用户
	public void deleteuser(String email)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from userid where email =?");
			pstmt.setString(1, email);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("删除用户"+email+"成功!");
			else
				System.out.println("无此用户!");
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
			ps = conn.prepareStatement("select * from Relationship where user_email=?");
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
	//删除好友
	public void deletefriend(String username,String friendname)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from Relationship where username=? and friendname=?");
			pstmt.setString(1, username);
			pstmt.setString(2, friendname);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("删除好友"+friendname+"成功!");
			else
				System.out.println("无此好友!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//添加好友
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
				System.out.println(username+"添加好友"+friend.getFriendname()+"成功!");
				return "插入成功";
			}
			else{
				System.out.println("添加失败!");
				return "插入失败";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//添加聊天消息
	public String insertnews(String username,String friendname,String content)
	{
		PreparedStatement pstmt=null;
		String context1 = null;
		try{
			 context1 = querynews(friendname,username).getContent();
		}catch(Exception e){
			context1 = "a";
	}
		if(context1=="a"){
			try {
				pstmt=conn.prepareStatement("insert into message(username,friendname,content) values(?,?,?)");
				pstmt.setString(1, username);
				pstmt.setString(2, friendname);
				pstmt.setString(3, content);
				int result=pstmt.executeUpdate();
				if(result>0)
					System.out.println("添加消息成功");
				else
					System.out.println("添加消息失败!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			try {
				String sql = "update message set content=? where username=? and friendname=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,context1+"-!-"+content);
				pstmt.setString(2, username);
				pstmt.setString(3,friendname);
				int count = pstmt.executeUpdate();
				if (count > 0) {
					System.out.println("添加消息成功");
				} else {
					System.out.println("添加消息失败!");
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != pstmt) {
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	//查询聊天信息
	public MsgData querynews(String username,String friendname){
		MsgData one = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String a = null;
		try{
			ps = conn.prepareStatement("select * from message where username=? and friendname=?");
			ps.setString(1,friendname);
			ps.setString(2,username);
			rs = ps.executeQuery();

			while(rs.next()){
				 a= rs.getString(3);
				 one = new MsgData(username,friendname,a,3,0);
			}
		}catch (Exception e){
			a = "a";
			one = new MsgData(username,friendname,a,3,0);
			return one;
		}
		return one;
	}
	
	//删除聊天消息
	public void deletenews(String username,String friendname)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from message where username=? and friendname=?");
			pstmt.setString(1, username);
			pstmt.setString(2, friendname);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("删除消息成功!");
			else
				System.out.println("删除消息失败!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//根据呢称获取用户id
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
                System.out.println("关闭连接失败！");
                e.printStackTrace();
            }
        }
    }
	
	
	
}












hat3?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
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
	
	//插入用户信息
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
            /*向数据库之中插入Blob的类型的数据*/
//            ps.setBinaryStream(5, in, (long)in.available());
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("插入成功！");
            } else {
                System.out.println("插入失败！");
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

	//获取所有的用户
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
			//注意这里的链表的使用
			//向其中添加对象必须是不同的地址空间
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
	
	//更改用户头像
	public String updateImage(UpdateUser newUser) {
        PreparedStatement ps = null;
        try {
            String sql = "update userid set photo=? where email=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newUser.getPhoto());
            ps.setString(2, newUser.getEmail());
            int count = ps.executeUpdate();
            if (count > 0) {
                return "头像更新成功";
            } else {
				return "头像更新失败";
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
	
	//更改用户呢称
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
	                System.out.println("更新呢称成功！");
	            } else {
	                System.out.println("更新呢称失败！");
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
	
	//读取用户除照片所有信息 通过email
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

	/*获取用户喜欢*/
	public SendList getfavority(){
		SendList favorityMessage = new SendList("litenfei",new ArrayList<OneFavorityMessage>());
		List<OneFavorityMessage> ArrayfavMes = new ArrayList<OneFavorityMessage>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select * from favority";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			//注意这里的链表的使用
			//向其中添加对象必须是不同的地址空间
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
	/*删除用户喜欢数据*/
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
				result = "取消成功";
				System.out.print(result);
			}else{
				result = "取消失败";
				System.out.print(result);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	/*插入用户喜欢数据*/
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
				result = "插入成功";
				System.out.println(result);
			} else {
				result = "插入失败";
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
	
	
	//获取用户头像信息
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
	/*获取用户发布动态*/
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
	/*插入用户动态*/
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
				result = "插入成功";
				System.out.println(result);
			} else {
				result = "插入失败";
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
	//删除用户
	public void deleteuser(String email)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from userid where email =?");
			pstmt.setString(1, email);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("删除用户"+email+"成功!");
			else
				System.out.println("无此用户!");
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
	//删除好友
	public void deletefriend(String username,String friendname)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from Relationship where username=? and friendname=?");
			pstmt.setString(1, username);
			pstmt.setString(2, friendname);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("删除好友"+friendname+"成功!");
			else
				System.out.println("无此好友!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//添加好友
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
				System.out.println(username+"添加好友"+friend.getFriendname()+"成功!");
				return "插入成功";
			}
			else{
				System.out.println("添加失败!");
				return "插入失败";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//添加聊天消息
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
				System.out.println("添加消息成功");
			else
				System.out.println("添加消息失败!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//删除聊天消息
	public void deletenews(String username,String friendname)
	{
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement("delete from message where username=? and friendname=?");
			pstmt.setString(1, username);
			pstmt.setString(2, friendname);
			int result=pstmt.executeUpdate();
			if(result>0)
				System.out.println("删除消息成功!");
			else
				System.out.println("删除消息失败!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//根据呢称获取用户id
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
                System.out.println("关闭连接失败！");
                e.printStackTrace();
            }
        }
    }
	
	
	
}












