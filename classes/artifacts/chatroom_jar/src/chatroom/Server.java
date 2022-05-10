package chatroom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;


import com.example.foodrecipes.model.*;
import com.wsl.mychat.MyConnection;
import com.wsl.mychat.MyObjectOutputStream;


public class Server {
	/*通过id存储相应的socket连接，用来后续的对对应的socket连接的操作*/
    static HashMap<String, Socket>clientlist=new HashMap<>();
    static String username = new String();

    static Socket socket = null;
	static ObjectInputStream ins =null;
    static ServerSocket serverSocket = null;
    public Server() {
        try {
            serverSocket = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("************服务端*************");
        Server t = new Server();
        int count = 0;
        while (true) {
            try {
//              System.out.println("端口8000等待被连接......");

                socket = serverSocket.accept();
                socket.setSoTimeout(1000000);
                count++;
                System.out.println("第" + count + "个客户已连接");
				 ins=new ObjectInputStream(socket.getInputStream());
				System.out.print("Success1");
			} catch (IOException e) {
            	socket.close();
                e.printStackTrace();
            }

            new Thread(new Print(socket,ins)).start();
        }
    }
    
}

 class Print implements Runnable {
    private Socket socket;
	 ObjectInputStream ins;
    private boolean first=true;
    public Print(Socket s,ObjectInputStream ins) {// 构造方法
        try {
        	this.ins = ins;
            socket=s;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
    	MsgData msg = null;

    	try {

            Thread.sleep(1000);
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket
//                    .getInputStream()));
//			ObjectInputStream ins=new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			System.out.print("Success");

			/*这里可以设置定时关闭socket的输出流*/
					while (true) {
						Object ob=null;

						while((ob=ins.readObject())!=null) {
							if (ob instanceof MsgData) {/*读取的对象是MsgData类型*/

								System.out.print(ob.toString());
//					private static final long serialVersionUID = 1L;
								msg = (MsgData) ob;
								System.out.print(msg.getSendName());
								Server.username = msg.getSendName();
								if (Server.clientlist.get(msg.getSendName()) != null) {
									System.out.println(msg.getSendName() + "连接已经存在");
								} else {
									Server.clientlist.put(msg.getSendName(), socket);
								}
								// 消息类型为1，用户通讯消息
								if (msg.getWhat() == 1) {
									System.out.print("Success1");
									/*获取对应的socket连接*/
									if (Server.clientlist.get(msg.getReceiveName()) != null) {
										if (first) {
											System.out.println(msg.getSendName() + "对" + msg.getReceiveName() + "说" + msg.getContent());
											/*消息的转发，转发的对象是保存在clientlist其中的一员，连接成功之后就会保存在其中*/
											ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getReceiveName()).getOutputStream());
											ous.writeObject(msg);
											ous.flush();
											System.out.println("发送成功");
											first = false;
										} else {
											System.out.println(msg.getSendName() + "对" + msg.getReceiveName() + "说" + msg.getContent());
											MyObjectOutputStream ous = new MyObjectOutputStream(Server.clientlist.get(msg.getReceiveName()).getOutputStream());
											ous.writeObject(msg);
											ous.flush();
											System.out.println("发送成功");
										}
									} else {
										System.out.println(msg.getSendName() + "对" + msg.getReceiveName() + "说" + msg.getContent());
										//存入数据库....
										System.out.println("该用户不在线");
									}
								}
								// 消息类型为2 用户登录消息 身份验证
								else if (msg.getWhat() == 2) {
									MyConnection con = new MyConnection();
									/*查询数据库获取用户的信息通过传送的信息的Name*/
									UserData user = con.getuser(msg.getSendName());/*拿取邮箱*/
									if (user == null) {
										MsgData msg0 = new MsgData("", "", "用户不存在", 0, 0);
										msg0.setWhat(2);
										ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
										/*返回操作信息*/
										ous.writeObject(msg0);
										ous.flush();
										System.out.println("用户登录失败");
									} else {
										//检验登录密码是否正确 msg.getContent()为密码字段
										if (user.getPassword().equals(msg.getContent())) {/*拿取密码*/
											MsgData msg0 = new MsgData("", "", "登录成功", 0, 0);
											msg0.setWhat(2);
											ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
											ous.writeObject(msg0);
											ous.flush();
											System.out.println("用户登录成功");
										} else {
											MsgData msg0 = new MsgData("", "", "密码错误", 0, 0);
											msg0.setWhat(2);
											ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
											ous.writeObject(msg0);
											ous.flush();
											System.out.println("用户登录失败");
										}
									}
									con.close();
								}
								// 消息类型为3 用户获取账户信息如头像，好友名字和头像
								else if (msg.getWhat() == 3) {
									//将用户的好友信息以userdata对象的形式传输
									MyConnection con = new MyConnection();
									System.out.print("成功进入");
									UserData user = con.getuser(msg.getContent());
									if (user != null) {
										ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
										ous.writeObject(user);
										ous.flush();
										System.out.println("用户登录成功");
									} else {
										System.out.println("用户登录失败");
									}
									con.close();
								}
								/*用户注册信息*/
								else if (msg.getWhat() == 4) {
									MyConnection con = new MyConnection();
									/*查询数据库获取用户的信息通过传送的信息的Name*/
									UserData user = con.getuser(msg.getContent());
									if (user != null) {
										MsgData msg0 = new MsgData("", "", "用户已存在", 0, 0);
										msg0.setWhat(2);
										ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
										/*返回操作信息*/
										ous.writeObject(msg0);
										ous.flush();
										System.out.println("用户注册失败");
									} else {
										/**/
										con.insertImage(user);
									}
									con.close();
								} else if (msg.getWhat() == 5) {
									/*获取喜欢的数据*/
									System.out.print("正在获取喜欢的数据");
									MyConnection con = new MyConnection();

									SendList ArrayFavority = con.getfavority();
//								System.out.print("正在获取喜欢的数据");
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(ArrayFavority);
									out.flush();

									System.out.print("获取喜欢数据成功");
									con.close();

								} else if (msg.getWhat() == 6) {
									/*删除喜欢数据*/
									MyConnection con = new MyConnection();

									String result = con.deleteFavorityInfo(msg.getContent());
									MsgData sendMessage = new MsgData(null, null, result, 0, 0);
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(sendMessage);
									out.flush();
									con.close();
								} else if (msg.getWhat() == 7) {
									/*获取盆友圈动态*/
									MyConnection con = new MyConnection();
									Carrier_DynamicMessage result = con.queryDynamic_message();
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(result);
									System.out.print("获取喜欢盆友圈成功");
									out.flush();
									con.close();
								} else if (msg.getWhat() == 8) {
									System.out.print("开始");

									MyConnection con = new MyConnection();
									SendRelationship result = con.queryfriend(msg.getSendName());
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(result);
									System.out.print("获取好友成功");
									out.flush();
									con.close();
								}else if(msg.getWhat() == 9){
									MyConnection con = new MyConnection();
									SendUserData result = con.queryAlluser();
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(result);
									System.out.print("获取所有的用户成功");
									out.flush();
									con.close();
								}
							} else if (ob instanceof UserData) {
								/*读取的对象是UserData*/
								/*进行插入对象*/
								MyConnection con = new MyConnection();
								UserData user = (UserData) ob;
								UserData isExist = con.getuser(user.getEmail());
								if (Server.clientlist.get(user.getName()) != null) {
									System.out.println(user.getName() + "连接已经存在");
								} else {
									Server.clientlist.put(user.getName(), socket);
								}
								if (isExist != null) {
									ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(user.getName()).getOutputStream());
									ous.writeObject(new MsgData("", "", "邮箱已存在", 0, 0));
								} else {
									con.insertImage(user);
									MsgData msg0 = new MsgData("", "", "注册成功", 0, 0);
									ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(user.getName()).getOutputStream());
									ous.writeObject(msg0);
									ous.flush();
									System.out.print("插入成功");
								}
								con.close();
							} else if (ob instanceof Carrier_FavorityMessage) {
								/*插入喜欢数据*/
								MyConnection con = new MyConnection();
								Carrier_FavorityMessage FavInfo = (Carrier_FavorityMessage) ob;
								/*开始插入*/
								String result = con.InsertFavorityInfo(FavInfo.getFavMeg());
								ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(FavInfo.getSendName()).getOutputStream());
								out.writeObject(new MsgData(null, null, result, 0, 0));
								out.flush();
								con.close();
							} else if (ob instanceof Carrier_DynamicMessage) {
								/*插入盆友圈数据*/
								MyConnection con = new MyConnection();
								Carrier_DynamicMessage DyMsg = (Carrier_DynamicMessage) ob;
								String result = con.insertDynamic_Message(DyMsg);
								ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(DyMsg.getSendName()).getOutputStream());
								out.writeObject(new MsgData(null, null, result, 0, 0));
								out.flush();
								con.close();
							}
							else if (ob instanceof Relationship){
								/*插入好友*/
								MyConnection con = new MyConnection();
								Relationship DyMsg = (Relationship) ob;
								System.out.print(DyMsg.getFriendname()+DyMsg.getFriendemail()+DyMsg.getUsername()+DyMsg.getFriendphone());
								String result = con.insertfriend(DyMsg);
								ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(DyMsg.getUseremail()).getOutputStream());
								out.writeObject(new MsgData(null, null, result, 0, 0));
								out.flush();
								con.close();
							}
							else if(ob instanceof UpdateUser){
								MyConnection con = new MyConnection();
								UpdateUser UpdateUser = (UpdateUser) ob;
								String result = con.updateImage(UpdateUser);
								ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(UpdateUser.getEmail()).getOutputStream());
								out.writeObject(new MsgData(null, null, result, 0, 0));
								out.flush();
								con.close();
							}
						}
            }
        } catch (Exception e) {
			try {

				Server.clientlist.remove(msg.getSendName());
				System.out.print(msg.getSendName());

				System.out.print("连接结束");
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
        }
    }


}








