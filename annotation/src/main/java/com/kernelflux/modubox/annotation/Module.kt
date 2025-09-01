package com.kernelflux.modubox.annotation

/**
 * 模块注解
 * 用于标记模块类，提供依赖注入的容器
 * 参考Hilt @Module设计，支持组件化架构
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Module(
    /**
     * 模块名称
     */
    val name: String = "",
    
    /**
     * 模块描述
     */
    val description: String = "",
    
    /**
     * 模块优先级，数字越大优先级越高
     */
    val priority: Int = 0,
    
    /**
     * 是否单例模块
     */
    val singleton: Boolean = true,
    
    /**
     * 模块依赖的其他模块
     */
    val dependencies: Array<String> = [],
    
    /**
     * 模块作用域
     */
    val scope: String = "Application",
    
    /**
     * 是否延迟初始化
     */
    val lazy: Boolean = false,
    
    /**
     * 是否支持热更新
     */
    val hotReload: Boolean = false
)
