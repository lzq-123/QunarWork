package questionFive.socketImpl;

import lombok.extern.slf4j.Slf4j;
import util.MyHttpClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @author zhiqiang.li
 * @date 2022/7/6
 */
@Slf4j
public class MyServerSocket extends Thread{

    private ServerSocket serverSocket;

    private MyHttpClient myHttpClient = new MyHttpClient();

    public MyServerSocket(int port) throws IOException{
        serverSocket = new ServerSocket(port);
//        serverSocket.setSoTimeout(10000);
    }

    public void run(){
        System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
        Socket server;
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            server = serverSocket.accept();
            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true){
            try {
                String url = in.readUTF();
                System.out.println("服务端接收 ：" + url);
                String input = myHttpClient.get(url);
                out.writeUTF(getFindStr(input));
//                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public String getFindStr(String input){
        // 统计总字符数(包括标点符号)、汉字数、
        //英文字符数、标点符号数
        int len = input.length();
        int chiNum = 0, enlNum = 0, pointNum, numCount = 0;
        String replace = input.replaceAll("\\p{P}", "");
        pointNum = len - replace.length();
        for (char c : replace.toCharArray()){
            if (String.valueOf(c).matches("[\u4e00-\u9fa5]")){
                chiNum++;
                continue;
            }
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')){
                enlNum++;
                continue;
            }

            if (c >= '0' && c < '9'){
                numCount++;
            }
        }

        return "总字符 : " + len + " 汉字数 : " + chiNum + " 英文字符数 : " + enlNum + " 标点符号数 : " + pointNum + "数字数 ：" + numCount;
    }

    public static void main(String[] args) {
        int port = Integer.parseInt("6666");

        try {
            Thread t = new MyServerSocket(port);
            t.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
