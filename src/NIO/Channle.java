package NIO;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author lch
 * @create 2020- 05 - 27 18:58
 */
public class Channle {
    //使用直接缓存区复制文件
    @Test
    public void test2() throws IOException {
        //使用jdk1.7 NIO2 的通道open()方法获得通道
        FileChannel inChannel = FileChannel.open(Paths.get("src//huishao.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("src//3.jpg"), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);

        //内存映射文件
        MappedByteBuffer inMapBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMapBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //直接对缓冲区进行读写操作
        byte[] dest = new byte[inMapBuf.limit()];
        inMapBuf.get(dest);
        outMapBuf.put(dest);

        inChannel.close();
        outChannel.close();


    }

    //使用非直接缓冲区复制文件
    @Test
    public void test1() throws FileNotFoundException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inchannel = null;
        FileChannel oschannel = null;
        try {
            fis = new FileInputStream("src\\huishao.jpg");
            fos = new FileOutputStream("src\\huishao1.jpg");

            //1获取通道
            inchannel = fis.getChannel();
            oschannel = fos.getChannel();

            //分配缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            //将数据写入到缓冲区中
            while (inchannel.read(byteBuffer) != -1){
                //切换到读取模式
                byteBuffer.flip();
                oschannel.write(byteBuffer);

                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inchannel != null)
                    inchannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(oschannel != null)
                     oschannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
