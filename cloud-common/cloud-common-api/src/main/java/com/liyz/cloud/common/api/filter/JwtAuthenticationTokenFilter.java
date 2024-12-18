package com.liyz.cloud.common.api.filter;

import com.google.common.base.Charsets;
import com.liyz.cloud.common.api.config.AnonymousMappingConfig;
import com.liyz.cloud.common.api.constant.SecurityClientConstant;
import com.liyz.cloud.common.api.context.AuthContext;
import com.liyz.cloud.common.api.user.AuthUserDetails;
import com.liyz.cloud.common.api.util.CookieUtil;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.result.Result;
import com.liyz.cloud.common.util.CryptoUtil;
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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/3/9 14:34
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String AUTH_ID = "AUTH_ID";
    private static final String AES_KEY = "BdbGFURCLfHFgg3qmhaBxG0LG6rYuhST";

    private final String tokenHeaderKey;

    public JwtAuthenticationTokenFilter(String tokenHeaderKey) {
        this.tokenHeaderKey = tokenHeaderKey;
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
            AuthUserBO authUser = null;
            String authInfo = request.getHeader(AUTH_ID);
            if (StringUtils.isNotBlank(authInfo)) {
                authUser = JsonUtil.readValue(CryptoUtil.Symmetric.decryptAES(authInfo, AES_KEY), AuthUserBO.class);
            } else {
                Cookie cookie = CookieUtil.getCookie(this.tokenHeaderKey);
                //UriUtils、URLDecoder、URLEncoder
                String token = Objects.isNull(cookie) ? request.getHeader(this.tokenHeaderKey) : UriUtils.decode(cookie.getValue(), StandardCharsets.UTF_8);
                if (!AnonymousMappingConfig.pathMatch(request.getServletPath()) && StringUtils.isNotBlank(token)) {
                    token = URLDecoder.decode(token, String.valueOf(Charsets.UTF_8));
                    authUser = AuthContext.JwtService.parseToken(token);
                }
                //cookie续期
                if (Objects.nonNull(cookie)) {
                    CookieUtil.addCookie(
                            response,
                            SecurityClientConstant.DEFAULT_TOKEN_HEADER_KEY,
                            token,
                            30 * 60,
                            null
                    );
                }
            }
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
}
