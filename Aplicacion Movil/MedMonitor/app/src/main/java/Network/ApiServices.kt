package Network

import Models.Appointment
import Models.AppointmentDTO
import Models.LoginRequest
import Models.LoginResponse
import Models.Medication
import Models.MedicationDTO
import Models.Mood
import Models.MoodDTO
import Models.PhysicalActivity
import Models.PhysicalActivityDTO
import Models.RegisterRequest
import Models.RegisterResponse
import Models.Reminder
import Models.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    // Login
    @POST("/users/login")
    fun login(@Body loginData: LoginRequest): Call<Users>
    // Register
    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("/users/register")
    fun registerUser(@Body user: Users): Call<String>
    // Buscar medicamentos por el id de usuario
    @GET("medications/user/{userId}")
    fun getMedicationsByUser(@Path("userId") userId: Long): Call<List<Medication>>

    @GET("mood/user/{userId}")
    fun getMoodsByUser(@Path("userId") userId: Long): Call<List<Mood>>

    @GET("activities/user/{userId}")
    fun getActivitiesByUser(@Path("userId") userId: Long): Call<List<PhysicalActivity>>

    @GET("reminders/user/{userId}")
    fun getRemindersByUser(@Path("userId") userId: Long): Call<List<Reminder>>

    @POST("medications/createDTO")
    fun createMedication(@Body medicationDTO: MedicationDTO): Call<MedicationDTO>

    @PUT("/medications/updateD/{id}")
    fun updateMedication(@Path("id") id: Long, @Body medication: Medication): Call<Medication>

    @DELETE("/medications/delete/{id}")
    fun deleteMedication(@Path("id") id: Long): Call<Void>

    @PUT("/medications/{id}/status")
    fun updateMedicationStatus(@Path("id") id: Long, @Query("status") status: String): Call<Void>

    @GET("/users/{id}")
    fun getUserById(@Path("id") userId: Long): Call<Users>

    @POST("appointments/createDTO")
    fun createAppointment(@Body appointmentDTO: AppointmentDTO): Call<AppointmentDTO>

    @POST("activities/createDTO")
    fun createPhysicalActivity(@Body physicalActivityDTO: PhysicalActivityDTO): Call<PhysicalActivityDTO>

    @PUT("physical-activities/updateDTO")
    fun updatePhysicalActivity(@Body physicalActivityDTO: PhysicalActivityDTO): Call<PhysicalActivityDTO>

    @POST("/mood/createDTO")
    fun createMood(@Body moodDTO: MoodDTO): Call<MoodDTO>

    @PUT("/mood/update/{id}")
    fun updateMood(@Path("id") id: Long, @Body moodDTO: MoodDTO): Call<MoodDTO>

    @DELETE("/mood/delete/{id}")
    fun deleteMood(@Path("id") id: Long): Call<Void>

    @GET("/appointments/user/{userId}")
    fun getConsultationsByUser(@Path("userId") userId: Long): Call<List<Appointment>>

    @DELETE("/appointments/delete/{id}")
    fun deleteAppointment(@Path("id") id: Long): Call<Void>

    @PUT("/appointments/{id}")
    fun updateAppointment(@Path("id") id: Long, @Body appointment: Appointment): Call<Appointment>

    @PUT("/activities/update/{id}")
    fun updatePhysicalActivity(@Path("id") id: Long, @Body physicalActivityDTO: PhysicalActivityDTO): Call<PhysicalActivity>

    @DELETE("/activities/delete/{id}")
    fun deletePhysicalActivity(@Path("id") id: Long): Call<Void>



}