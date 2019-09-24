package com.xk.dynamiczuuldemo.config;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;import org.springframework.util.StringUtils;import java.util.LinkedHashMap;import java.util.Map;/** * Description: 获取nacos配置 * Created by xk on 2019/9/6 0006 10:48 */public class NewZuulRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {    @Autowired    private ZuulProperties properties;    @Autowired    private PropertiesNacos propertiesAssemble;    public NewZuulRouteLocator(String servletPath, ZuulProperties properties) {        super(servletPath, properties);        this.properties = properties;    }    @Override    public void refresh() {        doRefresh();    }    @Override    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>(16);        // 默认配置加载        routesMap.putAll(super.locateRoutes());        routesMap.putAll(propertiesAssemble.getProperties());        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {            String path = entry.getKey();            if (!path.startsWith("/")) {                path = "/" + path;            }            if (StringUtils.hasText(this.properties.getPrefix())) {                path = this.properties.getPrefix() + path;                if (!path.startsWith("/")) {                    path = "/" + path;                }            }            values.put(path, entry.getValue());        }        return values;    }}