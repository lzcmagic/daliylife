package com.lzc.daliylife.utils;

import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.normalUtil.L;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Created by lzc on 2017/9/12.
 *
 */

public class SignUtil {

    public static String YiYuanSign(String sortString){
        String temp=sortString+ Constants.YIYUANSECRET.toLowerCase(Locale.getDefault());
        try {
        String s = new String(Hex.encodeHex(DigestUtils.md5(temp.getBytes("utf-8"))));
            L.d("s: "+s);
            return s.toUpperCase(Locale.getDefault());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
