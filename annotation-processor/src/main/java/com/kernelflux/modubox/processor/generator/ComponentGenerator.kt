package com.kernelflux.modubox.processor.generator

import com.kernelflux.modubox.processor.model.ComponentInfo
import com.squareup.kotlinpoet.*

/**
 * 组件代码生成器
 * 专门处理组件相关的代码生成
 */
class ComponentGenerator : RegistryGenerator<ComponentInfo>(
    packageName = "com.kernelflux.modubox.registry",
    className = "ComponentRegistry"
) {
    
    override fun createRegisterMethod(items: List<ComponentInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val dependencyManager = moduBox.getDependencyManager()")
        
        items.forEach { component ->
            body.addStatement("dependencyManager.registerComponent(${component.className}::class, ${component.className}())")
        }
        
        return createStandardRegisterMethod("registerComponents", body)
    }
    
    override fun createInfoMethod(items: List<ComponentInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val components = mutableMapOf<String, String>()")
        
        items.forEach { component ->
            body.addStatement("components[\"${component.name}\"] = \"${component.description}\"")
        }
        
        body.addStatement("return components")
        
        return createStandardInfoMethod("getComponentInfo", body)
    }
}
