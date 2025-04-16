package com.liyz.cloud.common.api.filter;

import com.google.common.base.Charsets;
import com.liyz.cloud.common.api.config.AnonymousMappingConfig;
import com.liyz.cloud.common.api.constant.SecurityClientConstant;
import com.liyz.cloud.common.api.context.AuthContext;
import com.liyz.cloud.common.api.properties.GatewayAuthHeaderProperties;
import com.liyz.cloud.common.api.user.AuthUserDetails;
import com.liyz.cloud.common.api.util.CookieUtil;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.result.Result;
import com.liyz.cloud.common.util.CryptoUtil;
import com.liyz.cloud.common.util.DateUtil;
import com.liyz.cloud.common.util.JsonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Desc:jwt认证过滤器
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/3/9 14:34
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final String tokenHeaderKey;
    private final GatewayAuthHeaderProperties properties;

    public JwtAuthenticationTokenFilter(String tokenHeaderKey, GatewayAuthHeaderProperties properties) {
        this.tokenHeaderKey = tokenHeaderKey;
        this.properties = properties;
    }

    /**
     * 这里优先解析gateway预约的header，其次是cookie，最后再是请求携带的header token
     * 注：AES_KEY可以放入cloud-service-auth
     *
     * @param request request
     * @param response response
     * @param filterChain filterChain
     * @throws ServletException servletException
     * @throws IOException iOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            AuthUserBO authUser = this.getAuthUser(request, response);
            if (Objects.nonNull(authUser)) {
                AuthUserDetails authUserDetails = AuthUserDetails.build(authUser);
                UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                        authUserDetails,
                        null,
                        authUserDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                AuthContext.setAuthUser(authUser);
            }
            //处理下一个过滤器
            filterChain.doFilter(request, response);
        } catch (RemoteServiceException exception) {
            response.setCharacterEncoding(Charsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(JsonUtil.toJSONString(Result.error(exception.getCode(), exception.getMessage())));
            response.getWriter().flush();
        } finally {
            AuthContext.remove();
            SecurityContextHolder.getContextHolderStrategy().clearContext();
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * 获取认证信息
     * 优先级：gateway header -> cookie -> Authorization header
     *
     * @param request http请求
     * @param response http返回
     * @return 认证信息
     */
    private AuthUserBO getAuthUser(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        if (AnonymousMappingConfig.pathMatch(request.getServletPath())) {
            return null;
        }
        String authInfo = request.getHeader(properties.getKey());
        if (StringUtils.isNotBlank(authInfo)) {
            return JsonUtil.readValue(CryptoUtil.Symmetric.decryptAES(authInfo, properties.getSecret()), AuthUserBO.class);
        }
        String token;
        Cookie cookie = CookieUtil.getCookie(this.tokenHeaderKey);
        if (Objects.nonNull(cookie)) {
            token = UriUtils.decode(cookie.getValue(), StandardCharsets.UTF_8);
        } else {
            token = request.getHeader(this.tokenHeaderKey);
            if (StringUtils.isNotBlank(token)) {
                token = URLDecoder.decode(token, String.valueOf(Charsets.UTF_8));
            }
        }
        //cookie续期
        if (Objects.nonNull(cookie)) {
            Cookie extraCookie = CookieUtil.getCookie(SecurityClientConstant.DEFAULT_TOKEN_HEADER_KEY + CookieUtil.COOKIE_START_SUFFIX);
            if (Objects.isNull(extraCookie) || (CookieUtil.getMaxAge()/2* 1000L) <= (DateUtil.current() - Long.parseLong(extraCookie.getValue()))) {
                CookieUtil.addCookie(response, SecurityClientConstant.DEFAULT_TOKEN_HEADER_KEY, token);
            }
        }
        return StringUtils.isBlank(token) ? null : AuthContext.JwtService.parseToken(token);
    }
}
