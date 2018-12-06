package org.demon.config;

import org.demon.advice.RequestBodyAdviceHandler;
import org.demon.advice.ResponseBodyAdviceHandler;
import org.demon.filter.RequestFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * desc: 启动配置
 *
 * @author demon
 * @date 2018-12-05 16:43
 */
@Configuration
@Import({RequestBodyAdviceHandler.class, ResponseBodyAdviceHandler.class})
public class AppAutoConfiguration {

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class ServletFilterConfiguration extends AppAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RequestFilter requestFilter() {
            return new RequestFilter();
        }

    }

}
