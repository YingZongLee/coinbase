package com.cathaybk.coinbase.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.cathaybk.coinbase.utils.OkHttpUtils.SetOkHttpClient;

@Slf4j
@Component
@ConfigurationProperties("okhttp")
public class OkHttpConfig {
    @Value("${okhttp.connect-timeout-ms}")
    private Long connectTimeoutMS;
    @Value("${okhttp.read-timeout-ms}")
    private Long readTimeoutMS;
    @Value("${okhttp.write-timeout-ms}")
    private Long writeTimeoutMS;
    @Value("${okhttp.max-idle}")
    private Integer maxIdle;
    @Value("${okhttp.keep-alive-duration-sec}")
    private Long keepAliveDurationSEC;

    @Bean
    public OkHttpClient okHttpClient() {
        log.debug("connectTimeoutMS;{};readTimeoutMS;{};writeTimeoutMS;{};maxIdle;{};keepAliveDurationSEC;{}",
            connectTimeoutMS,
            readTimeoutMS,
            writeTimeoutMS,
            maxIdle,
            keepAliveDurationSEC);
        OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(Boolean.FALSE)
            .connectionPool(new ConnectionPool(maxIdle, keepAliveDurationSEC, TimeUnit.SECONDS))
            .connectTimeout(connectTimeoutMS, TimeUnit.MILLISECONDS)
            .readTimeout(readTimeoutMS, TimeUnit.MILLISECONDS)
            .writeTimeout(writeTimeoutMS,TimeUnit.MILLISECONDS)
            .build();
        SetOkHttpClient(client);
        return client;
    }
}
