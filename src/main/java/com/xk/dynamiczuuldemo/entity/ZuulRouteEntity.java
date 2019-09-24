package com.xk.dynamiczuuldemo.entity;import lombok.Getter;import lombok.Setter;/** * Description: dynamic-zuul-demo * Created by xk on 2019/9/6 0006 10:43 */@Setter@Getterpublic class ZuulRouteEntity {    /**     * route id     */    private String id;    /**     * 需转发路由地址 /github/**     */    private String path;    /**     * 映射路由的服务id     */    private String serviceId;    /**     * 映射路由完整地址http://github.com     */    private String url;    /**     * 前缀剥离     */    private boolean stripPrefix = true;    /**     * 重试     */    private Boolean retryable;    private String apiName;    private Boolean enabled;}