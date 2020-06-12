package com.lrj.util;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * BaseCache.java Create on 2017年2月21日 下午9:42:55
 * <p>
 * 类功能说明: GUAVA缓存
 * <p>
 * Copyright: Copyright(c) 2013
 * Company: COSHAHO
 *
 * @Version 1.0
 * @Author coshaho
 */
public abstract class BaseCache<K, V> {

    private Map<K,V> cache;

    /**
     * 超时缓存：数据写入缓存超过一定时间自动刷新
     * @param duration
     * @param timeUtil
     */
    public BaseCache(long duration, TimeUnit timeUtil)
    {
        cache = new HashMap<>();
    }


    /**
     *
     * 缓存数据加载方法
     * @author coshaho
     * @param k
     * @return
     */
    protected abstract V loadData(K k);

    /**
     *
     * 从缓存获取数据
     * @author coshaho
     * @param param
     * @return
     */
    public V getCache(K param)
    {
        return cache.get(param);
    }

    /**
     *
     * 主动设置缓存数据
     * @author coshaho
     * @param k
     * @param v
     */
    public void put(K k, V v)
    {
        cache.put(k, v);
    }
}