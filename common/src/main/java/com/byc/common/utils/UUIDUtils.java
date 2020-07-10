package com.byc.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

public class UUIDUtils {
    @Getter
    @AllArgsConstructor
    public enum StrType{
        NUMBER("0123456789"),
        LOWERCASE("abcdefghijklmnopqrstuvwxyz"),
        UPPERCASE("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        private String content;
    }

    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp;
        temp = str.replaceAll("-", "");
        return temp;
    }

    // 获得指定数量的UUID
    public static String[] uuids(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = uuid();
        }
        return ss;
    }


    public static String sn() {
        String uuid = uuid();
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, 12);
    }

    public static String sn(int num) {
        String uuid = uuid();
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, num);
    }

    public static String random(int len){
        return random(len, StrType.LOWERCASE, StrType.NUMBER, StrType.UPPERCASE);
    }

    public static String random(int len,StrType... types){
        String origin = "";
        for (StrType type:types)
            origin += type.getContent();
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for(int i=0;i<len;i++){
            sb.append(origin.charAt(rand.nextInt(origin.length())));
        }
        return sb.toString();
    }
}
