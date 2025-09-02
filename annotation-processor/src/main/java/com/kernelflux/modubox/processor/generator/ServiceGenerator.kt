package com.kernelflux.modubox.processor.generator

import com.kernelflux.modubox.processor.model.ServiceInfo
import com.squareup.kotlinpoet.*

/**
 * 服务代码生成器
 * 专门处理服务相关的代码生成
 */
class ServiceGenerator : RegistryGenerator<ServiceInfo>(
    packageName = "com.kernelflux.modubox.registry",
    className = "ServiceRegistry"
) {
    
    override fun createRegisterMethod(items: List<ServiceInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val dependencyManager = moduBox.getDependencyManager()")
        
        items.forEach { service ->
            body.addStatement("dependencyManager.registerService(${service.className}::class, ${service.className}())")
        }
        
        return createStandardRegisterMethod("registerServices", body)
    }
    
    override fun createInfoMethod(items: List<ServiceInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val services = mutableMapOf<String, String>()")
        
        items.forEach { service ->
            body.addStatement("services[\"${service.name}\"] = \"${service.className}\"")
        }
        
        body.addStatement("return services")
        
        return createStandardInfoMethod("getServiceInfo", body)
    }
}
