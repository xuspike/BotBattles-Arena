package com.kob.backend.controller.oss;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kob.backend.controller.oss.config.AliOssConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ali/oss")
public class ApplyController {
    /**
     * 创建OSS客户端
     */
    OSS ossClient = new OSSClientBuilder().build(AliOssConfig.endpoint, AliOssConfig.accessId, AliOssConfig.accessKey);

    /**
     * 前端获取直传的policy信息
     * 前端可以使用响应的参数进行直传到oss存储
     *
     * @return
     */
    @GetMapping("/policy/")
    public Map<String, String> getPolicy(@RequestParam Map<String, String> data) {
        String filename = data.get("filename");
        // 直传有效截止时间
        long expireEndTime = System.currentTimeMillis() + (AliOssConfig.expireTime * 1000);
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConditions = new PolicyConditions();
        // 设置可上传文件的大小
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, AliOssConfig.min, AliOssConfig.max);
        // 设置上传文件的前缀、可忽略
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, AliOssConfig.dir);
        // 生成policy
        String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        JSONObject jasonCallback = new JSONObject();
        jasonCallback.put("callbackUrl", AliOssConfig.callback_url);
        String callbackBody = "filename=" + filename +"&randomname=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}";
        jasonCallback.put("callbackBody",callbackBody);
        jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
        String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        // 封装policy等信息
        respMap.put("callback", base64CallbackBody);
        respMap.put("ossAccessKeyId", AliOssConfig.accessId);
        respMap.put("policy", encodedPolicy);
        respMap.put("signature", postSignature);
        respMap.put("dir", AliOssConfig.dir + formattedDate);
        respMap.put("host", AliOssConfig.host);
        respMap.put("expire", String.valueOf(expireEndTime / 1000));

        return respMap;
    }
}
