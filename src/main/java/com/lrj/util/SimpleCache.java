package com.lrj.util;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * <b>项目名称</b>：lanrenxiyi<br>
 * <b>类名称</b>：SimpleCache<br>
 * <b>类描述</b>：简单的缓存类.<br>
 * <b>创建人</b>：SAM QZL<br>
 * <b>创建时间</b>：2017-7-25 下午3:04:01<br>
 * <b>修改人</b>：SAM QZL<br>
 * <b>修改时间</b>：2017-7-25 下午3:04:01<br>
 * <b>修改备注</b>：<br>
 * @author SAM QZL<br>
 * @version
 * 
 */
public final class SimpleCache<K, V> {

    private final Lock lock = new ReentrantLock();
    private final int maxCapacity;
    private final Map<K, V> eden;
    private final Map<K, V> perm;

    /** 缓存类型 **/
    public enum CacheType {
        /** 等级实体列表 **/
        LEVEL_LIST(1, "等级实体列表"),
        /** 增值服务换成 **/
        VALUES_ADD_SERVICES(2, "增值服务列表");

        private CacheType(int statusCode, String statusName) {

            this.statusCode = statusCode;
            this.statusName = statusName;
        }

        private final int statusCode;
        private final String statusName;

        public int getStatusCode() {

            return statusCode;
        }

        public String getStatusName() {

            return statusName;
        }
    }

    /** 缓存单例 **/
    private static final SimpleCache<CacheType, Object> simpleCache = new SimpleCache<CacheType, Object>(10);

    /**
     * @功能说明:返回缓存对象！
     * @return
     * @返回类型:SimpleCache
     * @方法名称:getInstance
     * @类名称:SimpleCache
     * @文件名称:SimpleCache.java
     * @所属包名:com.lanrenxiyi.cache
     * @项目名称:lanrenxiyi
     * @创建时间:2017-7-25 下午3:30:43
     * @作者:SAM QZL
     * @版本:1.0
     */
    public static SimpleCache<CacheType, Object> getInstance() {

        return simpleCache;
    }

    private SimpleCache(int maxCapacity) {

        this.maxCapacity = maxCapacity;
        this.eden = new ConcurrentHashMap<K, V>(maxCapacity);
        this.perm = new WeakHashMap<K, V>(maxCapacity);
    }

    /**
     * @功能说明:根据key键获取值。
     * @param k
     *            键
     * @return 值
     * @返回类型:V
     * @方法名称:get
     * @类名称:SimpleCache
     * @文件名称:SimpleCache.java
     * @所属包名:com.lanrenxiyi.cache
     * @项目名称:lanrenxiyi
     * @创建时间:2017-7-25 下午3:13:44
     * @作者:SAM QZL
     * @版本:1.0
     */
    public V get(K k) {

        /** 增值服务暂时不缓存 **/
        if (k == CacheType.VALUES_ADD_SERVICES) {
            return null;
        }
        V v = this.eden.get(k);
        if (v == null) {
            lock.lock();
            try {
                v = this.perm.get(k);
            }
            finally {
                lock.unlock();
            }
            if (v != null) {
                this.eden.put(k, v);
            }
        }
        return v;
    }

    /**
     * @功能说明:存入一个值.
     * @param k
     *            值
     * @param v
     *            键
     * @返回类型:void
     * @方法名称:put
     * @类名称:SimpleCache
     * @文件名称:SimpleCache.java
     * @所属包名:com.lanrenxiyi.cache
     * @项目名称:lanrenxiyi
     * @创建时间:2017-7-25 下午3:14:26
     * @作者:SAM QZL
     * @版本:1.0
     */
    public void put(K k, V v) {

        if (this.eden.size() >= maxCapacity) {
            lock.lock();
            try {
                this.perm.putAll(this.eden);
            }
            finally {
                lock.unlock();
            }
            this.eden.clear();
        }
        this.eden.put(k, v);
    }

    @Override
    public String toString() {

        return "SimpleCache [lock=" + lock + ", maxCapacity=" + maxCapacity + ", eden=" + eden
                + ", perm=" + perm + "]";
    }

    public static void main(String args[]) {

        SimpleCache.getInstance().put(CacheType.LEVEL_LIST, "1,1,1,1,1,1");
        SimpleCache.getInstance().put(CacheType.LEVEL_LIST, "1,1asdasd,1,1,1,1");
        System.out.println(SimpleCache.getInstance().toString());
    }
}
