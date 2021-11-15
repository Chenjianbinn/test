package com.cjianbin.boom.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author cjianbin
 * @Date 2021/11/15 14:36
 * @Version 1.0
 * @Description 引入咖啡因做本地缓存
 */
public class CaffeineCache {

    public static final Logger log = LoggerFactory.getLogger(CaffeineCache.class);

    public static Cache<String, Object> Cache = Caffeine.newBuilder()
            //初始大小
            .initialCapacity(2)
            //最大数量
            .maximumSize(10)
            //过期时间
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build();

    /**
     * 放入缓存
     * @param key 缓存key
     * @param value 缓存值
     */
    public static void put2Cache(String key,Object value){
        Cache.put(key,value);
    }

    /**
     * 获取缓存
     * @param key 缓存key
     */
    public static String get2Cache(String key){
        //若缓存获取不到直接返回null，一般获取不到可以从其他途径取数据并放入缓存中
        Object result = Cache.get(key,k -> null);
        if (result != null) {
            Object expiresIn = Cache.get("expiresIn",k -> null);
            if (expiresIn != null) {
                Date expiresTime = (Date) expiresIn;
                Date now = new Date();
                if (now.compareTo(expiresTime) < 0) {
                    return result.toString();
                }
            }
        }
        //获取不到缓存就将缓存里所有数据清空。
        Cache.invalidateAll();
        return null;
    }
}
