package com.liyz.cloud.common.api.util;

import com.liyz.cloud.common.api.constant.SecurityClientConstant;
import com.liyz.cloud.common.util.DateUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Desc:cookie工具类
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/3/21 15:47
 */
@Component
public class CookieUtil implements ApplicationContextAware, InitializingBean {

    public static final String COOKIE_START_SUFFIX = "-S";

    private static ApplicationContext applicationContext;
    private static org.springframework.boot.web.server.Cookie serverCookie;

    /**
     * 获得指定cookie中的值
     *
     * @param request http request
     * @param cookieName cookie
     * @return 值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = getCookie(request, cookieName);
        return Objects.isNull(cookie) ? null : cookie.getValue();
    }

    /**
     * 获得指定cookie中的值
     *
     * @param cookieName cookie
     * @return 值
     */
    public static String getCookieValue(String cookieName) {
        return getCookieValue(HttpServletContext.getRequest(), cookieName);
    }

    /**
     * 获得指定cookie中的值
     *
     * @param request http request
     * @param cookieName cookie
     * @return 值
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * 获得指定cookie中的值
     *
     * @param cookieName cookie
     * @return 值
     */
    public static Cookie getCookie(String cookieName) {
        return getCookie(HttpServletContext.getRequest(), cookieName);
    }

    /**
     * 添加一个cookie
     *
     * @param response http response
     * @param cookieName name
     * @param value value
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String value) {
        addCookie(response, cookieName, value, getMaxAge());
    }

    /**
     * 添加一个cookie
     *
     * @param cookieName cookie
     * @param value 值
     */
    public static void addCookie(String cookieName, String value) {
        addCookie(HttpServletContext.getResponse(), cookieName, value, getMaxAge());
    }

    /**
     * 添加一个cookie
     *
     * @param response http response
     * @param cookie cookie
     */
    public static void addCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    /**
     * 添加一个cookie
     *
     * @param cookie cookie
     */
    public static void addCookie(Cookie cookie) {
        addCookie(HttpServletContext.getResponse(), cookie);
    }

    /**
     * 添加一个cookie
     *
     * @param response http response
     * @param cookieName cookie
     * @param value 值
     * @param expiry 过期时间
     */
    @SneakyThrows
    public static void addCookie(HttpServletResponse response, String cookieName, String value, int expiry) {
        Cookie cookie = new Cookie(cookieName, UriUtils.encode(value, StandardCharsets.UTF_8));
        cookie.setMaxAge(expiry);
        cookie.setSecure(Objects.nonNull(serverCookie.getSecure()) ? serverCookie.getSecure() : false);
        cookie.setPath(getPath());
        cookie.setDomain(serverCookie.getDomain());
        cookie.setHttpOnly(Objects.nonNull(serverCookie.getHttpOnly()) ? serverCookie.getHttpOnly() : true);
        response.addCookie(cookie);
        //设置副属性
        Cookie cookieSta = new Cookie(cookieName + COOKIE_START_SUFFIX, String.valueOf(DateUtil.current()));
        cookieSta.setMaxAge(expiry);
        cookieSta.setSecure(Objects.nonNull(serverCookie.getSecure()) ? serverCookie.getSecure() : false);
        cookieSta.setPath(getPath());
        cookieSta.setDomain(serverCookie.getDomain());
        cookieSta.setHttpOnly(Objects.nonNull(serverCookie.getHttpOnly()) ? serverCookie.getHttpOnly() : true);
        response.addCookie(cookieSta);
    }

    /**
     * 移除cookie
     *
     * @param response http response
     * @param cookieName cookie
     */
    public static void removeCookie(HttpServletResponse response, String cookieName) {
        addCookie(response, cookieName, StringUtils.EMPTY, 0);
    }

    /**
     * 获取server设置的path
     *
     * @return path
     */
    private static String getPath() {
        if (serverCookie == null || StringUtils.isEmpty(serverCookie.getPath())) {
            return "/";
        }
        return serverCookie.getPath();
    }

    /**
     * 获取server设置的过期时间
     *
     * @return maxAge
     */
    public static int getMaxAge() {
        if (serverCookie == null || Objects.isNull(serverCookie.getMaxAge())) {
            return SecurityClientConstant.DEFAULT_SESSION_TIMEOUT;
        }
        return Long.valueOf(serverCookie.getMaxAge().getSeconds()).intValue();
    }

    /**
     * 移除cookie
     *
     * @param cookieName cookie
     */
    public static void removeCookie(String cookieName) {
        removeCookie(HttpServletContext.getResponse(), cookieName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        serverCookie = applicationContext.getBean(ServerProperties.class).getServlet().getSession().getCookie();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CookieUtil.applicationContext = applicationContext;
    }
}
