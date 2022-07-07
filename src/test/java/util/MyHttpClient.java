package util;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author zhiqiang.li
 * @date 2022/7/6
 */

@Slf4j
public class MyHttpClient {
    private CloseableHttpClient httpClient = null;
    private CloseableHttpResponse response = null;
    private RequestConfig requestConfig;


    public MyHttpClient(){
        requestConfig = RequestConfig.custom()
                //读取目标服务器数据超时时间
                .setSocketTimeout(10000)
                //连接目标服务器超时时间
                .setConnectTimeout(10000)
                //从连接池获取连接的超时时间
                .setConnectionRequestTimeout(10000)
                .build();

    }

    public String get(String url){
        //创建HttpClient
        httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        try {
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String string = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (statusCode == 200){
                log.info("{} 响应 success res : {}", url, string);
            }
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
