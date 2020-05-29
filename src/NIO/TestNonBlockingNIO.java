package NIO;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author lch
 * @create 2020- 05 - 28 15:05
 */
public class TestNonBlockingNIO {

    @Test
    public void client() throws IOException {

    }

    public static void main(String[] args) throws IOException {
        //获取客户端通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        //设置客户端通道为非阻塞
        sChannel.configureBlocking(false);

        //设定指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //
        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()){
            String str = sc.next();
            //往缓冲区输入内容
            buf.put((new Date().toString() + "\n" + str).getBytes());

            buf.flip();

            //将缓冲区的内容输送到通道中
            sChannel.write(buf);
            buf.clear();
        }
        //关闭

        sChannel.close();

    }

    @Test
    public void server() throws IOException {
        //获取服务端通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //切换为非阻塞
        ssChannel.configureBlocking(false);

        //绑定端口
        ssChannel.bind(new InetSocketAddress(9898));


        //获取选择器
        Selector selector = Selector.open();

        //将通道注册到选择器，并且指定“监听接受事件”
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);


        //轮询式的获取选择器上已经准备就绪的事件
        while (selector.select()>0){
            //获取当前选择器上注册的“选择键（已经准备就绪的监听事件）”
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey selectionKey = it.next();

                //判断具体是什么事件就绪
                if(selectionKey.isAcceptable()){

                    //如果监听事件就绪的为接受状态
                    SocketChannel sCannel = ssChannel.accept();

                    //切换为非阻塞
                    sCannel.configureBlocking(false);

                    //将通道注册到选择器上
                    sCannel.register(selector,SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    //获取选择器上“读就绪”的通道
                    SocketChannel sChannel = (SocketChannel) selectionKey.channel();

                    //读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len = 0;
                    while ((len = sChannel.read(buf)) != -1){
                        buf.flip();
                        System.out.println(new String(buf.array(),0,len));
                        buf.clear();
                    }

                }
                //取消选择键 SelectedKey
                it.remove();
            }
        }







    }
}
