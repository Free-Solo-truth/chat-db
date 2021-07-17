package chatroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
public class Client implements Runnable {// �ͻ���
    static Socket socket = null;
    Scanner input = new Scanner(System.in);
    static String name=null;
    public static void main(String[] args) {
    	int x=(int)(Math.random()*100);
    	Client.name="client"+x;
    	System.out.println("************�ͻ���"+x+"*************");
        try {
            socket = new Socket("127.0.0.1", 9999);
            System.out.println("�Ѿ����Ϸ�������");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Client t = new Client();
        Read r = new Read(socket);
        Thread print = new Thread(t);
        Thread read = new Thread(r);
        print.start();
        read.start();
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);         
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while (true) {
                String msg = input.next();
                out.println(name+"˵:"+msg);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class Read implements Runnable {
    static Socket socket = null;
    public Read(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            while (true) {
                System.out.println(  in.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
