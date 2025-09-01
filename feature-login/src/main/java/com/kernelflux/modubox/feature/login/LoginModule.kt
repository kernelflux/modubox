package com.kernelflux.modubox.feature.login

import com.kernelflux.modubox.annotation.Module
import com.kernelflux.modubox.annotation.Provides
import com.kernelflux.modubox.annotation.Scope
import com.kernelflux.modubox.annotation.ScopeType

/**
 * 登录模块
 * 提供登录相关的依赖注入
 */
@Module(
    name = "LoginModule",
    description = "登录功能模块",
    priority = 1,
    singleton = true,
    scope = "Application"
)
@Scope(
    name = "LoginScope",
    type = ScopeType.APPLICATION
)
class LoginModule {
    
    /**
     * 提供登录服务
     */
    @Provides(
        name = "LoginService",
        singleton = true,
        scope = "Application"
    )
    fun provideLoginService(): LoginService {
        return LoginServiceImpl()
    }
    
    /**
     * 提供用户管理器
     */
    @Provides(
        name = "UserManager",
        singleton = true,
        scope = "Application"
    )
    fun provideUserManager(): UserManager {
        return UserManagerImpl()
    }
    
    /**
     * 提供登录配置
     */
    @Provides(
        name = "LoginConfig",
        singleton = true,
        scope = "Application"
    )
    fun provideLoginConfig(): LoginConfig {
        return LoginConfig(
            enableBiometric = true,
            enableRememberPassword = true,
            sessionTimeout = 30 * 60 * 1000L // 30分钟
        )
    }
}

/**
 * 登录配置
 */
data class LoginConfig(
    val enableBiometric: Boolean,
    val enableRememberPassword: Boolean,
    val sessionTimeout: Long
)

/**
 * 用户管理器接口
 */
interface UserManager {
    fun getCurrentUser(): String?
    fun setCurrentUser(userId: String)
    fun clearCurrentUser()
}

/**
 * 用户管理器实现
 */
class UserManagerImpl : UserManager {
    private var currentUser: String? = null
    
    override fun getCurrentUser(): String? = currentUser
    
    override fun setCurrentUser(userId: String) {
        currentUser = userId
    }
    
    override fun clearCurrentUser() {
        currentUser = null
    }
}
