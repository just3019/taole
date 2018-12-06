package org.demon.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.core.Ordered;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author demon
 * @version 1.0
 * @date 2018/10/17 15:01
 * @since 1.0
 */
@CommonsLog
public class RequestFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 原始请求Host
     */
    private static final String HEADER_ORIGIN_API = "originApi";
    /**
     * 统计UV
     */
    private static final String HEADER_TARGET_ID = "targetID";
    /**
     * Header Name targetIp
     */
    private static final String HEADER_TARGET_IP = "targetIp";
    private static final String IP_UNKNOWN = "unknown";
    /**
     * 需要解析的IP请求头名称
     */
    private static final Set<String> IP_HEADER_NAMES = new HashSet<>();

    static {
        IP_HEADER_NAMES.add("X-REAL-IP");
        IP_HEADER_NAMES.add("REAL-IP");
        IP_HEADER_NAMES.add("X-Forwarded-For");
        IP_HEADER_NAMES.add("Proxy-Client-IP");
        IP_HEADER_NAMES.add("HTTP_CLIENT_IP");
        IP_HEADER_NAMES.add("HTTP_X_FORWARDED_FOR");
        IP_HEADER_NAMES.add("WL-Proxy-Client-IP");
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request 请求
     */
    private static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = null;
        for (String name : IP_HEADER_NAMES) {
            if (!isEmptyIp(ip = request.getHeader(name))) {
                break;
            }
        }

        if (isEmptyIp(ip)) {
            ip = request.getRemoteAddr();
        }

        if (isEmptyIp(ip)) {
            ip = IP_UNKNOWN;
        } else {
            assert ip != null;
            if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (String strIp : ips) {
                    if (!(IP_UNKNOWN.equalsIgnoreCase(strIp))) {
                        ip = strIp;
                        break;
                    }
                }
            }
        }
        return ip;
    }

    private static boolean isEmptyIp(String ip) {
        return StrUtil.isEmpty(ip) || IP_UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * UV
     *
     * @param request 请求
     * @param ip      ip
     * @return 返回标示ID
     */
    private static String getAccessID(HttpServletRequest request, String ip) {
        String accept = request.getHeader("accept");
        String agent = request.getHeader("user-agent");
        String acceptEncoding = request.getHeader("accept-encoding");
        return SecureUtil.md5(accept + agent + acceptEncoding + ip);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long time = System.currentTimeMillis();
        resolverRequest(request);
        setRequestCharset(request);
        setResponseCharset(response);
        filterChain.doFilter(request, response);
        time = System.currentTimeMillis() - time;
        log.info(MessageFormat.format("耗时：({0})", String.valueOf(time)));

    }

    /**
     * 处理request
     *
     * @param request HttpServletRequest
     */
    private void resolverRequest(HttpServletRequest request) {
        Map<String, String> headers = new LinkedCaseInsensitiveMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        String value;
        String key;
        while (headerNames.hasMoreElements()) {
            key = headerNames.nextElement();
            value = request.getHeader(key);
            headers.put(key, value);
        }
        String ip = request.getHeader(HEADER_TARGET_IP);
        if (StrUtil.isEmpty(ip)) {
            ip = getIpAddress(request);
            // 取IP
            headers.put(HEADER_TARGET_IP, ip);
        }

        if (StrUtil.isEmpty(request.getHeader(HEADER_ORIGIN_API))) {
            // 保存原始请求API
            headers.put(HEADER_ORIGIN_API, request.getRequestURI());
        }
        String id = request.getHeader(HEADER_TARGET_ID);
        if (StrUtil.isEmpty(id)) {
            // 取ID
            headers.put(HEADER_TARGET_ID, getAccessID(request, ip));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format("\nAPI : {0}\n", request.getRequestURI()));
        sb.append(MessageFormat.format("METHOD : {0}\n", request.getMethod()));
        sb.append(MessageFormat.format("QUERY : {0}\n", request.getQueryString()));
        sb.append("HEADERS : \n");
        headers.forEach((k, v) -> sb.append(MessageFormat.format("  {0}={1}\n", k, v)));
        log.info(sb.toString());
    }

    /**
     * 设置请求字符集
     */
    private void setRequestCharset(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        httpServletRequest.setCharacterEncoding("UTF-8");
    }

    /**
     * 设置响应字符集
     */
    private void setResponseCharset(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
