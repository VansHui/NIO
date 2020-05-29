package NIO;

import com.sun.xml.internal.stream.writers.UTF8OutputStreamWriter;
import org.junit.Test;
import sun.java2d.pipe.OutlineTextRenderer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Scanner;

/**
 * @author lch
 * @create 2020- 05 - 27 22:26
 */
public class NewIO {

    public static void main(String[] args) throws IOException {
        //获取客户端通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        //获得传输文件的通道
        FileChannel inChannel = FileChannel.open(Paths.get("src//3.jpg"), StandardOpenOption.READ);
        ByteBuffer bBuf = ByteBuffer.allocate(1024);

        while (inChannel.read(bBuf) != -1) {
            bBuf.flip();
            socketChannel.write(bBuf);
            bBuf.clear();
        }

        socketChannel.shutdownOutput();
        //接收反馈
        int len = 0;
        while ((len = socketChannel.read(bBuf)) != -1){
            bBuf.flip();
            System.out.println(new String(bBuf.array(),0,len));
            bBuf.clear();
        }
        inChannel.close();
        socketChannel.close();
    }



    @Test
    public void server() throws IOException {
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.bind(new InetSocketAddress(9898));
        SocketChannel sChannel = ssChannel.accept();

        FileChannel OutChannel = FileChannel.open(Paths.get("src//5.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        ByteBuffer bBuf = ByteBuffer.allocate(1024);

        while (sChannel.read(bBuf) != -1) {
            bBuf.flip();
            OutChannel.write(bBuf);
            bBuf.clear();
        }

        bBuf.put("服务端接受数据成功".getBytes());
        bBuf.flip();
        sChannel.write(bBuf);

        OutChannel.close();
        ssChannel.close();
        sChannel.close();
    }
}











