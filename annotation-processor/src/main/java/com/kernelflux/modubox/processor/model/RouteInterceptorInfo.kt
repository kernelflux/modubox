package com.kernelflux.modubox.processor.model

/**
 * 路由拦截器信息数据模型
 */
data class RouteInterceptorInfo(
    val className: String,
    val simpleName: String,
    val name: String,
    val description: String,
    val priority: Int,
    val group: String,
    val global: Boolean,
    val patterns: List<String>,
    val excludes: List<String>,
    val async: Boolean,
    val timeout: Long,
    val enabled: Boolean,
    val tags: List<String>,
    val hotReload: Boolean
)
