package Repositories

import Models.Appointment
import Models.AppointmentDTO
import Network.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppintmentRepository (private val apiServices: ApiServices){

    fun getConsultationsByUser(userId: Long, callback: (List<Appointment>?) -> Unit) {
        apiServices.getConsultationsByUser(userId).enqueue(object : retrofit2.Callback<List<Appointment>> {
            override fun onResponse(call: Call<List<Appointment>>, response: Response<List<Appointment>>) {
                if (response.isSuccessful) {
                    callback(response.body()) // Devuelve la lista si la llamada es exitosa
                } else {
                    callback(null) // Devuelve null en caso de error
                }
            }

            override fun onFailure(call: Call<List<Appointment>>, t: Throwable) {
                callback(null) // Devuelve null en caso de fallo
            }
        })
    }


    fun createAppointment(appointmentDTO: AppointmentDTO, callback: (AppointmentDTO?) -> Unit) {
        apiServices.createAppointment(appointmentDTO).enqueue(object : retrofit2.Callback<AppointmentDTO> {
            override fun onResponse(call: Call<AppointmentDTO>, response: Response<AppointmentDTO>) {
                callback(response.body()) // Devuelve el objeto creado si la llamada fue exitosa
            }

            override fun onFailure(call: Call<AppointmentDTO>, t: Throwable) {
                callback(null) // Devuelve null si hubo un error en la llamada
            }
        })
    }

    fun deleteAppointment(id: Long, callback: (Boolean) -> Unit) {
        apiServices.deleteAppointment(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }


    fun updateAppointment(id: Long, appointment: Appointment, callback: (Appointment?) -> Unit) {
        apiServices.updateAppointment(id, appointment).enqueue(object : Callback<Appointment> {
            override fun onResponse(call: Call<Appointment>, response: Response<Appointment>) {
                callback(response.body())
            }

            override fun onFailure(call: Call<Appointment>, t: Throwable) {
                callback(null)
            }
        })
    }





}