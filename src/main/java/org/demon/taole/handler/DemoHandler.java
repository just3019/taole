package org.demon.taole.handler;

import cn.hutool.json.JSONUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-04 17:58
 */
@Component
@CommonsLog
public class DemoHandler {

    public Mono<ServerResponse> get(ServerRequest request) {
        log.info(JSONUtil.toJsonPrettyStr(request));
        return null;
    }
}
