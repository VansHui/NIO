package NIO;

import com.sun.corba.se.impl.monitoring.MonitoredAttributeInfoImpl;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Scanner;

/**
 * @author lch
 * @create 2020- 05 - 27 20:45
 */
public class wangluotongxin {

    //利用TCP协议
    @Test
    public void client() throws IOException {

    }

    public static void main(String[] args) throws IOException {
        while (true){
            //获取客户端通道
            SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));

            //设置Host和端口
            Scanner sc = new Scanner(System.in);
            //指定缓冲区大小
            ByteBuffer bBuf = ByteBuffer.allocate(1024);

            //往缓冲区中写入数据
            bBuf.put((new Date().toString()+"\n"+sc.next()).getBytes());
            bBuf.flip();
            sChannel.write(bBuf);
            sChannel.close();
        }
    }

    @Test
    public void server() throws IOException {
        while (true){
            ServerSocketChannel ssChannel = ServerSocketChannel.open();
            ssChannel.bind(new InetSocketAddress(9898));
            SocketChannel sChannel = ssChannel.accept();
            System.out.println(sChannel.getLocalAddress());
            ByteBuffer bBuf = ByteBuffer.allocate(1024);

            int len;
            while ((len = sChannel.read(bBuf) )!= -1){
                bBuf.flip();
                System.out.println(new String(bBuf.array(),0,len));
                bBuf.clear();
            }

            ssChannel.close();
            sChannel.close();
        }
    }
}
