package transto.api.baidu;

import java.util.HashMap;
import java.util.Map;

/**
 * TransApi
 */
public class TransApi {

    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appId;
    private String securityKey;

    public TransApi(String appId, String securityKey) {
        this.appId = appId;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>(16);
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", appId);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名，加密前的原文
        String src = appId + query + salt + securityKey;
        params.put("sign", MD5.md5(src));
        return params;
    }

}
