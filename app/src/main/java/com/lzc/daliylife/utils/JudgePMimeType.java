package com.lzc.daliylife.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by lzc on 2017/9/11.
 *
 */

public class JudgePMimeType {

    private enum MimeType{
        JPEG,
        PNG,
        GIF,
        NONE
    }

    public static MimeType getMimeType(String filePath) {
        MimeType mimeType = null;
        try {
            FileInputStream fis;
            fis = new FileInputStream(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buff = new byte[10];
            int len;
            if ((len=fis.read(buff))!=-1){
                bos.write(buff,0,len);
            }
            //得到图片的字节数组
            byte[] result = bos.toByteArray();
            String hexStr = byte2HexStr(result[0]);
            switch (hexStr){
                case "0xFF":
                    mimeType=MimeType.JPEG;
                    break;
                case "0x89":
                    mimeType=MimeType.PNG;
                    break;
                case "0x47":
                    mimeType=MimeType.GIF;
                    break;
                default:{
                    mimeType=MimeType.NONE;
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mimeType;
    }

    /*
     *  实现字节数组向十六进制的转换方法一
     */
    private static String byte2HexStr(byte b) {
        String hs = "";
        String tmp = (Integer.toHexString(b & 0XFF));
            if (tmp.length() == 1) hs = hs + "0" + tmp;
            else hs = hs + tmp;
        return hs;
    }

}
