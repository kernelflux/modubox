package com.kernelflux.modubox.annotation

/**
 * 提供者注解
 * 用于标记提供依赖的方法
 * 参考Hilt @Provides设计，支持依赖注入
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Provides(
    /**
     * 提供者名称
     */
    val name: String = "",
    
    /**
     * 是否单例
     */
    val singleton: Boolean = true,
    
    /**
     * 作用域
     */
    val scope: String = "Application",
    
    /**
     * 优先级，数字越大优先级越高
     */
    val priority: Int = 0,
    
    /**
     * 是否延迟初始化
     */
    val lazy: Boolean = false,
    
    /**
     * 条件注入
     */
    val condition: String = "",
    
    /**
     * 是否支持热更新
     */
    val hotReload: Boolean = false
)
