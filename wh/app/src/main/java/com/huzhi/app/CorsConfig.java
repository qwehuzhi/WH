package com.huzhi.app;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨域请求过滤器
 */
@Configuration
public class CorsConfig {
    public CorsConfiguration buildConfig(){
        CorsConfiguration corsConfiguration=new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);

        List<String> allowedOriginPatterns = new ArrayList<>();
        // for web dev
        allowedOriginPatterns.add("http://web.console.vvlife.com:8181");
        //for dev

        //for pro
        allowedOriginPatterns.add("https://xcx.vvlife.vip");
        allowedOriginPatterns.add("https://h5.vvlife.vip");
        allowedOriginPatterns.add("https://h5.server.vvlife.vip");

        corsConfiguration.setAllowedOriginPatterns(allowedOriginPatterns);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return corsConfiguration;
    }

    /**
     * 添加映射路径
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
