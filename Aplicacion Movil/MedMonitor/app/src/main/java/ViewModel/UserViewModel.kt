package ViewModel

import Network.ApiServices
import Models.LoginRequest
import Models.LoginResponse
import Models.RegisterResponse
import Models.Users
import Network.RetrofitClient
import Repositories.UserRepository
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(
    private val userRepository: UserRepository,
    private val application: Application
) : AndroidViewModel(application) {

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>>  get() = _loginResult

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult


    fun login(correo: String, contraseña: String) {
        val loginRequest = LoginRequest(correo, contraseña)

        RetrofitClient.apiService.login(loginRequest).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        val userId = user.id
                        val userName = user.nombre
                        val userEmail = user.correo
                        saveUserSession(isLoggedIn  = true, userId = user.id!!)
                        saveUserName(userName)
                        saveUserEmail(userEmail)

                        _loginResult.postValue(Result.success("Login exitoso"))
                    } else {
                        _loginResult.postValue(Result.failure(Exception("Usuario no encontrado")))
                    }
                } else {
                    _loginResult.postValue(Result.failure(Exception("Error de login: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                _loginResult.postValue(Result.failure(t))
            }
        })
    }

    private fun saveUserSession(isLoggedIn: Boolean, userId: Long) {
        val sharedPreferences = application.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean("is_logged_in", isLoggedIn)
            .putLong("userId", userId)
            .apply()
    }


    private fun saveUserName(userName: String) {
        val sharedPreferences = application.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("userName", userName)
            .apply()
    }

    private fun saveUserEmail(userEmail: String) {
        val sharedPreferences = application.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("userEmail", userEmail)
            .apply()
    }


    fun isUserLoggedIn(): Boolean {
        val sharedPreferences = application.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun getUserId(): Long {
        val sharedPreferences = application.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("userId", -1L)
    }

    fun getUserName(): String? {
        val sharedPreferences = application.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userName", null)
    }

    fun getUserEmail(): String? {
        val sharedPreferences = application.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userEmail", null)
    }

    fun logout() {
        val sharedPreferences = application.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", false)
        editor.apply()
    }


    fun register(nombre: String, correo: String, contraseña: String) {
        userRepository.register(nombre, correo, contraseña).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _registerResult.postValue(Result.success(it))
                    } ?: _registerResult.postValue(Result.failure(Exception("Respuesta vacia del servidor")))
                } else {
                    _registerResult.value = Result.failure(Exception("Error de registro: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResult.postValue(Result.failure(t))
            }
        })
    }
}