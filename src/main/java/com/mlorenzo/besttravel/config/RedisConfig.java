package com.mlorenzo.besttravel.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@EnableCaching
@Configuration
@PropertySource(value = "classpath:redis.properties")
public class RedisConfig {
    public static final String HOTEL_CACHE_NAME = "hotel";
    public static final String FLIGHT_CACHE_NAME = "flight";
    // "0 * * * * *" -> Cada minuto / "0 0 0 * * ?" -> A las 12:00 AM
    private static final String CRON_RESET_CACHE = "0 * * * * *";

    private final Environment env;

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();
        config.useSingleServer()
                .setAddress(env.getProperty("cache.redis.address"))
                .setPassword(env.getProperty("cache.redis.password"));
        return Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager(final RedissonClient redissonClient) {
        final Map<String, CacheConfig> configMap = new HashMap<>();
        return new RedissonSpringCacheManager(redissonClient, configMap);
    }

    // Anotación que, en este caso, elimna todas las entradas de ámbas cachés que están en Redis
    @CacheEvict(cacheNames = {HOTEL_CACHE_NAME, FLIGHT_CACHE_NAME}, allEntries = true)
    // La eliminación de los datos de las cachés de hará de forma automática cada vez que pase el tiempo indicado en
    // el atributo "cron"
    @Scheduled(cron = CRON_RESET_CACHE)
    // Para que esta tarea automática se ejecute en un hilo distinto al hilo principal o hilo "main"
    @Async
    public void deleteCache() {
        log.info("Clean cache");
    }
}
