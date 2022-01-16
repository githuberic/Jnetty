package com.lgq.nio.ioe.filee;

import com.lgq.nio.common.util.IOUtil;
import com.lgq.nio.common.util.NioEConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lgq
 */
public class FileNIOCopyE {

    /**
     * 程序的入口函数
     *
     * @param args
     */
    public static void main(String[] args) {
        // 复制资源文件
        nioCopyResouceFile();
    }


    /**
     * 复制两个资源目录下的文件
     */
    public static void nioCopyResouceFile() {
        String sourcePath = NioEConfig.FILE_RESOURCE_SRC_PATH;
        String srcPath = IOUtil.getResourcePath(sourcePath);
        System.out.println("srcPath=" + srcPath);

        String destShortPath = NioEConfig.FILE_RESOURCE_DEST_PATH;
        String destdePath = IOUtil.builderResourcePath(destShortPath);
        System.out.println("destdePath=" + destdePath);

        nioCopyFile(srcPath, destdePath);
    }

    /**
     * 复制文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void nioCopyFile(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);

        try {
            //如果目标文件不存在，则新建
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            long startTime = System.currentTimeMillis();

            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel inChannel = null;
            FileChannel outchannel = null;
            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                inChannel = fis.getChannel();
                outchannel = fos.getChannel();

                int length = -1;
                ByteBuffer buf = ByteBuffer.allocate(1024);

                //从输入通道读取到buf
                while ((length = inChannel.read(buf)) != -1) {
                    //翻转buf,变成成读模式
                    buf.flip();

                    int outLength = 0;
                    //将buf写入到输出的通道
                    while ((outLength = outchannel.write(buf)) != 0) {
                        System.out.println("写入字节数：" + outLength);
                    }
                    //清除buf,变成写入模式
                    buf.clear();
                }


                //强制刷新磁盘
                outchannel.force(true);
            } finally {
                IOUtil.closeQuietly(outchannel);
                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(inChannel);
                IOUtil.closeQuietly(fis);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("base 复制毫秒数：" + (endTime - startTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
