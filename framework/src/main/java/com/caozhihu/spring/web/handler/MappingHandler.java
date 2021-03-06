package com.caozhihu.spring.web.handler;

import com.caozhihu.spring.bean.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author:czwbig
 * @date:2019/7/6 18:15
 * @description: 映射处理器
 */
public class MappingHandler {
    // 请求 uri , 注意不是 url
    // url 如 http://localhost:8080/getSalary.json
    // uri 如 /getSalary.json
    private String uri;
    // 调用类
    private Class<?> controller;
    // 调用方法
    private Method method;
    // 调用方法的参数名(key)
    private String[] args;

    MappingHandler(String uri, Class<?> controller, Method method, String[] args) {
        this.uri = uri;
        this.controller = controller;
        this.method = method;
        this.args = args;
    }

    public boolean handle(ServletRequest req, ServletResponse res) throws
            IllegalAccessException, InvocationTargetException, IOException {
        String servletUri = ((HttpServletRequest) req).getRequestURI();
        if (!uri.equals(servletUri)) {
            return false;
        }
        // 如果本 MappingHandler 对应请求 uri 的 uri，
        // 先根据方法参数名，提取 request 中的参数值
        Object[] parameters = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            parameters[i] = req.getParameter(args[i]);
        }

        // 获取对应 Controller 类实例以调用方法
        Object ctroller = BeanFactory.getBean(controller);
        Object response = method.invoke(ctroller, parameters);
        res.getWriter().println(response.toString());

        return true;


    }
}
