package Repositories

import Models.Mood
import Models.MoodDTO
import Network.ApiServices
import android.telecom.Call
import retrofit2.Callback
import retrofit2.Response


class MoodRepository(private val apiServices: ApiServices) {

    fun getMoodsByUser(userId: Long, callback: (List<Mood>?) -> Unit) {
        apiServices.getMoodsByUser(userId).enqueue(object : retrofit2.Callback<List<Mood>> {
            override fun onResponse(call: retrofit2.Call<List<Mood>>, response: Response<List<Mood>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Mood>>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun createMood(moodDTO: MoodDTO, callback: (MoodDTO?) -> Unit) {
        apiServices.createMood(moodDTO).enqueue(object : Callback<MoodDTO> {
            override fun onResponse(call: retrofit2.Call<MoodDTO>, response: Response<MoodDTO>) {
                callback(response.body())
            }

            override fun onFailure(call: retrofit2.Call<MoodDTO>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun updateMood(id: Long, moodDTO: MoodDTO, callback: (MoodDTO?) -> Unit) {
        apiServices.updateMood(id, moodDTO).enqueue(object : Callback<MoodDTO> {
            override fun onResponse(call: retrofit2.Call<MoodDTO>, response: Response<MoodDTO>) {
                callback(response.body())
            }

            override fun onFailure(call: retrofit2.Call<MoodDTO>, t: Throwable) {
                callback(null)
            }
        })
    }


    fun deleteMood(moodId: Long, callback: (Boolean) -> Unit) {
        apiServices.deleteMood(moodId).enqueue(object : Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

}
