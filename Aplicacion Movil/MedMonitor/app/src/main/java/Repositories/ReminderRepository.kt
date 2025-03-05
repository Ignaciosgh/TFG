package Repositories

import Models.Reminder
import Network.ApiServices
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ReminderRepository (private val apiService: ApiServices){

    fun getRemindersByUser(userId: Long, callback: (List<Reminder>?) -> Unit) {
        apiService.getRemindersByUser(userId).enqueue(object : retrofit2.Callback<List<Reminder>> {
            override fun onResponse(call: Call<List<Reminder>>, response: Response<List<Reminder>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Reminder>>, t: Throwable) {
                callback(null)
            }
        })
    }

}