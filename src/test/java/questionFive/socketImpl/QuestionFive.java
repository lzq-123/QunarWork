package questionFive.socketImpl;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class QuestionFive {

    public static void main(String[] args) throws IOException {
        String serverName = "localhost";
        int port = Integer.parseInt("6666");

        MyServerSocket myServerSocket = new MyServerSocket(port);
        myServerSocket.start();

        try {
            Socket client = new Socket(serverName, port);
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

            while (true){
                Scanner sc = new Scanner(System.in);
                System.out.print("请输入 ：");
                String next = sc.next();
                out.writeUTF(next);
                System.out.println("服务端响应 ： " + in.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

