package com.kernelflux.modubox.core.api

/**
 * ModuBox配置类
 * 用于配置框架的各种参数
 */
data class ModuBoxConfig(
    /**
     * 是否启用调试模式
     */
    val debug: Boolean = false,
    
    /**
     * 是否启用自动注册
     */
    val autoRegister: Boolean = true,
    
    /**
     * 是否启用路由拦截器
     */
    val enableRouteInterceptor: Boolean = true,
    
    /**
     * 是否启用依赖注入
     */
    val enableDependencyInjection: Boolean = true,
    
    /**
     * 插件加载超时时间（毫秒）
     */
    val pluginLoadTimeout: Long = 5000,
    
    /**
     * 路由跳转超时时间（毫秒）
     */
    val routeTimeout: Long = 3000,
    
    /**
     * 最大插件数量
     */
    val maxPlugins: Int = 100,
    
    /**
     * 是否启用插件热更新
     */
    val enableHotUpdate: Boolean = false,
    
    /**
     * 插件安全验证
     */
    val enableSecurityCheck: Boolean = true,
    
    /**
     * 日志级别
     */
    val logLevel: LogLevel = LogLevel.INFO
) {
    companion object {
        /**
         * 默认配置
         */
        fun default(): ModuBoxConfig {
            return ModuBoxConfig()
        }
        
        /**
         * 调试配置
         */
        fun debug(): ModuBoxConfig {
            return ModuBoxConfig(
                debug = true,
                logLevel = LogLevel.DEBUG
            )
        }
        
        /**
         * 生产配置
         */
        fun     production(): ModuBoxConfig {
            return ModuBoxConfig(
                debug = false,
                enableHotUpdate = false,
                enableSecurityCheck = true,
                logLevel = LogLevel.WARN
            )
        }
    }
}

/**
 * 日志级别枚举
 */
enum class LogLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR
}
