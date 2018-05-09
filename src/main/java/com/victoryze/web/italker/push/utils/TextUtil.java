package com.victoryze.web.italker.push.utils;



import com.victoryze.web.italker.push.provider.GsonProvider;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by dsz on 2018/2/24.
 */
public class TextUtil {

    /**
     * 计算一个字符串的MD5信息
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String endcodeBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }


    public static String toJson(Object obj) {
        return GsonProvider.getGson().toJson(obj);
    }
}
