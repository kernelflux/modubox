package com.kernelflux.modubox.processor.model

/**
 * 组件信息数据模型
 */
data class ComponentInfo(
    val className: String,
    val simpleName: String,
    val name: String,
    val description: String,
    val scope: String,
    val modules: List<String>,
    val dependencies: List<String>,
    val singleton: Boolean,
    val lazy: Boolean,
    val hotReload: Boolean,
    val priority: Int
)
