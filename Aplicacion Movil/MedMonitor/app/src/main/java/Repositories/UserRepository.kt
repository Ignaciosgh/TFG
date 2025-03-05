package Repositories

import Models.LoginRequest
import Models.LoginResponse
import Models.RegisterRequest
import Models.RegisterResponse
import Models.Users
import Network.ApiServices
import okhttp3.ResponseBody
import retrofit2.Call


class UserRepository(private val apiService: ApiServices) {

    fun login(loginData: LoginRequest): Call<Users> {
        return apiService.login(loginData)
    }

    fun register(nombre: String, correo: String, contraseña: String): Call<RegisterResponse> {
        return apiService.register(RegisterRequest(nombre, correo, contraseña))
    }

}