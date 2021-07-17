package chatroom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.wsl.mychat.MsgData;
import com.wsl.mychat.MyConnection;
import com.wsl.mychat.MyObjectOutputStream;
import com.wsl.mychat.UserData;


public class Server {

    static HashMap<String, Socket>clientlist=new HashMap<>();

    static Socket socket = null;
    static ServerSocket serverSocket = null;
    public Server() {
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("************服务端*************");
        Server t = new Server();
        int count = 0;
        while (true) {          
            try {
//              System.out.println("端口8000等待被连接......");
            	
                socket = serverSocket.accept();
                count++;
                System.out.println("第" + count + "个客户已连接");

            } catch (IOException e) {  
                e.printStackTrace();
            }

            new Thread(new Print(socket)).start();
        }
    }
    
} 

 class Print implements Runnable {
    private Socket socket;
    private boolean first=true;
    public Print(Socket s) {// 构造方法
        try {
            socket=s;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
    	try {
            Thread.sleep(1000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            ObjectInputStream ins=new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            
            while (true) {
                Object ob=null;
                while((ob=ins.readObject())!=null)
                {
                	
                	MsgData msg=(MsgData)ob;
                	if(Server.clientlist.get(msg.getSendID())!=null)
                	{
                		System.out.println(msg.getSendID()+"连接已经存在");
                	}
                	else
                	{
                		Server.clientlist.put(msg.getSendID(), socket);
                	}
                	// 消息类型为1，用户通讯消息
                	if(msg.getWhat()==1)			
                	{
                		
                    	
                    	if(Server.clientlist.get(msg.getReceiveID())!=null)
                    	{
                    		if(first)
                    		{
                    			System.out.println(msg.getSendID()+"对"+msg.getReceiveID()+"说"+msg.getContent());
                        		ObjectOutputStream ous=new ObjectOutputStream(Server.clientlist.get(msg.getReceiveID()).getOutputStream());
                        		ous.writeObject(msg);
                        		ous.flush();
                        		System.out.println("发送成功");
                    			first=false;
                    		}
                    		else {
                    			System.out.println(msg.getSendID()+"对"+msg.getReceiveID()+"说"+msg.getContent());
                        		MyObjectOutputStream ous=new MyObjectOutputStream(Server.clientlist.get(msg.getReceiveID()).getOutputStream());                    		
                        		ous.writeObject(msg);
                        		ous.flush();
                        		System.out.println("发送成功");
                    		}
                    	}else {
                    		System.out.println(msg.getSendID()+"对"+msg.getReceiveID()+"说"+msg.getContent());
                    		//存入数据库....
                    		System.out.println("该用户不在线");
                    	}
                	}
                	// 消息类型为2 用户登录消息
                	else if(msg.getWhat()==2)
                	{
                		MyConnection con=new MyConnection();
                		UserData user=con.getuser(msg.getSendID());
                		if(user==null)
                		{
                			MsgData msg0= new MsgData("", "", "no");
                			msg0.setWhat(2);
                    		ObjectOutputStream ous=new ObjectOutputStream(Server.clientlist.get(msg.getSendID()).getOutputStream());
                    		ous.writeObject(msg0);
                    		ous.flush();
                    		System.out.println("用户登录失败");
                		}
                		else
                		{
                			//检验登录密码是否正确 msg.getReceiveID()为密码字段
                			if(user.getPassword().equals(msg.getReceiveID()))
                			{
                				MsgData msg0= new MsgData("", "", "yes");
                				msg0.setWhat(2);
                        		ObjectOutputStream ous=new ObjectOutputStream(Server.clientlist.get(msg.getSendID()).getOutputStream());
                        		ous.writeObject(msg0);
                        		ous.flush();
                        		System.out.println("用户登录失败");
                			}
                			else
                			{
                				MsgData msg0= new MsgData("", "", "no");
                				msg0.setWhat(2);
                        		ObjectOutputStream ous=new ObjectOutputStream(Server.clientlist.get(msg.getSendID()).getOutputStream());
                        		ous.writeObject(msg0);
                        		ous.flush();
                        		System.out.println("用户登录失败");
                			}
                		}
                		con.close();
                	}
                	// 消息类型为3 用户获取账户信息如头像，好友名字和头像
                	else if(msg.getWhat()==3)
                	{
                		//将用户的好友信息以userdata对象的形式传输
                		MyConnection con=new MyConnection();
                		UserData user=con.getuser(msg.getSendID());
                		Blob photo=con.getPhoto(msg.getSendID());
                		if(user!=null)
                		{
                			ObjectOutputStream ous=new ObjectOutputStream(Server.clientlist.get(msg.getSendID()).getOutputStream());
                    		ous.writeObject(user);
                    		ous.flush();
                    		
                    		
                    		
                    		
                    		System.out.println("用户登录成功");
                		}
                		else
                		{
                			System.out.println(user.getId());
                			System.out.println("用户登录失败");
                		}
                	}
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}








