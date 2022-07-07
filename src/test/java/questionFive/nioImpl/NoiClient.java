package questionFive.nioImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author zhiqiang.li
 * @date 2022/7/7
 */
public class NoiClient {
    public static void main(String[] args) {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer wirteBuffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);

        SocketChannel channel = null;
        try {
            Thread thread = new Thread(new NioServer(8888));
            thread.start();

            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost", 8888));
            while(true){
                System.out.print("put message to Server:");
                String str = scanner.nextLine();
                if (str.equalsIgnoreCase("bye"))break;
                wirteBuffer.clear();
                wirteBuffer.put(str.getBytes());
                wirteBuffer.flip();
                channel.write(wirteBuffer);
                readBuffer.clear();
                int read = channel.read(readBuffer);
                if (read==-1){
                    break;
                }
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                System.out.println("from server:"+new String(bytes));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
