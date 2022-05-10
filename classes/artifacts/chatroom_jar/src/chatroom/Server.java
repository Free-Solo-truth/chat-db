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
	/*ͨ��id�洢��Ӧ��socket���ӣ����������ĶԶ�Ӧ��socket���ӵĲ���*/
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
        System.out.println("************�����*************");
        Server t = new Server();
        int count = 0;
        while (true) {
            try {
//              System.out.println("�˿�8000�ȴ�������......");

                socket = serverSocket.accept();
                socket.setSoTimeout(1000000);
                count++;
                System.out.println("��" + count + "���ͻ�������");
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
    public Print(Socket s,ObjectInputStream ins) {// ���췽��
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

			/*����������ö�ʱ�ر�socket�������*/
					while (true) {
						Object ob=null;

						while((ob=ins.readObject())!=null) {
							if (ob instanceof MsgData) {/*��ȡ�Ķ�����MsgData����*/

								System.out.print(ob.toString());
//					private static final long serialVersionUID = 1L;
								msg = (MsgData) ob;
								System.out.print(msg.getSendName());
								Server.username = msg.getSendName();
								if (Server.clientlist.get(msg.getSendName()) != null) {
									System.out.println(msg.getSendName() + "�����Ѿ�����");
								} else {
									Server.clientlist.put(msg.getSendName(), socket);
								}
								// ��Ϣ����Ϊ1���û�ͨѶ��Ϣ
								if (msg.getWhat() == 1) {
									System.out.print("Success1");
									/*��ȡ��Ӧ��socket����*/
									if (Server.clientlist.get(msg.getReceiveName()) != null) {
										if (first) {
											System.out.println(msg.getSendName() + "��" + msg.getReceiveName() + "˵" + msg.getContent());
											/*��Ϣ��ת����ת���Ķ����Ǳ�����clientlist���е�һԱ�����ӳɹ�֮��ͻᱣ��������*/
											ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getReceiveName()).getOutputStream());
											ous.writeObject(msg);
											ous.flush();
											System.out.println("���ͳɹ�");
											first = false;
										} else {
											System.out.println(msg.getSendName() + "��" + msg.getReceiveName() + "˵" + msg.getContent());
											MyObjectOutputStream ous = new MyObjectOutputStream(Server.clientlist.get(msg.getReceiveName()).getOutputStream());
											ous.writeObject(msg);
											ous.flush();
											System.out.println("���ͳɹ�");
										}
									} else {
										System.out.println(msg.getSendName() + "��" + msg.getReceiveName() + "˵" + msg.getContent());
										//�������ݿ�....
										System.out.println("���û�������");
									}
								}
								// ��Ϣ����Ϊ2 �û���¼��Ϣ �����֤
								else if (msg.getWhat() == 2) {
									MyConnection con = new MyConnection();
									/*��ѯ���ݿ��ȡ�û�����Ϣͨ�����͵���Ϣ��Name*/
									UserData user = con.getuser(msg.getSendName());/*��ȡ����*/
									if (user == null) {
										MsgData msg0 = new MsgData("", "", "�û�������", 0, 0);
										msg0.setWhat(2);
										ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
										/*���ز�����Ϣ*/
										ous.writeObject(msg0);
										ous.flush();
										System.out.println("�û���¼ʧ��");
									} else {
										//�����¼�����Ƿ���ȷ msg.getContent()Ϊ�����ֶ�
										if (user.getPassword().equals(msg.getContent())) {/*��ȡ����*/
											MsgData msg0 = new MsgData("", "", "��¼�ɹ�", 0, 0);
											msg0.setWhat(2);
											ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
											ous.writeObject(msg0);
											ous.flush();
											System.out.println("�û���¼�ɹ�");
										} else {
											MsgData msg0 = new MsgData("", "", "�������", 0, 0);
											msg0.setWhat(2);
											ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
											ous.writeObject(msg0);
											ous.flush();
											System.out.println("�û���¼ʧ��");
										}
									}
									con.close();
								}
								// ��Ϣ����Ϊ3 �û���ȡ�˻���Ϣ��ͷ�񣬺������ֺ�ͷ��
								else if (msg.getWhat() == 3) {
									//���û��ĺ�����Ϣ��userdata�������ʽ����
									MyConnection con = new MyConnection();
									System.out.print("�ɹ�����");
									UserData user = con.getuser(msg.getContent());
									if (user != null) {
										ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
										ous.writeObject(user);
										ous.flush();
										System.out.println("�û���¼�ɹ�");
									} else {
										System.out.println("�û���¼ʧ��");
									}
									con.close();
								}
								/*�û�ע����Ϣ*/
								else if (msg.getWhat() == 4) {
									MyConnection con = new MyConnection();
									/*��ѯ���ݿ��ȡ�û�����Ϣͨ�����͵���Ϣ��Name*/
									UserData user = con.getuser(msg.getContent());
									if (user != null) {
										MsgData msg0 = new MsgData("", "", "�û��Ѵ���", 0, 0);
										msg0.setWhat(2);
										ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
										/*���ز�����Ϣ*/
										ous.writeObject(msg0);
										ous.flush();
										System.out.println("�û�ע��ʧ��");
									} else {
										/**/
										con.insertImage(user);
									}
									con.close();
								} else if (msg.getWhat() == 5) {
									/*��ȡϲ��������*/
									System.out.print("���ڻ�ȡϲ��������");
									MyConnection con = new MyConnection();

									SendList ArrayFavority = con.getfavority();
//								System.out.print("���ڻ�ȡϲ��������");
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(ArrayFavority);
									out.flush();

									System.out.print("��ȡϲ�����ݳɹ�");
									con.close();

								} else if (msg.getWhat() == 6) {
									/*ɾ��ϲ������*/
									MyConnection con = new MyConnection();

									String result = con.deleteFavorityInfo(msg.getContent());
									MsgData sendMessage = new MsgData(null, null, result, 0, 0);
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(sendMessage);
									out.flush();
									con.close();
								} else if (msg.getWhat() == 7) {
									/*��ȡ����Ȧ��̬*/
									MyConnection con = new MyConnection();
									Carrier_DynamicMessage result = con.queryDynamic_message();
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(result);
									System.out.print("��ȡϲ������Ȧ�ɹ�");
									out.flush();
									con.close();
								} else if (msg.getWhat() == 8) {
									System.out.print("��ʼ");

									MyConnection con = new MyConnection();
									SendRelationship result = con.queryfriend(msg.getSendName());
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(result);
									System.out.print("��ȡ���ѳɹ�");
									out.flush();
									con.close();
								}else if(msg.getWhat() == 9){
									MyConnection con = new MyConnection();
									SendUserData result = con.queryAlluser();
									ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(msg.getSendName()).getOutputStream());
									out.writeObject(result);
									System.out.print("��ȡ���е��û��ɹ�");
									out.flush();
									con.close();
								}
							} else if (ob instanceof UserData) {
								/*��ȡ�Ķ�����UserData*/
								/*���в������*/
								MyConnection con = new MyConnection();
								UserData user = (UserData) ob;
								UserData isExist = con.getuser(user.getEmail());
								if (Server.clientlist.get(user.getName()) != null) {
									System.out.println(user.getName() + "�����Ѿ�����");
								} else {
									Server.clientlist.put(user.getName(), socket);
								}
								if (isExist != null) {
									ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(user.getName()).getOutputStream());
									ous.writeObject(new MsgData("", "", "�����Ѵ���", 0, 0));
								} else {
									con.insertImage(user);
									MsgData msg0 = new MsgData("", "", "ע��ɹ�", 0, 0);
									ObjectOutputStream ous = new ObjectOutputStream(Server.clientlist.get(user.getName()).getOutputStream());
									ous.writeObject(msg0);
									ous.flush();
									System.out.print("����ɹ�");
								}
								con.close();
							} else if (ob instanceof Carrier_FavorityMessage) {
								/*����ϲ������*/
								MyConnection con = new MyConnection();
								Carrier_FavorityMessage FavInfo = (Carrier_FavorityMessage) ob;
								/*��ʼ����*/
								String result = con.InsertFavorityInfo(FavInfo.getFavMeg());
								ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(FavInfo.getSendName()).getOutputStream());
								out.writeObject(new MsgData(null, null, result, 0, 0));
								out.flush();
								con.close();
							} else if (ob instanceof Carrier_DynamicMessage) {
								/*��������Ȧ����*/
								MyConnection con = new MyConnection();
								Carrier_DynamicMessage DyMsg = (Carrier_DynamicMessage) ob;
								String result = con.insertDynamic_Message(DyMsg);
								ObjectOutputStream out = new ObjectOutputStream(Server.clientlist.get(DyMsg.getSendName()).getOutputStream());
								out.writeObject(new MsgData(null, null, result, 0, 0));
								out.flush();
								con.close();
							}
							else if (ob instanceof Relationship){
								/*�������*/
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

				System.out.print("���ӽ���");
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
        }
    }


}








