package com.kernelflux.modubox.core.api

/**
 * 依赖管理器接口
 * 负责管理依赖注入，参考Hilt设计
 */
interface DependencyManager {
    
    /**
     * 注册插件
     */
    fun registerPlugin(plugin: Plugin)
    
    /**
     * 注册服务
     */
    fun registerService(serviceName: String, service: Any)
    
    /**
     * 注册模块
     */
    fun registerModule(module: Any)
    
    /**
     * 注册组件
     */
    fun registerComponent(component: Any)
    
    /**
     * 获取服务
     */
    fun <T> getService(serviceName: String): T?
    
    /**
     * 获取服务（按类型）
     */
    fun <T> getService(type: Class<T>): T?
    
    /**
     * 获取模块
     */
    fun <T> getModule(type: Class<T>): T?
    
    /**
     * 获取组件
     */
    fun <T> getComponent(type: Class<T>): T?
    
    /**
     * 注入依赖
     */
    fun inject(target: Any)
    
    /**
     * 检查是否有服务
     */
    fun hasService(serviceName: String): Boolean
    
    /**
     * 检查是否有服务（按类型）
     */
    fun hasService(type: Class<*>): Boolean
    
    /**
     * 清除所有依赖
     */
    fun clear()
    
    /**
     * 获取所有服务名称
     */
    fun getAllServiceNames(): Set<String>
    
    /**
     * 获取所有模块类型
     */
    fun getAllModuleTypes(): Set<Class<*>>
    
    /**
     * 获取所有组件类型
     */
    fun getAllComponentTypes(): Set<Class<*>>
}

/**
 * 服务提供者接口
 */
interface ServiceProvider<T> {
    /**
     * 创建服务实例
     * @return 服务实例
     */
    fun create(): T
}

/**
 * 单例服务提供者
 */
class SingletonServiceProvider<T>(
    private val creator: () -> T
) : ServiceProvider<T> {
    private var instance: T? = null
    
    override fun create(): T {
        if (instance == null) {
            instance = creator()
        }
        return instance!!
    }
}

/**
 * 工厂服务提供者
 */
class FactoryServiceProvider<T>(
    private val creator: () -> T
) : ServiceProvider<T> {
    override fun create(): T = creator()
}
