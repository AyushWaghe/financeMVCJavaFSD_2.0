package org.example.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager=new CaffeineCacheManager(); //Caffeine cache manager implements cache manager. Hence cache manager is like post and caffiene is the employee working under that post
        cacheManager.registerCustomCache(
                "user_profile",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(Duration.ofDays(1))
                        .recordStats()
                        .build()
        );

        cacheManager.registerCustomCache(
                "user-categories",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(Duration.ofDays(1))
                        .recordStats()
                        .build()
        );



       return cacheManager;
    }
}
