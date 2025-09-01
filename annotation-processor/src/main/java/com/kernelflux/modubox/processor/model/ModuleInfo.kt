package com.kernelflux.modubox.processor.model

/**
 * 模块信息数据模型
 */
data class ModuleInfo(
    val className: String,
    val simpleName: String,
    val name: String,
    val description: String,
    val priority: Int,
    val singleton: Boolean,
    val dependencies: List<String>,
    val scope: String,
    val lazy: Boolean,
    val hotReload: Boolean
)
