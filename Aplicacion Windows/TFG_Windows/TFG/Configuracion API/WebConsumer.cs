using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using System.Windows.Forms;
using TFG.BD;

namespace TFG
{
    public class WebConsumer
    {
        private readonly string _host;
        private readonly HttpClient _client;
        private readonly JsonSerializerOptions _serializerOptions;

        public WebConsumer(string host)
        {
            _host = host;
            _client = new HttpClient();

            _serializerOptions = new JsonSerializerOptions
            {
                PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
                Converters = { new JsonStringEnumConverter() }
            };
        }

        // Mostrar todos los medicamentos
        public async Task<List<Medications>> ObtenerTodosLosMedicamentosAsync()
        {
            string url = $"{_host}/medications/all";
            HttpResponseMessage response = await _client.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<List<Medications>>(content, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al obtener medicamentos. Código: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        // MOstrar un medicamento por ID
        // Mostrar un medicamento por ID: GET /medications/{id}
        public async Task<Medications> ObtenerMedicamentoPorIdAsync(long id)
        {
            string url = $"{_host}/medications/{id}";
            HttpResponseMessage response = await _client.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Medications>(content, _serializerOptions);
            }
            else
            {
                return null;
            }
        }

        // Crear Medicamento
        public async Task<Medications> CrearMedicamentoAsync(MedicationDTO nuevoMedicamento)
        {
            string url = $"{_host}/medications/create";
            string jsonContent = JsonSerializer.Serialize(nuevoMedicamento, _serializerOptions);
            var content = new StringContent(jsonContent, Encoding.UTF8, "application/json");

            HttpResponseMessage response = await _client.PostAsync(url, content);
            if (response.IsSuccessStatusCode)
            {
                string resp = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Medications>(resp, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al crear el medicamento. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }


        // Editar Medicamento
        public async Task<Medications> EditarMedicamentoAsync(long id, MedicationDTO medicamentoDTO)
        {
            string url = $"{_host}/medications/{id}";
            string json = JsonSerializer.Serialize(medicamentoDTO, _serializerOptions);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            HttpResponseMessage response = await _client.PutAsync(url, content);
            if (response.IsSuccessStatusCode)
            {
                string resp = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Medications>(resp, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al editar el medicamento. Código: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        // Eliminar medicamento
        public async Task<bool> EliminarMedicamentoAsync(long id)
        {
            string url = $"{_host}/medications/delete/{id}";
            HttpResponseMessage response = await _client.DeleteAsync(url);
            return response.IsSuccessStatusCode;
        }

        // Obtener usuario por ID para validaciones
        public async Task<Users> ObtenerUsuarioPorIdAsync(long id)
        {
            // Ajusta la URL a la que obtenga usuario por ID si existe, si tu API no tiene este endpoint, debes ajustarlo.
            string url = $"{_host}/users/{id}";
            HttpResponseMessage response = await _client.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Users>(content, _serializerOptions);
            }
            else
            {
                return null;
            }
        }


        // GET / Appointments
        public async Task<List<Appointments>> ObtenerTodosLosAppointmentsAsync()
        {
            string url = $"{_host}/appointments";
            HttpResponseMessage response = await _client.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<List<Appointments>>(content, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al obtener las citas. Código: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }
        // GET / Appointments /{id}
        public async Task<Appointments> ObtenerAppointmentPorIdAsync(long id)
        {
            string url = $"{_host}/appointments/{id}";
            HttpResponseMessage response = await _client.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Appointments>(content, _serializerOptions);
            }
            else
            {
                return null;
            }
        }
        // GET / Appointments / user / {userId}
        public async Task<List<Appointments>> ObtenerAppointmentsPorUsuarioAsync(long userId)
        {
            string url = $"{_host}/appointments/user/{userId}";
            HttpResponseMessage response = await _client.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<List<Appointments>>(content, _serializerOptions);
            }
            else
            {
                if (response.StatusCode == HttpStatusCode.NoContent)
                {
                    // No hay citas
                    return new List<Appointments>();
                }
                MessageBox.Show($"Error al obtener citas del usuario. Código: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }
        // POST / Appointments / createDTO
        public async Task<Appointments> CrearAppointmentAsync(AppointmentDTO appointmentDTO)
        {
            string url = $"{_host}/appointments/createDTO";
            string json = JsonSerializer.Serialize(appointmentDTO, _serializerOptions);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            HttpResponseMessage response = await _client.PostAsync(url, content);

            if (response.IsSuccessStatusCode)
            {
                string resp = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Appointments>(resp, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al crear la cita. Código: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }
        // PUT / Appointments / update / {id}
        public async Task<Appointments> EditarAppointmentAsync(long id, AppointmentDTO appointmentDTO)
        {
            string url = $"{_host}/appointments/update/{id}";
            string json = JsonSerializer.Serialize(appointmentDTO, _serializerOptions);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            HttpResponseMessage response = await _client.PutAsync(url, content);

            if (response.IsSuccessStatusCode)
            {
                string resp = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Appointments>(resp, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al editar la cita. Código: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }
        // DELETE / Appointments / delete / {id}
        public async Task<bool> EliminarAppointmentAsync(long id)
        {
            string url = $"{_host}/appointments/delete/{id}";
            HttpResponseMessage response = await _client.DeleteAsync(url);

            return response.IsSuccessStatusCode;
        }

        // GET / activities
        public async Task<List<PhysicalActivity>> ObtenerTodasLasPhysicalActivitiesAsync()
        {
            string url = $"{_host}/activities";
            HttpResponseMessage response = await _client.GetAsync(url);
            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<List<PhysicalActivity>>(content, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al obtener las Actividades Físicas. Código: {response.StatusCode}", "Error");
                return null;
            }
        }
        // GET / activities / {id}
        public async Task<PhysicalActivity> ObtenerPhysicalActivityPorIdAsync(long id)
        {
            string url = $"{_host}/activities/{id}";
            HttpResponseMessage response = await _client.GetAsync(url);
            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<PhysicalActivity>(content, _serializerOptions);
            }
            else
            {
                return null;
            }
        }
        // GET / activities / user / {userId}
        public async Task<List<PhysicalActivity>> ObtenerPhysicalActivitiesPorUsuarioAsync(long userId)
        {
            string url = $"{_host}/activities/user/{userId}";
            HttpResponseMessage response = await _client.GetAsync(url);
            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<List<PhysicalActivity>>(content, _serializerOptions);
            }
            else if (response.StatusCode == HttpStatusCode.NoContent)
            {
                return new List<PhysicalActivity>();
            }
            else
            {
                MessageBox.Show($"Error al obtener actividades del usuario. Código: {response.StatusCode}", "Error");
                return null;
            }
        }
        // POST / activities / create DTO
        public async Task<PhysicalActivity> CrearPhysicalActivityDTOAsync(PhysicalActivityDTO dto)
        {
            string url = $"{_host}/activities/createDTO";
            string json = JsonSerializer.Serialize(dto, _serializerOptions);
            var content = new StringContent(json, Encoding.UTF8, "application/json");
            HttpResponseMessage response = await _client.PostAsync(url, content);

            if (response.IsSuccessStatusCode)
            {
                string resp = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<PhysicalActivity>(resp, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al crear la actividad física. Código: {response.StatusCode}", "Error");
                return null;
            }
        }
        // PUT / activities / update / {id}
        public async Task<PhysicalActivity> EditarPhysicalActivityAsync(long id, PhysicalActivityDTO updatedActivity)
        {
            string url = $"{_host}/activities/update/{id}";
            string json = JsonSerializer.Serialize(updatedActivity, _serializerOptions);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            HttpResponseMessage response = await _client.PutAsync(url, content);
            if (response.IsSuccessStatusCode)
            {
                string resp = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<PhysicalActivity>(resp, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al editar la actividad física. Código: {response.StatusCode}", "Error");
                return null;
            }
        }
        // DELETE / activities / delete / {id}
        public async Task<bool> EliminarPhysicalActivityAsync(long id)
        {
            string url = $"{_host}/activities/delete/{id}";
            HttpResponseMessage response = await _client.DeleteAsync(url);
            return response.IsSuccessStatusCode;
        }

        // GET / mood
        public async Task<List<Mood>> ObtenerTodosLosMoodsAsync()
        {
            string url = $"{_host}/mood";
            HttpResponseMessage response = await _client.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<List<Mood>>(content, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al obtener los estados de ánimo. Código: {response.StatusCode}");
                return null;
            }
        }
        // GET / mood / {id}
        public async Task<Mood> ObtenerMoodPorIdAsync(long id)
        {
            string url = $"{_host}/mood/{id}";
            HttpResponseMessage response = await _client.GetAsync(url);

            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Mood>(content, _serializerOptions);
            }
            else
            {
                return null;
            }
        }
        // POST / mood / createDTO
        public async Task<Mood> CrearMoodAsync(MoodDTO moodDTO)
        {
            string url = $"{_host}/mood/createDTO";
            string json = JsonSerializer.Serialize(moodDTO, _serializerOptions);
            var content = new StringContent(json, Encoding.UTF8, "application/json");
            HttpResponseMessage response = await _client.PostAsync(url, content);

            if (response.IsSuccessStatusCode)
            {
                string resp = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Mood>(resp, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al crear el estado de ánimo. Código: {response.StatusCode}");
                return null;
            }
        }
        // PUT / mood / updateDTO / {id}
        public async Task<Mood> EditarMoodAsync(long id, MoodDTO moodDTO)
        {
            string url = $"{_host}/mood/updateDTO/{id}";
            string json = JsonSerializer.Serialize(moodDTO, _serializerOptions);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            HttpResponseMessage response = await _client.PutAsync(url, content);

            if (response.IsSuccessStatusCode)
            {
                string resp = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Mood>(resp, _serializerOptions);
            }
            else
            {
                MessageBox.Show($"Error al editar el estado de ánimo. Código: {response.StatusCode}");
                return null;
            }
        }
        // DELETE / mood / delete / {id}
        public async Task<bool> EliminarMoodAsync(long id)
        {
            string url = $"{_host}/mood/delete/{id}";
            HttpResponseMessage response = await _client.DeleteAsync(url);

            return response.IsSuccessStatusCode;
        }



        // Viejo
        public async Task<List<MedicationDTO>> ObtenerMedicamentosAsync(string endpoint)
        {
            try
            {
                string url = $"{_host}{endpoint}";
                HttpResponseMessage response = await _client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string content = await response.Content.ReadAsStringAsync();
                    return JsonSerializer.Deserialize<List<MedicationDTO>>(content, _serializerOptions);
                }
                else
                {
                    MessageBox.Show($"Error al obtener medicamentos. Código: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return null;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }


        public async Task<List<Mood>> ObtenerMoodAsync(string endpoint)
        {
            try
            {
                string url = $"{_host}/{endpoint}";
                HttpResponseMessage response = await _client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string content = await response.Content.ReadAsStringAsync();
                    List<Mood> mood = JsonSerializer.Deserialize<List<Mood>>(content, _serializerOptions);
                    return mood;
                }
                else
                {
                    MessageBox.Show($"Error al obtener los estados mentales. Codigo de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return null;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrio un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        public async Task<List<PhysicalActivity>> ObtenerPhysicalActivitiesAsync(string endpoint)
        {
            try
            {
                string url = $"{_host}/{endpoint}";
                HttpResponseMessage response = await _client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string content = await response.Content.ReadAsStringAsync();
                    List<PhysicalActivity> activities = JsonSerializer.Deserialize<List<PhysicalActivity>>(content, _serializerOptions);
                    return activities;
                }
                else
                {
                    MessageBox.Show($"Error al obtener las Actividades Fisicas. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return null;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }  

        }

        public async Task<List<Appointments>> ObtnerAppointmentsAsync(string endpoint)
        {
            try
            {
                string url = $"{_host}/{endpoint}";
                HttpResponseMessage response = await _client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string content = await response.Content.ReadAsStringAsync();
                    List<Appointments> app = JsonSerializer.Deserialize<List<Appointments>>(content, _serializerOptions);
                    return app;
                }
                else
                {
                    MessageBox.Show($"Error al obtener las Consultas Medicas. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return null;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        public async Task<List<Reminders>> ObtnerListaRemindersAsync(string endpoint)
        {
            try
            {
                string url = $"{_host}/{endpoint}";
                HttpResponseMessage response = await _client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string content = await response.Content.ReadAsStringAsync();
                    List<Reminders> app = JsonSerializer.Deserialize<List<Reminders>>(content, _serializerOptions);
                    return app;
                }
                else
                {
                    MessageBox.Show($"Error al obtener las alarmas. Codigo de estado: {response.StatusCode}", " Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return null;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrio un error: {ex.Message}","Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        public async Task<bool> CrearMedicamentoAsync(MedicationDTO nuevoMedicamento, string endpoint)
        {
            try
            {
                string url = $"{_host}{endpoint}";
                string jsonContent = JsonSerializer.Serialize(nuevoMedicamento, _serializerOptions);
                var content = new StringContent(jsonContent, Encoding.UTF8, "application/json");
                HttpResponseMessage response = await _client.PostAsync(url, content);

                if (response.IsSuccessStatusCode)
                {
                    return true;
                }
                else
                {
                    MessageBox.Show($"Error al crear el medicamento. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return false;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return false;
            }
        }

        public async Task<bool> CrearPhysicalActivityAsync(PhysicalActivityDTO physicalActivityDTO, string endpoint)
        {
            try
            {
                string url = $"{_host}/{endpoint}";
                string jsonContent = JsonSerializer.Serialize(physicalActivityDTO, _serializerOptions);
                var content = new StringContent(jsonContent, Encoding.UTF8, "application/json");
                HttpResponseMessage response = await _client.PostAsync(url, content);

                if (response.IsSuccessStatusCode)
                {
                    return true;
                }
                else
                {
                    MessageBox.Show($"Error al crear la actividad. Codigo de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return false;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrio un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return false;
            }
        }

        public async Task<bool> CrearNuevoMedicamentoAsync(MedicationDTO nuevoMedicamento, string endpoint)
        {
            try
            {
                string url = $"{_host}{endpoint}";
                string json = JsonSerializer.Serialize(nuevoMedicamento, _serializerOptions);
                StringContent content = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await _client.PostAsync(url, content);

                if (response.IsSuccessStatusCode)
                {
                    return true;
                }
                else
                {
                    MessageBox.Show($"Error al crear el medicamento. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return false;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return false;
            }
        }

        public async Task<Users> ObtenerUsuarioPorCorreoAsync(string correo)
        {
            string url = $"/users/correo/{correo}";
            HttpResponseMessage response = await _client.GetAsync(_host + url);

            if (response.IsSuccessStatusCode)
            {
                string content = await response.Content.ReadAsStringAsync();
                Users usuario = JsonSerializer.Deserialize<Users>(content, _serializerOptions);
                return usuario;
            }
            else
            {
                MessageBox.Show($"Error al obtener el usuario. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        public async Task<bool> ActualizarMedicamentoAsync(string endpoint, Medications medicamento)
        {
            try
            {
                string url = $"{_host}{endpoint}/{medicamento.id}";  // Incluye el ID en la URL
                string jsonContent = JsonSerializer.Serialize(medicamento, _serializerOptions);
                HttpContent content = new StringContent(jsonContent, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await _client.PutAsync(url, content);

                if (response.IsSuccessStatusCode)
                {
                    return true;
                }
                else
                {
                    MessageBox.Show($"Error al actualizar el medicamento. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return false;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return false;
            }



        }

        public async Task<T> SendPutRequestAsync<T>(string endpoint, object data)
        {
            try
            {
                var json = JsonSerializer.Serialize(data);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await _client.PutAsync($"{_host}{endpoint}", content);
                if (response.IsSuccessStatusCode)
                {
                    string responseContent = await response.Content.ReadAsStringAsync();
                    return JsonSerializer.Deserialize<T>(responseContent);
                }
                else
                {
                    // Maneja el error adecuadamente según sea necesario.
                    return default(T);
                }
            }
            catch (Exception ex)
            {
                // Maneja el error en caso de fallo en la solicitud
                throw new Exception("Error al realizar la solicitud", ex);
            }
        }
        
        public async Task<List<Users>> ObtenerUsuariosAsync(string endpoint)
        {
            try
            {
                string url = $"{_host}{endpoint}";
                HttpResponseMessage response = await _client.GetAsync(url);

                if (response.IsSuccessStatusCode)
                {
                    string content = await response.Content.ReadAsStringAsync();
                    List<Users> usuarios = JsonSerializer.Deserialize<List<Users>>(content, _serializerOptions);
                    return usuarios;
                }
                else
                {
                    MessageBox.Show($"Error al obtener los usuarios. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return null;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        public async Task<bool> CrearUsuarioAsync(Users usuario, string endpoint)
        {
            try
            {
                string url = $"{_host}{endpoint}";
                string jsonData = JsonSerializer.Serialize(usuario, _serializerOptions);
                HttpContent content = new StringContent(jsonData, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await _client.PostAsync(url, content);

                if (response.IsSuccessStatusCode)
                {
                    return true;
                }
                else
                {
                    MessageBox.Show($"Error al crear el usuario. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return false;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return false;
            }
        }

        public async Task<Users> ActualizarUsuarioAsync(long id, Users usuario)
        {
            try
            {
                string url = $"{_host}/users/{id}";  // URL para la actualización del usuario
                var content = new StringContent(JsonSerializer.Serialize(usuario), Encoding.UTF8, "application/json");

                HttpResponseMessage response = await _client.PutAsync(url, content);

                if (response.IsSuccessStatusCode)
                {
                    string responseContent = await response.Content.ReadAsStringAsync();
                    Users updatedUser = JsonSerializer.Deserialize<Users>(responseContent, _serializerOptions);
                    return updatedUser;  // Devolvemos el usuario actualizado
                }
                else
                {
                    MessageBox.Show($"Error al actualizar el usuario. Código de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return null;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        // Método para eliminar un usuario
        public async Task<bool> EliminarUsuarioAsync(long id)
        {
            try
            {
                string url = $"{_host}/users/{id}";  // URL para la eliminación del usuario
                HttpResponseMessage response = await _client.DeleteAsync(url);

                return response.IsSuccessStatusCode;  // Si la respuesta es exitosa, el usuario fue eliminado
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrió un error: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return false;
            }
        }
        /*
        public async Task<PhysicalActivityDTO> UpdatePhysicalActivityAsync(PhysicalActivityDTO updateActividad, string endpoint)
        {
            try
            {
                string url = $"{_host}/{endpoint}/{updateActividad.id}";
                var content = new StringContent(JsonSerializer.Serialize(updateActividad), Encoding.UTF8, "aplication/json");

                HttpResponseMessage response = await _client.PutAsync(url, content);

                if (response.IsSuccessStatusCode)
                {
                    string responseContent = await response.Content.ReadAsStringAsync();
                    PhysicalActivityDTO updatePhysical = JsonSerializer.Deserialize<PhysicalActivityDTO>(responseContent, _serializerOptions);
                    return updatePhysical;
                }
                else
                {
                    MessageBox.Show($"Error al actualizar la actividad fisica. Codigo de estado: {response.StatusCode}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return null;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ocurrio un error: {ex.Message}", "Error", MessageBoxButtons.OK , MessageBoxIcon.Error);
            }
        }
        */
    }
}
