//package chatroom;
//
//import com.example.foodrecipes.model.MsgData;
//
//import java.io.*;
//import java.net.Socket;
//import java.util.Scanner;
//public class Client implements Runnable {// 客户端
//    static Socket socket = null;
//    Scanner input = new Scanner(System.in);
//    static String name=null;
//    public static void main(String[] args) {
//    	int x=(int)(Math.random()*100);
//    	Client.name="client"+x;
//    	System.out.println("************客户端"+x+"*************");
//        try {
//            socket = new Socket("10.129.98.59", 8888);
//            System.out.println("已经连上服务器了");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Client t = new Client();
//        Read r = new Read(socket);
//        Thread print = new Thread(t);
//        Thread read = new Thread(r);
//        print.start();
//        read.start();
//    }
//    @Override
//    public void run() {
//        try {
//            Thread.sleep(1000);
//            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
////            PrintWriter out = new PrintWriter(socket.getOutputStream());
//            while (true) {
//                String msg = input.next();
//                out.writeObject(new MsgData("1","2",msg,0,1));
//                System.out.print("完成输出");
//                out.flush();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//class Read implements Runnable {
//    static Socket socket = null;
//    public Read(Socket socket) {
//        this.socket = socket;
//    }
//    @Override
//    public void run() {
//        try {
//            Thread.sleep(1000);
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket
//                    .getInputStream()));
//            while (true) {
//                System.out.println(  in.readLine());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
