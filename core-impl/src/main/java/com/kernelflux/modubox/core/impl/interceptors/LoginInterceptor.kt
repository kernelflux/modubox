package com.kernelflux.modubox.core.impl.interceptors

import android.util.Log
import com.kernelflux.modubox.annotation.RouteInterceptor
import com.kernelflux.modubox.core.api.RouterManager

/**
 * 登录拦截器
 * 检查用户是否已登录，未登录则跳转到登录页面
 */
@RouteInterceptor(
    name = "LoginInterceptor",
    description = "检查用户登录状态",
    priority = 100,
    group = "auth",
    global = true,
    patterns = ["/profile/*", "/order/*", "/payment/*"],
    excludes = [],
    async = false,
    timeout = 3000,
    enabled = true,
    tags = ["auth", "login"],
    hotReload = false
)
class LoginInterceptor : RouterManager.Interceptor {
    
    companion object {
        private const val TAG = "LoginInterceptor"
    }
    
    override suspend fun intercept(
        path: String,
        params: Map<String, Any>?,
        chain: RouterManager.InterceptorChain
    ): Boolean {
        Log.d(TAG, "LoginInterceptor checking path: $path")
        
        // 检查用户是否已登录
        if (!isUserLoggedIn()) {
            Log.w(TAG, "User not logged in, redirecting to login page")
            return false
        }
        
        Log.d(TAG, "User logged in, proceeding with navigation")
        return chain.proceed(path, params)
    }
    
    override fun getPriority(): Int = 100
    
    override fun getName(): String = "LoginInterceptor"
    
    override fun matches(path: String): Boolean {
        // 匹配需要登录的路径
        return path.startsWith("/profile/") || 
               path.startsWith("/order/") || 
               path.startsWith("/payment/")
    }
    
    private fun isUserLoggedIn(): Boolean {
        // 这里应该检查实际的登录状态
        // 暂时返回false用于测试
        return false
    }
}
