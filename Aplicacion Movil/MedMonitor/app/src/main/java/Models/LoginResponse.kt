package Models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("users") val users: Users
)

