package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestClientConfig {

    @Value("${finance.ai.url}")
    private String financeAIURL;

    @Bean
    public RestClient financeAIRestClientConfig() {

        return RestClient.builder()
                .baseUrl(financeAIURL)
                .requestInterceptor((request, body, execution) -> {

                    ServletRequestAttributes attributes =
                            (ServletRequestAttributes)
                                    RequestContextHolder.getRequestAttributes();

                    if (attributes != null) {

                        String cookie = attributes.getRequest()
                                .getHeader(HttpHeaders.COOKIE);

                        if (cookie != null) {
                            request.getHeaders().set(
                                    HttpHeaders.COOKIE,
                                    cookie
                            );
                        }
                    }

                    return execution.execute(request, body);
                })
                .build();
    }
}
