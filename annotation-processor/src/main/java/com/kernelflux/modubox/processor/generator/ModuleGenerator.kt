package com.kernelflux.modubox.processor.generator

import com.kernelflux.modubox.processor.model.ModuleInfo
import com.squareup.kotlinpoet.*

/**
 * 模块代码生成器
 * 专门处理模块相关的代码生成
 */
class ModuleGenerator : RegistryGenerator<ModuleInfo>(
    packageName = "com.kernelflux.modubox.registry",
    className = "ModuleRegistry"
) {
    
    override fun createRegisterMethod(items: List<ModuleInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val dependencyManager = moduBox.getDependencyManager()")
        
        items.forEach { module ->
            body.addStatement("dependencyManager.registerModule(${module.className}())")
        }
        
        return createStandardRegisterMethod("registerModules", body)
    }
    
    override fun createInfoMethod(items: List<ModuleInfo>): FunSpec {
        val body = CodeBlock.builder()
            .addStatement("val modules = mutableMapOf<String, String>()")
        
        items.forEach { module ->
            body.addStatement("modules[\"${module.name}\"] = \"${module.description}\"")
        }
        
        body.addStatement("return modules")
        
        return createStandardInfoMethod("getModuleInfo", body)
    }
}
