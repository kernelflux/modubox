package com.kernelflux.modubox.processor.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies

/**
 * 基础代码生成器
 * 提供通用的代码生成功能，便于扩展
 */
abstract class BaseGenerator {
    
    /**
     * 生成代码文件
     * @param codeGenerator KSP代码生成器
     * @param packageName 包名
     * @param fileName 文件名
     * @param content 文件内容
     */
    protected fun generateFile(
        codeGenerator: CodeGenerator,
        packageName: String,
        fileName: String,
        content: String
    ) {
        val fileSpec = codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = packageName,
            fileName = fileName
        )
        
        fileSpec.writer().use { writer ->
            writer.write(content)
        }
    }
    
    /**
     * 生成Java类文件
     * @param codeGenerator KSP代码生成器
     * @param packageName 包名
     * @param className 类名
     * @param imports 导入语句列表
     * @param classContent 类内容
     */
    protected fun generateJavaClass(
        codeGenerator: CodeGenerator,
        packageName: String,
        className: String,
        imports: List<String> = emptyList(),
        classContent: String
    ) {
        val content = buildString {
            appendLine("package $packageName;")
            appendLine()
            
            // 添加导入语句
            imports.forEach { import ->
                appendLine("import $import;")
            }
            appendLine()
            
            // 添加类内容
            append(classContent)
        }
        
        generateFile(codeGenerator, packageName, className, content)
    }
    
    /**
     * 生成注释
     * @param comment 注释内容
     * @return 格式化的注释
     */
    protected fun generateComment(comment: String): String {
        return buildString {
            appendLine("    /**")
            appendLine("     * $comment")
            appendLine("     */")
        }
    }
    
    /**
     * 生成方法
     * @param modifiers 修饰符
     * @param returnType 返回类型
     * @param methodName 方法名
     * @param parameters 参数列表
     * @param body 方法体
     * @return 格式化的方法
     */
    protected fun generateMethod(
        modifiers: String,
        returnType: String,
        methodName: String,
        parameters: String = "",
        body: String
    ): String {
        return buildString {
            appendLine("    $modifiers $returnType $methodName($parameters) {")
            appendLine(body)
            appendLine("    }")
        }
    }
}
