package com.kernelflux.modubox.processor.model

/**
 * 路由信息数据模型
 */
data class RouteInfo(
    val className: String,
    val simpleName: String,
    val path: String,
    val group: String,
    val description: String,
    val requireLogin: Boolean,
    val permissions: List<String>,
    val priority: Int,
    val regex: Boolean,
    val fallback: Boolean,
    val fallbackPath: String,
    val extras: List<String>,
    val webView: Boolean,
    val external: Boolean,
    val externalPackage: String,
    val deepLink: Boolean,
    val scheme: String,
    val multiProcess: Boolean,
    val process: String,
    val abTest: Boolean,
    val abTestConfig: String,
    val tags: List<String>,
    val enabled: Boolean
)
