package test;

import sun.applet.Main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2015/9/12 0012.
 */
public class InputStremNio {



    //nio文件复制功能
    public static void nioCopy(String infile,String outfile)throws  Exception{
        //输入流
        FileInputStream fin = new FileInputStream(infile);
        //输出流
        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel fcin =fin.getChannel();
        FileChannel fcout =fout.getChannel();
        ByteBuffer buffer =ByteBuffer.allocate(1024);

        while(true){
            buffer.clear();
            int r = fcin.read(buffer);
            if(r == -1){
                break;
            }
            buffer.flip();
            fcout.write(buffer);
        }
        fcin.close();
        fcout.close();
        fin.close();
        fout.close();
    }

    //普通I/O文件复制功能
    public static void normalCopy(String infile,String outfile)throws Exception{
        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        byte[] block = new byte[1024];
        while(fin.read(block) != -1){
            fout.write(block);
        }
        fin.close();
        fout.close();
    }

    public static void main(String[] args)throws Exception {
        String infile1 ="E:\\tests\\a\\log_server.log.2015-09-09-09";
        String outfile2 ="E:\\tests\\b\\test2.txt";
        long start =System.currentTimeMillis();
        nioCopy(infile1, outfile2);
        long end1 =System.currentTimeMillis();
        normalCopy(infile1,outfile2);
        long end2 = System.currentTimeMillis();
        System.out.println("nio copy lasts:" + (end1 - start));
        System.out.println("normal copy lasts: " + (end2 - end1));
    }


}
