package com.kernelflux.modubox.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.kernelflux.modubox.annotation.*
import com.kernelflux.modubox.processor.generator.*
import com.kernelflux.modubox.processor.model.*

/**
 * ModuBox KSP注解处理器
 * 参考大厂主流技术方案，支持大型组件化App
 */
class ModuBoxProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    
    private val pluginInfos = mutableListOf<PluginInfo>()
    private val routeInfos = mutableListOf<RouteInfo>()
    private val routeInterceptorInfos = mutableListOf<RouteInterceptorInfo>()
    private val serviceInfos = mutableListOf<ServiceInfo>()
    private val moduleInfos = mutableListOf<ModuleInfo>()
    private val componentInfos = mutableListOf<ComponentInfo>()
    private val providesInfos = mutableListOf<ProvidesInfo>()
    
    override fun process(resolver: Resolver): List<KSAnnotated> {
        // 处理所有注解
        processAnnotations(resolver)
        
        // 生成代码
        generateCode()
        
        return emptyList()
    }
    
    private fun processAnnotations(resolver: Resolver) {
        // 处理@Plugin注解
        resolver.getSymbolsWithAnnotation(Plugin::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processPlugin(it) }
        
        // 处理@Route注解
        resolver.getSymbolsWithAnnotation(Route::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processRoute(it) }
        
        // 处理@RouteInterceptor注解
        resolver.getSymbolsWithAnnotation(RouteInterceptor::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processRouteInterceptor(it) }
        
        // 处理@Service注解
        resolver.getSymbolsWithAnnotation(Service::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processService(it) }
        
        // 处理@Module注解
        resolver.getSymbolsWithAnnotation(Module::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processModule(it) }
        
        // 处理@Component注解
        resolver.getSymbolsWithAnnotation(Component::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { processComponent(it) }
        
        // 处理@Provides注解
        resolver.getSymbolsWithAnnotation(Provides::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()
            .forEach { processProvides(it) }
    }
    
    private fun processPlugin(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Plugin" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val pluginInfo = PluginInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            id = arguments["id"] as? String ?: "",
            name = (arguments["name"] as? String)?.ifEmpty { symbol.simpleName.asString() } ?: symbol.simpleName.asString(),
            version = arguments["version"] as? String ?: "1.0.0",
            description = arguments["description"] as? String ?: "",
            autoStart = arguments["autoStart"] as? Boolean ?: false,
            dependencies = (arguments["dependencies"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList()
        )
        
        pluginInfos.add(pluginInfo)
        logger.info("Found plugin: ${pluginInfo.id}")
    }
    
    private fun processRoute(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Route" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val routeInfo = RouteInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            path = arguments["path"] as? String ?: "",
            group = arguments["group"] as? String ?: "",
            description = arguments["description"] as? String ?: "",
            requireLogin = arguments["requireLogin"] as? Boolean ?: false,
            permissions = (arguments["permissions"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            priority = arguments["priority"] as? Int ?: 0,
            regex = arguments["regex"] as? Boolean ?: false,
            fallback = arguments["fallback"] as? Boolean ?: false,
            fallbackPath = arguments["fallbackPath"] as? String ?: "",
            extras = (arguments["extras"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            webView = arguments["webView"] as? Boolean ?: false,
            external = arguments["external"] as? Boolean ?: false,
            externalPackage = arguments["externalPackage"] as? String ?: "",
            deepLink = arguments["deepLink"] as? Boolean ?: false,
            scheme = arguments["scheme"] as? String ?: "",
            multiProcess = arguments["multiProcess"] as? Boolean ?: false,
            process = arguments["process"] as? String ?: "",
            abTest = arguments["abTest"] as? Boolean ?: false,
            abTestConfig = arguments["abTestConfig"] as? String ?: "",
            tags = (arguments["tags"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            enabled = arguments["enabled"] as? Boolean ?: true
        )
        
        routeInfos.add(routeInfo)
        logger.info("Found route: ${routeInfo.path}")
    }
    
    private fun processRouteInterceptor(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "RouteInterceptor" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val interceptorInfo = RouteInterceptorInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            name = arguments["name"] as? String ?: "",
            description = arguments["description"] as? String ?: "",
            priority = arguments["priority"] as? Int ?: 0,
            group = arguments["group"] as? String ?: "",
            global = arguments["global"] as? Boolean ?: false,
            patterns = (arguments["patterns"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            excludes = (arguments["excludes"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            async = arguments["async"] as? Boolean ?: false,
            timeout = arguments["timeout"] as? Long ?: 3000,
            enabled = arguments["enabled"] as? Boolean ?: true,
            tags = (arguments["tags"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            hotReload = arguments["hotReload"] as? Boolean ?: false
        )
        
        routeInterceptorInfos.add(interceptorInfo)
        logger.info("Found route interceptor: ${interceptorInfo.name}")
    }
    
    private fun processService(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Service" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val serviceInfo = ServiceInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            name = (arguments["name"] as? String)?.ifEmpty { symbol.simpleName.asString() } ?: symbol.simpleName.asString(),
            singleton = arguments["singleton"] as? Boolean ?: true
        )
        
        serviceInfos.add(serviceInfo)
        logger.info("Found service: ${serviceInfo.name}")
    }
    
    private fun processModule(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Module" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val moduleInfo = ModuleInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            name = arguments["name"] as? String ?: "",
            description = arguments["description"] as? String ?: "",
            priority = arguments["priority"] as? Int ?: 0,
            singleton = arguments["singleton"] as? Boolean ?: true,
            dependencies = (arguments["dependencies"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            scope = arguments["scope"] as? String ?: "Application",
            lazy = arguments["lazy"] as? Boolean ?: false,
            hotReload = arguments["hotReload"] as? Boolean ?: false
        )
        
        moduleInfos.add(moduleInfo)
        logger.info("Found module: ${moduleInfo.name}")
    }
    
    private fun processComponent(symbol: KSClassDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Component" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val componentInfo = ComponentInfo(
            className = symbol.qualifiedName?.asString() ?: "",
            simpleName = symbol.simpleName.asString(),
            name = arguments["name"] as? String ?: "",
            description = arguments["description"] as? String ?: "",
            scope = arguments["scope"] as? String ?: "Application",
            modules = (arguments["modules"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            dependencies = (arguments["dependencies"] as? Array<*>)?.mapNotNull { it as? String } ?: emptyList(),
            singleton = arguments["singleton"] as? Boolean ?: true,
            lazy = arguments["lazy"] as? Boolean ?: false,
            hotReload = arguments["hotReload"] as? Boolean ?: false,
            priority = arguments["priority"] as? Int ?: 0
        )
        
        componentInfos.add(componentInfo)
        logger.info("Found component: ${componentInfo.name}")
    }
    
    private fun processProvides(symbol: KSFunctionDeclaration) {
        val annotation = symbol.annotations.first { it.shortName.asString() == "Provides" }
        val arguments = annotation.arguments.associate { it.name?.asString() to it.value }
        
        val providesInfo = ProvidesInfo(
            className = symbol.parentDeclaration?.qualifiedName?.asString() ?: "",
            methodName = symbol.simpleName.asString(),
            name = arguments["name"] as? String ?: "",
            singleton = arguments["singleton"] as? Boolean ?: true,
            scope = arguments["scope"] as? String ?: "Application",
            priority = arguments["priority"] as? Int ?: 0,
            lazy = arguments["lazy"] as? Boolean ?: false,
            condition = arguments["condition"] as? String ?: "",
            hotReload = arguments["hotReload"] as? Boolean ?: false,
            returnType = symbol.returnType?.resolve()?.declaration?.qualifiedName?.asString() ?: ""
        )
        
        providesInfos.add(providesInfo)
        logger.info("Found provides: ${providesInfo.methodName}")
    }
    
    private fun generateCode() {
        try {
            if (hasAnyAnnotations()) {
                generateSpecializedRegistries()
                generateMainRegistry()
            }
        } catch (e: Exception) {
            logger.error("Failed to generate code: ${e.message}")
        }
    }
    
    private fun generateSpecializedRegistries() {
        // 生成专门的注册器
        PluginGenerator().generate(codeGenerator, pluginInfos)
        RouteGenerator().generate(codeGenerator, routeInfos)
        InterceptorGenerator().generate(codeGenerator, routeInterceptorInfos)
        ServiceGenerator().generate(codeGenerator, serviceInfos)
        ModuleGenerator().generate(codeGenerator, moduleInfos)
        ComponentGenerator().generate(codeGenerator, componentInfos)
    }
    
    private fun generateMainRegistry() {
        val content = buildString {
            appendLine("package com.kernelflux.modubox.registry;")
            appendLine()
            appendLine("import com.kernelflux.modubox.core.api.ModuBox;")
            appendLine("import java.util.Map;")
            appendLine("import java.util.HashMap;")
            appendLine()
            appendLine("/**")
            appendLine(" * ModuBox统一注册入口")
            appendLine(" * 由KSP注解处理器自动生成，请勿手动修改")
            appendLine(" */")
            appendLine("public class ModuBoxRegistry {")
            appendLine()
            
            // 生成统一注册方法
            append(generateUnifiedRegisterMethod())
            appendLine()
            
            // 生成统一信息获取方法
            append(generateUnifiedInfoMethod())
            appendLine()
            
            appendLine("}")
        }
        
        val fileSpec = codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "com.kernelflux.modubox.registry",
            fileName = "ModuBoxRegistry"
        )
        
        fileSpec.writer().use { writer ->
            writer.write(content)
        }
    }
    
    private fun generateUnifiedRegisterMethod(): String {
        val body = buildString {
            if (pluginInfos.isNotEmpty()) {
                appendLine("        // 注册插件")
                appendLine("        PluginRegistry.registerPlugins(moduBox);")
            }
            
            if (routeInfos.isNotEmpty()) {
                appendLine("        // 注册路由")
                appendLine("        RouteRegistry.registerRoutes(moduBox);")
            }
            
            if (routeInterceptorInfos.isNotEmpty()) {
                appendLine("        // 注册拦截器")
                appendLine("        InterceptorRegistry.registerInterceptors(moduBox);")
            }
            
            if (serviceInfos.isNotEmpty()) {
                appendLine("        // 注册服务")
                appendLine("        ServiceRegistry.registerServices(moduBox);")
            }
            
            if (moduleInfos.isNotEmpty()) {
                appendLine("        // 注册模块")
                appendLine("        ModuleRegistry.registerModules(moduBox);")
            }
            
            if (componentInfos.isNotEmpty()) {
                appendLine("        // 注册组件")
                appendLine("        ComponentRegistry.registerComponents(moduBox);")
            }
        }
        
        return buildString {
            appendLine("    /**")
            appendLine("     * 执行所有注册")
            appendLine("     */")
            appendLine("    public static void registerAll(ModuBox moduBox) {")
            append(body)
            appendLine("    }")
        }
    }
    
    private fun generateUnifiedInfoMethod(): String {
        val body = buildString {
            appendLine("        Map<String, Object> info = new HashMap<>();")
            appendLine()
            
            if (pluginInfos.isNotEmpty()) {
                appendLine("        // 插件信息")
                appendLine("        info.put(\"plugins\", PluginRegistry.getPluginInfo());")
            }
            
            if (routeInfos.isNotEmpty()) {
                appendLine("        // 路由信息")
                appendLine("        info.put(\"routes\", RouteRegistry.getRouteInfo());")
            }
            
            if (routeInterceptorInfos.isNotEmpty()) {
                appendLine("        // 拦截器信息")
                appendLine("        info.put(\"interceptors\", InterceptorRegistry.getInterceptorInfo());")
            }
            
            if (serviceInfos.isNotEmpty()) {
                appendLine("        // 服务信息")
                appendLine("        info.put(\"services\", ServiceRegistry.getServiceInfo());")
            }
            
            if (moduleInfos.isNotEmpty()) {
                appendLine("        // 模块信息")
                appendLine("        info.put(\"modules\", ModuleRegistry.getModuleInfo());")
            }
            
            if (componentInfos.isNotEmpty()) {
                appendLine("        // 组件信息")
                appendLine("        info.put(\"components\", ComponentRegistry.getComponentInfo());")
            }
            
            appendLine()
            appendLine("        return info;")
        }
        
        return buildString {
            appendLine("    /**")
            appendLine("     * 获取注册信息")
            appendLine("     */")
            appendLine("    public static Map<String, Object> getRegistrationInfo() {")
            append(body)
            appendLine("    }")
        }
    }
    
    private fun hasAnyAnnotations(): Boolean {
        return pluginInfos.isNotEmpty() || routeInfos.isNotEmpty() || routeInterceptorInfos.isNotEmpty() || 
               serviceInfos.isNotEmpty() || moduleInfos.isNotEmpty() || componentInfos.isNotEmpty()
    }
}

/**
 * ModuBox KSP处理器提供者
 */
class ModuBoxProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ModuBoxProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}
