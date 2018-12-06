package org.demon.taole.service.jd;

import lombok.extern.apachecommons.CommonsLog;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-06 16:48
 */
@CommonsLog
public class JdQueryListTask implements Runnable {

    private String queryString;

    public JdQueryListTask(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public void run() {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create("https://api.m.jd.com/client.action"))
                .POST(HttpRequest.BodyPublishers.ofString(queryString)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (log.isDebugEnabled()) {
                log.debug(response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
