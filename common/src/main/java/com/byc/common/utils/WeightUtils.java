package com.byc.common.utils;


import java.util.List;
import java.util.Random;

/**
 * Created by baiyc
 * 2020/5/28/028 16:31
 * Description：权重工具
 */
public class WeightUtils {

    /**传入权重集合，响应权重对象（测试效率略高）*/
    public static IWeight random(List<? extends IWeight> weights){
        long sum = weights.stream().mapToLong(IWeight::getWeight).sum();
        for (IWeight weight:weights){
            double random = Math.random() * sum;
            Long weightNum = weight.getWeight();
            if(random < weightNum){
                return weight;
            }else {
                sum -= weightNum;
            }
        }
        return null;
    }

    /**权重算法2*/
    public static IWeight random1(List<? extends IWeight> weights){
        int weightSum = (int)weights.stream().mapToLong(IWeight::getWeight).sum();
        IWeight result=null;

        if (weightSum <= 0) {
            return result;
        }
        Random random = new Random();
        long n = random.nextInt(weightSum);
        int m = 0;
        for (IWeight weightConfig : weights) {
            if (m <= n && n < m + weightConfig.getWeight()) {
                result=weightConfig;
                break;
            }
            m += weightConfig.getWeight();
        }
        return result;
    }
}
