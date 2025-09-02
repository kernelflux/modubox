package com.kernelflux.modubox.core.impl

import android.content.Context
import android.content.Intent
import android.util.Log
import com.kernelflux.modubox.core.api.*
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

/**
 * 高级路由管理器实现类
 * 参考美团WMRouter设计，支持拦截器、正则匹配等核心功能
 */
class AdvancedRouterManagerImpl : RouterManager {

    private lateinit var context: Context
    private val routes = ConcurrentHashMap<String, RouteInfo>()
    private val routePatterns = ConcurrentHashMap<Pattern, RouteInfo>()
    private val interceptors = mutableListOf<RouterManager.Interceptor>()
    private var fallbackHandler: RouterManager.FallbackHandler? = null

    companion object {
        private const val TAG = "AdvancedRouterManager"
    }

    override fun init(context: Context) {
        this.context = context.applicationContext
        Log.d(TAG, "AdvancedRouterManager initialized")
    }

    override fun registerRoute(path: String, target: Class<*>) {
        val routeInfo = RouteInfo(
            path = path,
            target = target
        )
        registerRoute(routeInfo)
    }

    override fun registerRoute(routeInfo: RouteInfo) {
        routes[routeInfo.path] = routeInfo

        // 如果支持正则匹配，添加到模式映射
        if (routeInfo.regex) {
            try {
                val pattern = Pattern.compile(routeInfo.path)
                routePatterns[pattern] = routeInfo
            } catch (e: Exception) {
                Log.e(TAG, "Invalid regex pattern: ${routeInfo.path}", e)
            }
        }

        Log.d(TAG, "Route registered: ${routeInfo.path} -> ${routeInfo.target.simpleName}")
    }

    override fun unregisterRoute(path: String) {
        routes.remove(path)
        val iterator = routePatterns.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.path == path) {
                iterator.remove()
            }
        }
        Log.d(TAG, "Route unregistered: $path")
    }

    override fun navigate(path: String, params: Map<String, Any>?): Boolean {
        return try {
            // 执行拦截器链
            val chain = InterceptorChainImpl(0, interceptors, this)
            val shouldProceed = runBlocking { chain.proceed(path, params) }

            if (!shouldProceed) {
                Log.d(TAG, "Route intercepted: $path")
                return false
            }

            // 查找路由
            val routeInfo = findRouteInfo(path)
            if (routeInfo == null) {
                Log.w(TAG, "Route not found: $path")
                return false
            }

            // 构建Intent并启动
            val intent = buildIntent(path, params)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                Log.d(TAG, "Navigation successful: $path")
                return true
            }

            false
        } catch (e: Exception) {
            Log.e(TAG, "Navigation failed: $path", e)
            false
        }
    }

    override fun buildIntent(path: String, params: Map<String, Any>?): Intent? {
        val routeInfo = findRouteInfo(path) ?: return null

        return Intent(context, routeInfo.target).apply {
            params?.forEach { (key, value) ->
                when (value) {
                    is String -> putExtra(key, value)
                    is Int -> putExtra(key, value)
                    is Long -> putExtra(key, value)
                    is Float -> putExtra(key, value)
                    is Double -> putExtra(key, value)
                    is Boolean -> putExtra(key, value)
                    is Char -> putExtra(key, value)
                    is Byte -> putExtra(key, value)
                    is Short -> putExtra(key, value)
                    is CharSequence -> putExtra(key, value)
                    is IntArray -> putExtra(key, value)
                    is LongArray -> putExtra(key, value)
                    is FloatArray -> putExtra(key, value)
                    is DoubleArray -> putExtra(key, value)
                    is BooleanArray -> putExtra(key, value)
                    is CharArray -> putExtra(key, value)
                    is ByteArray -> putExtra(key, value)
                    is ShortArray -> putExtra(key, value)
                    is Array<*> -> putExtra(key, value)
                    is ArrayList<*> -> putExtra(key, value)
                    else -> putExtra(key, value.toString())
                }
            }
        }
    }

    override fun hasRoute(path: String): Boolean {
        return routes.containsKey(path) || findRouteByPattern(path) != null
    }

    override fun getRouteInfo(path: String): RouteInfo? {
        return routes[path] ?: findRouteByPattern(path)
    }

    override fun getAllRoutes(): List<RouteInfo> {
        return routes.values.toList()
    }

    override fun findRoutesByPattern(pattern: String): List<RouteInfo> {
        val regexPattern = try {
            Pattern.compile(pattern)
        } catch (e: Exception) {
            Log.e(TAG, "Invalid pattern: $pattern", e)
            return emptyList()
        }

        return routes.values.filter { routeInfo ->
            regexPattern.matcher(routeInfo.path).matches()
        }
    }

    override fun addInterceptor(interceptor: RouterManager.Interceptor) {
        interceptors.add(interceptor)
        interceptors.sortByDescending { it.getPriority() }
        Log.d(TAG, "Interceptor added: ${interceptor.getName()}")
    }

    override fun removeInterceptor(interceptor: RouterManager.Interceptor) {
        interceptors.remove(interceptor)
        Log.d(TAG, "Interceptor removed: ${interceptor.getName()}")
    }

    override fun getInterceptors(): List<RouterManager.Interceptor> {
        return interceptors.toList()
    }

    override fun clearInterceptors() {
        interceptors.clear()
        Log.d(TAG, "All interceptors cleared")
    }

    override fun fallback(
        originalPath: String,
        fallbackPath: String,
        params: Map<String, Any>?
    ): Boolean {
        return fallbackHandler?.handleFallback(originalPath, fallbackPath, params) ?: false
    }

    override fun setFallbackHandler(fallbackHandler: RouterManager.FallbackHandler) {
        this.fallbackHandler = fallbackHandler
        Log.d(TAG, "Fallback handler set")
    }

    private fun findRouteInfo(path: String): RouteInfo? {
        // 先查找精确匹配
        routes[path]?.let { return it }

        // 再查找正则匹配
        return findRouteByPattern(path)
    }

    private fun findRouteByPattern(path: String): RouteInfo? {
        for ((pattern, routeInfo) in routePatterns) {
            if (pattern.matcher(path).matches()) {
                return routeInfo
            }
        }
        return null
    }

    /**
     * 拦截器链实现
     */
    private class InterceptorChainImpl(
        private val index: Int,
        private val interceptors: List<RouterManager.Interceptor>,
        private val routerManager: AdvancedRouterManagerImpl
    ) : RouterManager.InterceptorChain {

        override suspend fun proceed(path: String, params: Map<String, Any>?): Boolean {
            if (index >= interceptors.size) {
                return true
            }

            val interceptor = interceptors[index]
            if (!interceptor.matches(path)) {
                return InterceptorChainImpl(index + 1, interceptors, routerManager).proceed(
                    path,
                    params
                )
            }

            return interceptor.intercept(
                path,
                params,
                InterceptorChainImpl(index + 1, interceptors, routerManager)
            )
        }
    }
}
