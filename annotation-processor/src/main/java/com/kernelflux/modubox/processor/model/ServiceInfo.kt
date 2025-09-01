package com.kernelflux.modubox.processor.model

/**
 * 服务信息数据模型
 */
data class ServiceInfo(
    val className: String,
    val simpleName: String,
    val name: String,
    val singleton: Boolean
)
