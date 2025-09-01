package com.kernelflux.modubox.annotation

/**
 * 路由注解
 * 用于标记Activity或Fragment，自动注册到路由管理器
 * 参考ARouter设计，支持大型组件化App
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Route(
    /**
     * 路由路径
     * 支持正则表达式匹配
     */
    val path: String,
    
    /**
     * 路由组名
     */
    val group: String = "",
    
    /**
     * 路由描述
     */
    val description: String = "",
    
    /**
     * 是否需要登录
     */
    val requireLogin: Boolean = false,
    
    /**
     * 权限要求
     */
    val permissions: Array<String> = [],
    
    /**
     * 路由优先级，数字越大优先级越高
     */
    val priority: Int = 0,
    
    /**
     * 是否支持正则匹配
     */
    val regex: Boolean = false,
    
    /**
     * 是否支持降级处理
     */
    val fallback: Boolean = false,
    
    /**
     * 降级路由路径
     */
    val fallbackPath: String = "",
    
    /**
     * 路由参数
     */
    val extras: Array<String> = [],
    
    /**
     * 是否支持WebView
     */
    val webView: Boolean = false,
    
    /**
     * 是否支持外部应用跳转
     */
    val external: Boolean = false,
    
    /**
     * 外部应用包名
     */
    val externalPackage: String = "",
    
    /**
     * 是否支持深度链接
     */
    val deepLink: Boolean = false,
    
    /**
     * 深度链接Scheme
     */
    val scheme: String = "",
    
    /**
     * 是否支持多进程
     */
    val multiProcess: Boolean = false,
    
     /**
     * 进程名称
     */
    val process: String = "",
    
    /**
     * 是否支持A/B测试
     */
    val abTest: Boolean = false,
    
    /**
     * A/B测试配置
     */
    val abTestConfig: String = "",
    
    /**
     * 路由标签
     */
    val tags: Array<String> = [],
    
    /**
     * 是否启用
     */
    val enabled: Boolean = true
)
