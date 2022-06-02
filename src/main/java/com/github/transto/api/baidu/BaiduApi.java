package com.github.transto.api.baidu;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * BaiduApi
 * 文档：http://api.fanyi.baidu.com/doc/21
 */
public class BaiduApi {

    /**
     * 翻译
     * @param q
     * @return
     */
    public static Object translate(String q){
        // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
        final String APP_ID = "20200418000423327";
        final String SECURITY_KEY = "w86l2KSP7GJbtB0rokYv";
        // 1 QPS
        TransApi transApi = new TransApi(APP_ID, SECURITY_KEY);
        String transResult = transApi.getTransResult(q, "auto", "zh");
        Object dst = "";
        if (null != transResult){
            JSONObject jsonObject = JSONUtil.parseObj(transResult);
            Object transResultObj = jsonObject.get("trans_result");
            if (null != transResultObj){
                dst = JSONUtil.parseObj(JSONUtil.parseArray(transResultObj.toString()).get(0)).get("dst");
            }else{
                dst = jsonObject.get("error_code").toString() + ", " + jsonObject.get("error_msg");
            }
        }
        return dst;
    }
}
