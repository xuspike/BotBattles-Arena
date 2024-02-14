package com.kob.backend.controller.oss.config;

public class AliOssConfig {
    /**
     * 填写您的AccessKeyId
     */
    public final static String accessId = "LTAI5tHqaghKxDYmTfrpA8rX";
    /**
     * 填写您的AccessKeySecret
     */
    public final static String accessKey = "FJBTEAtdFtjUewiwuCbIJwOVVdbbL6";
    /**
     * 填写您的 endpoint（地域节点）,就是你OSS的区域节点的域名
     */
    public final static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    /**
     * 填写您的 bucketName ，就是你再OSS创建Bucket的名称
     */
    public final static String bucket = "xrookie";
    /**
     * 直传地址，格式为 bucketName.endpoint (Bucket域名)
     */
    public final static String host = "https://" + bucket +"."+ endpoint;

    /**
     * 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
     *
     */
    public final static String callback_url = "https://pata.xrookie.top:2752/api/ali/oss/receive/";

    /*
     * ============= 配置上传过期时间、大小等等 =========
     */
    /**
     * 上传截止时间（秒）
     */
    public final static long expireTime = 30;

    /**
     * 上传文件最小（字节）
     */
    public final static long min = 0;
    /**
     * 上传文件最大（字节）
     */
    public final static long max = 1048576000;

    /**
     * 上传文件的前缀、可忽略
     */
    public final static String dir = "pata/photo";
}
