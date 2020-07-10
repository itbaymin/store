package com.byc.common.utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class RSAUtils {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**Signature方式*/
    public enum SignAlgo{
        NONEwithRSA,
        MD2withRSA,
        MD5withRSA,
        SHA1withRSA,
        SHA224withRSA,
        SHA256withRSA,
        SHA384withRSA,
        SHA512withRSA,
        NONEwithDSA,
        SHA1withDSA,
        SHA224withDSA,
        SHA256withDSA,
        NONEwithECDSA,
        SHA1withECDSA,
        SHA224withECDSA,
        SHA256withECDSA,
        SHA384withECDSA,
        SHA512withECDSA;
    }

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    private static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    private static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    public static String encrypt(String source, String publicKey) throws Exception {
        Key key = getPublicKey(publicKey);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return Base64.encodeBase64String(b1);
    }
    /**
     * 解密算法 cryptograph:密文
     */
    public static String decrypt(String cryptograph, String privateKey) throws Exception {
        Key key = getPrivateKey(privateKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    public static String sign(String content, String privateKey) throws Exception {
        String charset = "UTF-8";
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                Base64.decodeBase64(privateKey.getBytes()));
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyf.generatePrivate(priPKCS8);

        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(priKey);
        signature.update(content.getBytes(charset));
        byte[] signed = signature.sign();
        return new String(Base64.encodeBase64(signed));
    }

    /**
     * RSA加密
     *
     * @param data 待加密数据
     * @param key 公钥/私钥
     * @return
     */
    private static String encrypt(String data, Key key) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            int inputLen = data.getBytes().length;
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
            // 加密后的字符串
            return Base64.encodeBase64String(encryptedData);
        }catch (Exception e) {
            log.error("RSA加密失败",e);
            return "";
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error("关闭ByteArrayOutputStream异常",e);
            }
        }
    }

    /**
     * RSA解密
     *
     * @param data 待解密数据
     * @param key 私钥/公钥
     * @return
     */
    public static String decrypt(String data, Key key) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] dataBytes = Base64.decodeBase64(data);
            int inputLen = dataBytes.length;
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            // 解密后的内容
            return new String(decryptedData, "UTF-8");
        }catch (Exception e){
            log.error("RSA解密失败",e);
            return "";
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error("关闭ByteArrayOutputStream异常",e);
            }
        }
    }

    /**
     * 签名
     * @param data 待签名数据
     * @param privateKey 私钥
     * @param algorithm 签名方式
     * @return 签名
     */
    public static String sign(String data, String privateKey,SignAlgo algorithm) {
        return sign(data.getBytes(),privateKey,algorithm);
    }
    public static String sign(byte[] data, String privateKey,SignAlgo algorithm) {
        try {
            PrivateKey key = getPrivateKey(privateKey);
            Signature signature = Signature.getInstance(algorithm.toString());
            signature.initSign(key);
            signature.update(data);
            return new String(Base64.encodeBase64(signature.sign()));
        }catch (Exception e){
            log.error("RSA签名失败",e);
            return "";
        }
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, String publicKey, SignAlgo algorithm, String sign) {
        return verify(srcData.getBytes(),publicKey,algorithm,sign);
    }
    public static boolean verify(byte[] srcData, String publicKey, SignAlgo algorithm, String sign) {
        try {
            PublicKey key = getPublicKey(publicKey);
            Signature signature = Signature.getInstance(algorithm.toString());
            signature.initVerify(key);
            signature.update(srcData);
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        }catch (Exception e){
            log.error("RSA验签失败",e);
            return false;
        }
    }

    //1.私钥加密
    public static String encryptWithPriKey(String src, String strPrivateKey) {
        try {
            PrivateKey privateKey = getPrivateKey(strPrivateKey);
            return encrypt(src,privateKey);
        }catch (Exception e){
            log.error("生成私钥KEY失败",e);
            return "";
        }
    }

    //1.公钥加密
    public static String encryptWithPubKey(String src, String strPublicKey) {
        try {
            PublicKey publicKey = getPublicKey(strPublicKey);
            return encrypt(src,publicKey);
        }catch (Exception e){
            log.error("生成公钥KEY失败",e);
            return "";
        }
    }

    //2.公钥解密
    public static String decryptWhitPubKey(String data, String strPublicKey) {
        try {
            PublicKey publicKey = getPublicKey(strPublicKey);
            return decrypt(data,publicKey);
        }catch (Exception e){
            log.error("RSA2公钥解密失败",e);
            return "";
        }
    }

    //2.私钥解密
    public static String decryptWithPriKey(String data, String strPrivateKey) {
        try {
            PrivateKey privateKey = getPrivateKey(strPrivateKey);
            return decrypt(data,privateKey);
        }catch (Exception e){
            log.error("RSA2私钥解密失败",e);
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            // 生成密钥对
            KeyPair keyPair = getKeyPair();
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);
            // RSA加密
            String data = "待加密的文字内容";
            String encryptData = encryptWithPubKey(data, publicKey);
            System.out.println("加密后内容:" + encryptData);
            // RSA解密
            String decryptData = decryptWithPriKey(encryptData, privateKey);
            System.out.println("解密后内容:" + decryptData);

            String encrypt = encrypt(data, publicKey);
            String decrypt = decrypt(encrypt, privateKey);
            System.out.println(">>>加密后字符串："+encrypt+";解密后字符串"+decrypt);
            // RSA签名
            String sign = sign(data, privateKey, SignAlgo.MD2withRSA);
            // RSA验签
            boolean result = verify(data, publicKey, SignAlgo.MD2withRSA, sign);
            System.out.print("验签结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("加解密异常");
        }
    }
}