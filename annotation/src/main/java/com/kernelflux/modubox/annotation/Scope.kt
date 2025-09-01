package com.kernelflux.modubox.annotation

/**
 * 作用域注解
 * 用于定义依赖注入的作用域
 * 参考Hilt @Scope设计，支持生命周期管理
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Scope(
    /**
     * 作用域名称
     */
    val name: String,
    
    /**
     * 作用域描述
     */
    val description: String = "",
    
    /**
     * 作用域类型
     */
    val type: ScopeType = ScopeType.APPLICATION,
    
    /**
     * 是否单例
     */
    val singleton: Boolean = true,
    
    /**
     * 生命周期
     */
    val lifecycle: String = "Application"
)

/**
 * 作用域类型
 */
enum class ScopeType {
    /**
     * 应用级别作用域
     */
    APPLICATION,
    
    /**
     * Activity级别作用域
     */
    ACTIVITY,
    
    /**
     * Fragment级别作用域
     */
    FRAGMENT,
    
    /**
     * 自定义作用域
     */
    CUSTOM
}
