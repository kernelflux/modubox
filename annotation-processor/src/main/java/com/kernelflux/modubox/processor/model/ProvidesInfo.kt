package com.kernelflux.modubox.processor.model

/**
 * 提供者信息数据模型
 */
data class ProvidesInfo(
    val className: String,
    val methodName: String,
    val name: String,
    val singleton: Boolean,
    val scope: String,
    val priority: Int,
    val lazy: Boolean,
    val condition: String,
    val hotReload: Boolean,
    val returnType: String
)
