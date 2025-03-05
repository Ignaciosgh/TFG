package Repositories

import Models.PhysicalActivity
import Models.PhysicalActivityDTO
import Network.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhysicalActivityRepository(private val apiServices: ApiServices) {

    fun getActivitiesByUser(userId: Long, callback: (List<PhysicalActivity>?) -> Unit) {
        apiServices.getActivitiesByUser(userId).enqueue(object : retrofit2.Callback<List<PhysicalActivity>> {
            override fun onResponse(call: Call<List<PhysicalActivity>>, response: Response<List<PhysicalActivity>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<PhysicalActivity>>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun createPhysicalActivity(dto: PhysicalActivityDTO, callback: (Boolean) -> Unit) {
        apiServices.createPhysicalActivity(dto).enqueue(object : Callback<PhysicalActivityDTO> {
            override fun onResponse(call: Call<PhysicalActivityDTO>, response: Response<PhysicalActivityDTO>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<PhysicalActivityDTO>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun updatePhysicalActivity(dto: PhysicalActivityDTO, callback: (Boolean) -> Unit) {
        apiServices.updatePhysicalActivity(dto).enqueue(object : Callback<PhysicalActivityDTO> {
            override fun onResponse(call: Call<PhysicalActivityDTO>, response: Response<PhysicalActivityDTO>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<PhysicalActivityDTO>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun deleteActivity(id: Long, callback: (Boolean) -> Unit) {
        apiServices.deletePhysicalActivity(id).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}
