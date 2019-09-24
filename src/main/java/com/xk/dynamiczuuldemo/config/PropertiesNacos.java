package com.xk.dynamiczuuldemo.config;import com.alibaba.fastjson.JSONObject;import com.alibaba.nacos.api.NacosFactory;import com.alibaba.nacos.api.PropertyKeyConst;import com.alibaba.nacos.api.config.ConfigService;import com.alibaba.nacos.api.config.listener.Listener;import com.alibaba.nacos.api.exception.NacosException;import com.xk.dynamiczuuldemo.entity.ZuulRouteEntity;import org.apache.commons.lang.StringUtils;import org.springframework.beans.BeanUtils;import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;import org.springframework.stereotype.Component;import java.util.*;import java.util.concurrent.Executor;/** * Description: nacos远程配置 * Created by xk on 2019/9/6 0006 10:45 */@Componentpublic class PropertiesNacos {    private final static String DATA_ID = "zuul-server";    private final static String GROUP = "zuul_route";    private final static String ADDR = "127.0.0.1:8848";    public Map<String, ZuulProperties.ZuulRoute> getProperties() {        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();        List<ZuulRouteEntity> results = listenerNacos(DATA_ID, GROUP);        for (ZuulRouteEntity result : results) {            if (StringUtils.isBlank(result.getPath())) {                continue;            }            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();            try {                BeanUtils.copyProperties(result, zuulRoute);            } catch (Exception e) {            }            routes.put(zuulRoute.getPath(), zuulRoute);        }        return routes;    }    private List<ZuulRouteEntity> listenerNacos(String dataId, String group) {        try {            Properties properties = new Properties();            properties.put(PropertyKeyConst.SERVER_ADDR, ADDR);            ConfigService configService = NacosFactory.createConfigService(properties);            String content = configService.getConfig(dataId, group, 5000);            System.out.println("从Nacos返回的配置：" + content);            //注册Nacos配置更新监听器            configService.addListener(dataId, group, new Listener() {                @Override                public void receiveConfigInfo(String configInfo) {                    System.out.println("Nacos更新了！");                }                @Override                public Executor getExecutor() {                    return null;                }            });            return JSONObject.parseArray(content, ZuulRouteEntity.class);        } catch (NacosException e) {            e.printStackTrace();        }        return new ArrayList<>();    }}