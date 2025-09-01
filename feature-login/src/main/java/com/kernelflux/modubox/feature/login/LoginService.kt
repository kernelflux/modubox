package com.kernelflux.modubox.feature.login

/**
 * 登录服务接口
 */
interface LoginService {
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    fun login(username: String, password: String): LoginResult
    
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @return 注册结果
     */
    fun register(username: String, password: String): RegisterResult
    
    /**
     * 用户登出
     */
    fun logout()
    
    /**
     * 检查是否已登录
     * @return 是否已登录
     */
    fun isLoggedIn(): Boolean
    
    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    fun getCurrentUser(): UserInfo?
}

/**
 * 登录结果
 */
data class LoginResult(
    val success: Boolean,
    val message: String,
    val userInfo: UserInfo? = null
)

/**
 * 注册结果
 */
data class RegisterResult(
    val success: Boolean,
    val message: String
)

/**
 * 用户信息
 */
data class UserInfo(
    val userId: String,
    val username: String,
    val email: String? = null,
    val avatar: String? = null
)

/**
 * 登录服务实现类
 */
class LoginServiceImpl : LoginService {
    
    private var currentUser: UserInfo? = null
    
    override fun login(username: String, password: String): LoginResult {
        // 模拟登录逻辑
        return if (username.isNotEmpty() && password.isNotEmpty()) {
            val userInfo = UserInfo(
                userId = "user_${System.currentTimeMillis()}",
                username = username,
                email = "$username@example.com"
            )
            currentUser = userInfo
            LoginResult(
                success = true,
                message = "登录成功",
                userInfo = userInfo
            )
        } else {
            LoginResult(
                success = false,
                message = "用户名或密码不能为空"
            )
        }
    }
    
    override fun register(username: String, password: String): RegisterResult {
        // 模拟注册逻辑
        return if (username.isNotEmpty() && password.isNotEmpty()) {
            RegisterResult(
                success = true,
                message = "注册成功"
            )
        } else {
            RegisterResult(
                success = false,
                message = "用户名或密码不能为空"
            )
        }
    }
    
    override fun logout() {
        currentUser = null
    }
    
    override fun isLoggedIn(): Boolean {
        return currentUser != null
    }
    
    override fun getCurrentUser(): UserInfo? {
        return currentUser
    }
}
