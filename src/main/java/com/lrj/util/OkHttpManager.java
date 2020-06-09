package com.lrj.util;

import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author yichenshanren
 * @date 2017/10/30.
 */

public class OkHttpManager {

    /**
     * 从assets中读取 证书
     *
     * @param file 证书名称
     * @return
     * @author yichenshanren
     * @date 2017/10/30.
     */
    private static SSLSocketFactory createSSLSocketFactory(String file) {
        if (StringUtils.isBlank(file)) {
            return null;
        }
        try {
            return createSSLSocketFactory(new FileInputStream(new File(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SSLSocketFactory createSSLSocketFactory(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(
                    null,
                    trustManagerFactory.getTrustManagers(),
                    new SecureRandom()
            );
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OkHttpClient create() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
//                .addInterceptor(interceptor)
                .build();
        return okHttpClient;
    }

    public static OkHttpClient create(String host, String cerPath) {
        final String finalHost = host;
        OkHttpClient okHttpClient = create();
        SSLSocketFactory factory = createSSLSocketFactory(cerPath);
        if (factory == null) {
            factory = createSSLSocketFactory();
        }
        return okHttpClient.newBuilder()
                .sslSocketFactory(factory)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        if (StringUtils.isBlank(finalHost)) return true;
                        return hostname.equals(finalHost);
                    }
                }).build();
    }

    /**
     * 创建全部信任的工厂
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
