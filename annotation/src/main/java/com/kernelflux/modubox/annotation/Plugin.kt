package com.kernelflux.modubox.annotation

/**
 * 插件注解
 * 用于标记插件类，自动注册到插件管理器
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Plugin(
    /**
     * 插件ID，必须唯一
     */
    val id: String,
    
    /**
     * 插件名称
     */
    val name: String = "",
    
    /**
     * 插件版本
     */
    val version: String = "1.0.0",
    
    /**
     * 插件描述
     */
    val description: String = "",
    
    /**
     * 是否自动启动
     */
    val autoStart: Boolean = false,
    
    /**
     * 依赖的插件ID列表
     */
    val dependencies: Array<String> = []
)
