package com.kernelflux.modubox.annotation

/**
 * 路由拦截器注解
 * 用于标记路由拦截器，自动注册到路由管理器
 * 参考WMRouter设计，支持大型组件化App
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class RouteInterceptor(
    /**
     * 拦截器名称
     */
    val name: String,
    
    /**
     * 拦截器描述
     */
    val description: String = "",
    
    /**
     * 拦截器优先级，数字越大优先级越高
     */
    val priority: Int = 0,
    
    /**
     * 拦截器分组
     */
    val group: String = "",
    
    /**
     * 是否全局拦截器
     */
    val global: Boolean = false,
    
    /**
     * 拦截的路由路径模式
     */
    val patterns: Array<String> = [],
    
    /**
     * 排除的路由路径模式
     */
    val excludes: Array<String> = [],
    
    /**
     * 是否异步执行
     */
    val async: Boolean = false,
    
    /**
     * 超时时间（毫秒）
     */
    val timeout: Long = 3000,
    
    /**
     * 是否启用
     */
    val enabled: Boolean = true,
    
    /**
     * 拦截器标签
     */
    val tags: Array<String> = [],
    
    /**
     * 是否支持热更新
     */
    val hotReload: Boolean = false
)
