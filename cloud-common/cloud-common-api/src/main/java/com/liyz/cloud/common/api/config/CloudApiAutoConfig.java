package com.liyz.cloud.common.api.config;

import com.liyz.cloud.common.api.constant.SecurityClientConstant;
import com.liyz.cloud.common.api.context.AuthContext;
import com.liyz.cloud.common.api.filter.JwtAuthenticationTokenFilter;
import com.liyz.cloud.common.api.handler.JwtAuthenticationEntryPoint;
import com.liyz.cloud.common.api.handler.RestfulAccessDeniedHandler;
import com.liyz.cloud.common.api.properties.GatewayAuthHeaderProperties;
import com.liyz.cloud.common.api.user.UserDetailsServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/5/6 17:33
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(GatewayAuthHeaderProperties.class)
public class CloudApiAutoConfig {

    @Resource
    private GatewayAuthHeaderProperties properties;

    @Bean
    public AuthContext authContext() {
        return new AuthContext();
    }

    @Bean
    public AnonymousMappingConfig anonymousMappingConfig() {
        return new AnonymousMappingConfig();
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @DependsOn({"anonymousMappingConfig"})
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(eh -> eh
                        .accessDeniedHandler(new RestfulAccessDeniedHandler())
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(ahr -> ahr
                        .requestMatchers(HttpMethod.OPTIONS, SecurityClientConstant.OPTIONS_PATTERNS).permitAll()
                        .requestMatchers(HttpMethod.GET, SecurityClientConstant.KNIFE4J_IGNORE_RESOURCES).permitAll()
                        .requestMatchers(HttpMethod.GET, SecurityClientConstant.ACTUATOR_RESOURCES).permitAll()
                        .requestMatchers(AnonymousMappingConfig.getAnonymousMappings()).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationTokenFilter(SecurityClientConstant.DEFAULT_TOKEN_HEADER_KEY, properties),
                        UsernamePasswordAuthenticationFilter.class)
                .headers(hc -> hc.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                //如无需要请勿设置跨域
                .cors(cc -> cc.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(SecurityClientConstant.OPTIONS_PATTERNS, corsConfiguration);
        return source;
    }
}
