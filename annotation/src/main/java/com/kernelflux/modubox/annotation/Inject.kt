package com.kernelflux.modubox.annotation

/**
 * 注入注解
 * 用于标记需要注入的字段或构造函数
 * 参考Hilt @Inject设计，支持依赖注入
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.SOURCE)
annotation class Inject(
    /**
     * 注入名称
     */
    val name: String = "",
    
    /**
     * 是否必需
     */
    val required: Boolean = true,
    
    /**
     * 作用域
     */
    val scope: String = "Application",
    
    /**
     * 默认值
     */
    val defaultValue: String = "",
    
    /**
     * 条件注入
     */
    val condition: String = "",
    
    /**
     * 是否延迟注入
     */
    val lazy: Boolean = false,
    
    /**
     * 注入优先级
     */
    val priority: Int = 0
)

/**
 * 服务注解
 * 用于标记服务类，自动注册到依赖管理器
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Service(
    /**
     * 服务名称，如果不指定则使用类名
     */
    val name: String = "",
    
    /**
     * 是否为单例
     */
    val singleton: Boolean = true
)
