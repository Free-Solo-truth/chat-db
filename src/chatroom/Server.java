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
        System.out.println("************�����*************");
        Server t = new Server();
        int count = 0;
        while (true) {          
            try {
//              System.out.println("�˿�8000�ȴ�������......");
            	
                socket = serverSocket.accept();
                count++;
                System.out.println("��" + count + "���ͻ�������");

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
    public Print(Socket s) {// ���췽��
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
                		System.out.println(msg.getSendID()+"�����Ѿ�����");
                	}
                	else
                	{
                		Server.clientlist.put(msg.getSendID(), socket);
                	}
                	// ��Ϣ����Ϊ1���û�ͨѶ��Ϣ
                	if(msg.getWhat()==1)			
                	{
                		
                    	
                    	if(Server.clientlist.get(msg.getReceiveID())!=null)
                    	{
                    		if(first)
                    		{
                    			System.out.println(msg.getSendID()+"��"+msg.getReceiveID()+"˵"+msg.getContent());
                        		ObjectOutputStream ous=new ObjectOutputStream(Server.clientlist.get(msg.getReceiveID()).getOutputStream());
                        		ous.writeObject(msg);
                        		ous.flush();
                        		System.out.println("���ͳɹ�");
                    			first=false;
                    		}
                    		else {
                    			System.out.println(msg.getSendID()+"��"+msg.getReceiveID()+"˵"+msg.getContent());
                        		MyObjectOutputStream ous=new MyObjectOutputStream(Server.clientlist.get(msg.getReceiveID()).getOutputStream());                    		
                        		ous.writeObject(msg);
                        		ous.flush();
                        		System.out.println("���ͳɹ�");
                    		}
                    	}else {
                    		System.out.println(msg.getSendID()+"��"+msg.getReceiveID()+"˵"+msg.getContent());
                    		//�������ݿ�....
                    		System.out.println("���û�������");
                    	}
                	}
                	// ��Ϣ����Ϊ2 �û���¼��Ϣ
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
                    		System.out.println("�û���¼ʧ��");
                		}
                		else
                		{
                			//�����¼�����Ƿ���ȷ msg.getReceiveID()Ϊ�����ֶ�
                			if(user.getPassword().equals(msg.getReceiveID()))
                			{
                				MsgData msg0= new MsgData("", "", "yes");
                				msg0.setWhat(2);
                        		ObjectOutputStream ous=new ObjectOutputStream(Server.clientlist.get(msg.getSendID()).getOutputStream());
                        		ous.writeObject(msg0);
                        		ous.flush();
                        		System.out.println("�û���¼ʧ��");
                			}
                			else
                			{
                				MsgData msg0= new MsgData("", "", "no");
                				msg0.setWhat(2);
                        		ObjectOutputStream ous=new ObjectOutputStream(Server.clientlist.get(msg.getSendID()).getOutputStream());
                        		ous.writeObject(msg0);
                        		ous.flush();
                        		System.out.println("�û���¼ʧ��");
                			}
                		}
                		con.close();
                	}
                	// ��Ϣ����Ϊ3 �û���ȡ�˻���Ϣ��ͷ�񣬺������ֺ�ͷ��
                	else if(msg.getWhat()==3)
                	{
                		//���û��ĺ�����Ϣ��userdata�������ʽ����
                		MyConnection con=new MyConnection();
                		UserData user=con.getuser(msg.getSendID());
                		Blob photo=con.getPhoto(msg.getSendID());
                		if(user!=null)
                		{
                			ObjectOutputStream ous=new ObjectOutputStream(Server.clientlist.get(msg.getSendID()).getOutputStream());
                    		ous.writeObject(user);
                    		ous.flush();
                    		
                    		
                    		
                    		
                    		System.out.println("�û���¼�ɹ�");
                		}
                		else
                		{
                			System.out.println(user.getId());
                			System.out.println("�û���¼ʧ��");
                		}
                	}
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}








