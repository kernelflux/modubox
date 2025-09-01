package com.kernelflux.modubox.processor.model

/**
 * 插件信息数据模型
 */
data class PluginInfo(
    val className: String,
    val simpleName: String,
    val id: String,
    val name: String,
    val version: String,
    val description: String,
    val autoStart: Boolean,
    val dependencies: List<String>
)
