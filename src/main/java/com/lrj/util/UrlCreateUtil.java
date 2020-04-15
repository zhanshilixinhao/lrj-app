package com.lrj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : cwj
 * @describe : 请求第三方接口时的 请求创建工具
 * @date : 2020-4-5
 */
public class UrlCreateUtil {
    /**
            * java创建Get请求url
     * @param url 创建连接基础url
     * @param mapParams 需要传递的map集合类型参数
     *
             */
    public static String sendGet(String url, Map<String, Object> mapParams) {
        String result = "";// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        StringBuffer sb = new StringBuffer();//map参数格式化成url发送参数格式
        String params = "";//格式化之后url传递的参数
        try {
            // 格式化参数
            if (mapParams.size() == 1) {
                for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(
                            entry.getValue()==null?null:
                                    java.net.URLEncoder.encode(entry.getValue().toString(),
                                            "UTF-8"));  //对参数进行编码格式化以及拼接
                }
                params = sb.toString();
            } else {
                for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(
                            entry.getValue()==null?null:
                                    java.net.URLEncoder.encode(entry.getValue().toString(),
                                            "UTF-8")).append("&");  //对参数进行编码格式化以及拼接
                }
                params = sb.toString().substring(0, sb.toString().length() - 1);
            }

            String contactUrl = url + "?" + params;  //拼接url和参数

            // 创建URL对象
            java.net.URL connURL = new java.net.URL(contactUrl);

            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL
                    .openConnection();

            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");  //设置连接的状态，建立持久化连接，Keep-Alive；close短连接
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  //设置浏览器类型
            httpConn.setRequestProperty("Accept-Charset", "utf-8");  //设置编码语言

            //设置传送的内容是可序列化的java对象，即键值对
            //httpConn.setRequestProperty("Content-type", "application/x-java-serialized-object");//设置请求格式/json/xml/object

            // 设置GET方式相关属性
            // 设定请求的方法为"GET"，默认是GET
            httpConn.setRequestMethod("GET");

            // 设置是否向HttpURLConnection输出，get请求，默认false
            httpConn.setDoOutput(false);

            // 设置是否从httpUrlConnection读入
            httpConn.setDoInput(true);

            // Get请求可以使用缓存
            httpConn.setUseCaches(true);

            //防止网络异常，设置连接主机超时（单位：毫秒）
            httpConn.setConnectTimeout(30000);

            //防止网络异常，设置从主机读取数据超时（单位：毫秒）
            httpConn.setReadTimeout(30000);

            // 设置此 HttpURLConnection是否应该自动执行 HTTP 重定向
            httpConn.setInstanceFollowRedirects(true);

            //设置文件请求的长度
            //httpConn.setRequestProperty("Content-Length", params.getBytes().length + "");

            // 建立实际的连接 ,从上述openConnection至此的配置必须要在connect之前完成
            httpConn.connect();

            /**
             * 获取响应状态码，更多状态码请百度
             * (200)服务器成功返回;
             * (404)请求网页不存在;
             * (503)服务不可用
             */
            //httpConn.getHeaderFields();//获取响应头部
            int code = httpConn.getResponseCode();
            System.out.println("响应状态码："+code);
            if(code==200) {
                // 定义BufferedReader输入流来读取URL的响应,并设置编码方式
                in = new BufferedReader(new InputStreamReader(httpConn
                        .getInputStream(), "UTF-8"));
                String line;
                // 读取返回的内容
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
            //httpConn.disconnect();//关闭连接，释放资源，该方法会彻底关闭长连接，释放网络资源
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();  //关闭流，释放网络资源
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }



    /**
     * java创建Post请求url
     * @param url 创建连接基础url
     * @param mapParams 需要传递的map集合类型参数
     *
     */
    public static String sendPost(String url, Map<String, Object> mapParams) {
        String result = "";// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;  //获取输出流，往缓冲区写入数据
        StringBuffer sb = new StringBuffer();//map参数格式化成url发送参数格式
        String params = "";// 格式化之后url传递的参数
        String appKey = "eafffd68d5a30b76ea8a2ebf";
        String masterKey = "2685296eb76434d50283c476";
        try {
            // 格式化参数
            if (mapParams.size() == 1) {
                for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(
                            entry.getValue()==null?null:
                                    java.net.URLEncoder.encode(entry.getValue().toString(),
                                            "UTF-8"));  //对参数进行编码格式化以及拼接
                }
                params = sb.toString();
            } else {
                for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(
                            entry.getValue()==null?null:
                                    java.net.URLEncoder.encode(entry.getValue().toString(),
                                            "UTF-8")).append("&");  //对参数进行编码格式化以及拼接
                }
                params = sb.toString().substring(0, sb.toString().length() - 1);
            }

            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);

            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL
                    .openConnection();

            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");  //设置连接的状态，建立持久化连接，Keep-Alive；close短连接
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  //设置浏览器属性
            httpConn.setRequestProperty("Accept-Charset", "utf-8");  //设置编码语言
            httpConn.setRequestProperty("Authorization", " Basic " + Base64.getUrlEncoder().encodeToString((appKey + ":" + masterKey).getBytes()));

            //设置传送的内容是可序列化的java对象，即键值对
            //httpConn.setRequestProperty("Content-type", "application/x-java-serialized-object");//设置请求格式/json/xml/object

            // 设置POST方式相关属性
            // 设定请求的方法为"POST"，默认是GET
            httpConn.setRequestMethod("POST");

            //设置是否从HttpURLConnection读入，默认情况下是true;
            httpConn.setDoInput(true);

            //设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
            httpConn.setDoOutput(true);

            // Post请求不能使用缓存,需设置成false
            httpConn.setUseCaches(false);

            //防止网络异常，设置连接主机超时（单位：毫秒）
            httpConn.setConnectTimeout(30000);

            //防止网络异常，设置从主机读取数据超时（单位：毫秒）
            httpConn.setReadTimeout(30000);

            //设置文件请求的长度
            //httpConn.setRequestProperty("Content-Length", params.getBytes().length + "");

            //httpConn.connect();

            // 获取HttpURLConnection对象对应的输出流， 该方法已经隐含调用httpConn.connect()连接方法；
            out = new PrintWriter(httpConn.getOutputStream());

            // post方式发送请求参数
            out.write(params);

            //释放输出流的缓冲，同时关闭输出流对象，不再写入数据 ，释放网络资源
            out.flush();

            /**
             * 获取响应状态码，更多状态码请百度
             * (200)服务器成功返回;
             * (404)请求网页不存在;
             * (503)服务不可用
             */
            int code = httpConn.getResponseCode();
            System.out.println("响应状态码："+code);
            if(code==200) {
                // 定义BufferedReader输入流来读取URL的响应，设置编码方式
                in = new BufferedReader(new InputStreamReader(httpConn
                        .getInputStream(), "UTF-8"));

                String line;
                // 读取返回的内容
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
            //httpConn.disconnect();//关闭连接，释放资源，该方法会彻底关闭长连接，释放网络资源
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();  //关闭输出流，释放网络资源
                }
                if (in != null) {
                    in.close();  //关闭输入流，释放网络资源
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 主函数，测试请求
     *
     * @param args
     */
    public static void main(String[] args) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", "99");
        mapParams.put("name", "sixmonth");
        String result = sendPost("http://127.0.0.1:8080/testUrl.do",
                mapParams);
        System.out.println(result);
    }
}
