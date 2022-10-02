package util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 实现MD5的加密解密
 */
public class MD5Util {
    //实现MD5加密,返回加密结果
    public static String MD5Encrypt(String password){
        byte[] secretBytes=null;
        try {
            secretBytes=MessageDigest.getInstance("md5").digest(password.getBytes());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个MD5算法");
        }
        String md5Code=new BigInteger(1,secretBytes).toString(16);
        for(int i=0;i<32-md5Code.length();i++){
            md5Code="0"+md5Code;
        }
        return md5Code;
    }
}
