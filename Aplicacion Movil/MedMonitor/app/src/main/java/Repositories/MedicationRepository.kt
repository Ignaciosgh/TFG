package Repositories

import Models.Medication
import Models.MedicationDTO
import Network.ApiServices
import retrofit2.Call
import retrofit2.Response

class MedicationRepository (private val apiServices: ApiServices){


    fun getMedicationsByUser(userId: Long, callback: (List<Medication>?) -> Unit) {
        apiServices.getMedicationsByUser(userId).enqueue(object : retrofit2.Callback<List<Medication>> {
            override fun onResponse(call: Call<List<Medication>>, response: Response<List<Medication>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Medication>>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun createMedication(medicationDTO: MedicationDTO, callback: (MedicationDTO?) -> Unit) {
        apiServices.createMedication(medicationDTO).enqueue(object : retrofit2.Callback<MedicationDTO> {
            override fun onResponse(call: Call<MedicationDTO>, response: Response<MedicationDTO>) {
                callback(response.body())
            }

            override fun onFailure(p0: Call<MedicationDTO>, p1: Throwable) {
                callback(null)
            }
        })
    }

    fun updateMedication(medicationId: Long, updatedMedicationDTO: Medication, callback: (Boolean) -> Unit) {
        apiServices.updateMedication(medicationId, updatedMedicationDTO).enqueue(object : retrofit2.Callback<Medication> {
            override fun onResponse(call: Call<Medication>, response: Response<Medication>) {
                if (response.isSuccessful) {
                    callback(true) // Éxito
                } else {
                    callback(false) // Error en la actualización
                }
            }

            override fun onFailure(call: Call<Medication>, t: Throwable) {
                callback(false) // Error de red o de conexión
            }
        })
    }


    fun deleteMedication(id: Long, callback: (Boolean) -> Unit) {
        apiServices.deleteMedication(id).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun updateMedicationStatus(medicationId: Long, status: String, callback: (Boolean) -> Unit) {
        apiServices.updateMedicationStatus(medicationId, status).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }





}