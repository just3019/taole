package org.demon.taole.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * desc: 测试类
 *
 * @author demon
 * @date 2018-12-04 17:08
 */
@RestController
@CommonsLog
public class DemoWebFluxController {

    /**
     * 返回字符串
     */
    @GetMapping("test_01")
    public Mono<String> test_01() {
        log.info("test_01");
        return Mono.just("test_01");
    }
}
