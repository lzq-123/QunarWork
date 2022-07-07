package questionFive.nioImpl;

import util.MyHttpClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author zhiqiang.li
 * @date 2022/7/7
 */
public class NioServer implements Runnable{

    private ByteBuffer readBuf = ByteBuffer.allocate(1024);

    private ByteBuffer writeBuf = ByteBuffer.allocate(1024);

    private MyHttpClient myHttpClient = new MyHttpClient();

    private Selector selector;

    public NioServer(int port) throws IOException{
        init(port);
    }

    private void init(int port) throws IOException {
        // 1. 打开多路复用器
        this.selector = Selector.open();
        // 2. 打开服务器端通道
        ServerSocketChannel open = ServerSocketChannel.open();
        // 3. 设置通道为阻塞模式
        open.configureBlocking(false);
        // 4.  绑定地址
        open.bind(new InetSocketAddress(port));
        open.register(this.selector, SelectionKey.OP_ACCEPT);

    }


    @Override
    public void run() {
        while (true){
            try {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isValid()){
                        if (key.isAcceptable()) {
                            accept(key);
                        }
                        // 8 如果为 OP_READ  可读状态
                        if (key.isReadable()) {
                            read(key);
                        }
                        if (key.isWritable()){
                            write(key);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void read(SelectionKey key) {
        //1 清空缓冲区
        this.readBuf.clear();
        //2 获取之前注册的SocketChannel通道对象
        SocketChannel sc = (SocketChannel) key.channel();
        try {
            //3 从通道获取数据放入缓冲区
            int index = sc.read(readBuf);
            if (index == -1) {
                //关闭通道
                key.channel().close();
                //取消key
                key.cancel();
                return;
            }
            //4 由于 sc 通道里的数据流入到 readBuf 容器中，所以 readBuf里面的 position一定发生了变化， 必须进行复位
            readBuf.flip();
            // 读取readBuf数据 然后打印数据
            byte[] bytes = new byte[readBuf.remaining()];
            readBuf.get(bytes);
            String body = new String(bytes).trim();
            System.out.println("服务器读取到客户SocketChannel端数据：" + body);
            // 5 写出数据
            //清空缓冲区
            writeBuf.clear();
            String res = myHttpClient.get(body);
            byte[] writeBytes = getFindStr(res).getBytes();
            writeBuf.put(writeBytes);
            writeBuf.flip();
            sc.write(writeBuf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void accept(SelectionKey key){
        try {
            // 1 由于目前是 server端，那么一定是server端启动，并且处于阻塞状态，所以获取阻塞状态的key 一定是 ：服务端管道 ServerSocketChannel
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            // 2 通过调用 accept 方法，返回一个具体的客户端连接管道
            SocketChannel sc = ssc.accept();
            // 3 设置为非阻塞
            sc.configureBlocking(false);
            // 4 设置当前获取的客户端连接管道为可读状态
            sc.register(this.selector, SelectionKey.OP_READ);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(SelectionKey key){
        try {
            writeBuf.clear();
            SocketChannel channel = (SocketChannel) key.channel();
            System.out.println("put message to client");
            Scanner scanner = new Scanner(System.in);
            String str = scanner.nextLine();
            writeBuf.put(str.getBytes());
            writeBuf.flip();
            channel.write(writeBuf);
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
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

}
