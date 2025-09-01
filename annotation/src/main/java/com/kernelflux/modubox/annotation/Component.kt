package com.kernelflux.modubox.annotation

/**
 * 组件注解
 * 用于标记依赖注入组件
 * 参考Hilt @Component设计，支持组件化架构
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Component(
    /**
     * 组件名称
     */
    val name: String = "",
    
    /**
     * 组件描述
     */
    val description: String = "",
    
    /**
     * 组件作用域
     */
    val scope: String = "Application",
    
    /**
     * 依赖的模块
     */
    val modules: Array<String> = [],
    
    /**
     * 依赖的组件
     */
    val dependencies: Array<String> = [],
    
    /**
     * 是否单例组件
     */
    val singleton: Boolean = true,
    
    /**
     * 是否延迟初始化
     */
    val lazy: Boolean = false,
    
    /**
     * 是否支持热更新
     */
    val hotReload: Boolean = false,
    
    /**
     * 组件优先级
     */
    val priority: Int = 0
)
