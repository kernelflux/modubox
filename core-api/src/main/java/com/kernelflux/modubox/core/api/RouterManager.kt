package com.kernelflux.modubox.core.api

import android.content.Context
import android.content.Intent

/**
 * 路由管理器接口
 * 负责组件间的路由跳转和页面导航
 * 参考美团WMRouter设计，支持拦截器、正则匹配等核心功能
 */
interface RouterManager {
    
    /**
     * 初始化路由管理器
     * @param context 应用上下文
     */
    fun init(context: Context)
    
    /**
     * 注册路由
     * @param path 路由路径
     * @param target 目标类
     */
    fun registerRoute(path: String, target: Class<*>)
    
    /**
     * 注册路由（带配置）
     * @param routeInfo 路由信息
     */
    fun registerRoute(routeInfo: RouteInfo)
    
    /**
     * 注销路由
     * @param path 路由路径
     */
    fun unregisterRoute(path: String)
    
    /**
     * 导航到指定路径
     * @param path 路由路径
     * @param params 参数
     * @return 是否导航成功
     */
    fun navigate(path: String, params: Map<String, Any>? = null): Boolean
    
    /**
     * 构建Intent
     * @param path 路由路径
     * @param params 参数
     * @return Intent实例
     */
    fun buildIntent(path: String, params: Map<String, Any>? = null): Intent?
    
    /**
     * 检查路由是否存在
     * @param path 路由路径
     * @return 是否存在
     */
    fun hasRoute(path: String): Boolean
    
    /**
     * 获取路由信息
     * @param path 路由路径
     * @return 路由信息
     */
    fun getRouteInfo(path: String): RouteInfo?
    
    /**
     * 获取所有路由信息
     * @return 路由信息列表
     */
    fun getAllRoutes(): List<RouteInfo>
    
    /**
     * 根据正则表达式查找路由
     * @param pattern 正则表达式
     * @return 匹配的路由列表
     */
    fun findRoutesByPattern(pattern: String): List<RouteInfo>
    
    /**
     * 添加拦截器
     * @param interceptor 拦截器
     */
    fun addInterceptor(interceptor: Interceptor)
    
    /**
     * 移除拦截器
     * @param interceptor 拦截器
     */
    fun removeInterceptor(interceptor: Interceptor)
    
    /**
     * 获取所有拦截器
     * @return 拦截器列表
     */
    fun getInterceptors(): List<Interceptor>
    
    /**
     * 清除所有拦截器
     */
    fun clearInterceptors()
    
    /**
     * 路由降级处理
     * @param originalPath 原始路径
     * @param fallbackPath 降级路径
     * @param params 参数
     * @return 是否降级成功
     */
    fun fallback(originalPath: String, fallbackPath: String, params: Map<String, Any>? = null): Boolean
    
    /**
     * 设置路由降级处理器
     * @param fallbackHandler 降级处理器
     */
    fun setFallbackHandler(fallbackHandler: FallbackHandler)
    
    /**
     * 拦截器接口
     */
    interface Interceptor {
        /**
         * 拦截路由
         * @param path 路由路径
         * @param params 参数
         * @param chain 拦截器链
         * @return 是否拦截
         */
        suspend fun intercept(path: String, params: Map<String, Any>?, chain: InterceptorChain): Boolean
        
        /**
         * 获取拦截器优先级
         * @return 优先级
         */
        fun getPriority(): Int = 0
        
        /**
         * 获取拦截器名称
         * @return 名称
         */
        fun getName(): String = this.javaClass.simpleName
        
        /**
         * 是否匹配路径
         * @param path 路由路径
         * @return 是否匹配
         */
        fun matches(path: String): Boolean = true
    }
    
    /**
     * 拦截器链接口
     */
    interface InterceptorChain {
        /**
         * 继续执行下一个拦截器
         * @param path 路由路径
         * @param params 参数
         * @return 是否继续
         */
        suspend fun proceed(path: String, params: Map<String, Any>?): Boolean
    }
    
    /**
     * 降级处理器接口
     */
    interface FallbackHandler {
        /**
         * 处理路由降级
         * @param originalPath 原始路径
         * @param fallbackPath 降级路径
         * @param params 参数
         * @return 是否处理成功
         */
        fun handleFallback(originalPath: String, fallbackPath: String, params: Map<String, Any>?): Boolean
    }
}

/**
 * 路由信息
 */
data class RouteInfo(
    val path: String,
    val target: Class<*>,
    val description: String = "",
    val requireLogin: Boolean = false,
    val permissions: List<String> = emptyList(),
    val priority: Int = 0,
    val regex: Boolean = false,
    val fallback: Boolean = false,
    val fallbackPath: String = ""
)
