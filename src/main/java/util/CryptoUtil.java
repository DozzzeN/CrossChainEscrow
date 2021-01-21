package util;

import it.unisa.dia.gas.jpbc.Element;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtil {
    /**
     * 获取哈希值
     *
     * @param mode    哈希模式
     * @param element 要哈希的值
     * @return 哈希过后的值
     */
    public static byte[] getHash(String mode, Element element) {
        byte[] hash_value = null;

        try {
            MessageDigest md = MessageDigest.getInstance(mode);
            hash_value = md.digest(element.toBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash_value;
    }

    /**
     * 获取哈希值
     *
     * @param mode  哈希模式
     * @param bytes 要哈希的值
     * @return 哈希过后的值
     */
    public static byte[] getHash(String mode, byte[] bytes) {
        byte[] hash_value = null;

        try {
            MessageDigest md = MessageDigest.getInstance(mode);
            hash_value = md.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash_value;
    }
}
