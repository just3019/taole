package org.demon.taole.http;

import cn.hutool.json.JSONUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.taole.pojo.Demo;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-07 14:40
 */
@CommonsLog
public class HttpTest {


    /**
     * get请求
     */
    @Test
    public void test_01() throws ExecutionException, InterruptedException {
        Demo bean = new Demo();
        bean.setName("name");
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/demo"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JSONUtil.toJsonStr(bean)))
                .build();
        CompletableFuture result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        log.info(result.get());
    }


}
