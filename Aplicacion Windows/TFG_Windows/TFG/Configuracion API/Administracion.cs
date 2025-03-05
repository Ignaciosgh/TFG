using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Remoting.Contexts;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using TFG.BD;
using TFG.Enumerados;

namespace TFG
{
    public class Administracion
    {
        private static WebConsumer consumer;

        static Administracion()
        {
            consumer = new WebConsumer("http://localhost:8089");
        }

        // Mostrar todos los medicamentos
        public static async Task<List<Medications>> ObtenerTodosLosMedicamentosAsync()
        {
            return await consumer.ObtenerTodosLosMedicamentosAsync();
        }
        // Mostrar medicamentos por ID
        public static async Task<Medications> ObtenerMedicamentoPorIdAsync(long id)
        {
            return await consumer.ObtenerMedicamentoPorIdAsync(id);
        }
        // Crear Medicamento
        public static async Task<Medications> CrearMedicamentoAsync(MedicationDTO medicamentoDTO)
        {
            return await consumer.CrearMedicamentoAsync(medicamentoDTO);
        }
        // Editar Medicamento
        public static async Task<Medications> EditarMedicamentoAsync(long id, MedicationDTO medicamentoDTO)
        {
            return await consumer.EditarMedicamentoAsync(id, medicamentoDTO);
        }
        // Eliminar Medicamento
        public static async Task<bool> EliminarMedicamentoAsync(long id)
        {
            return await consumer.EliminarMedicamentoAsync(id);
        }
        // Obtener usuario por ID
        public static async Task<Users> ObtenerUsuarioPorIdAsync(long id)
        {
            return await consumer.ObtenerUsuarioPorIdAsync(id);
        }
        // Obtener todos los appointments
        public static async Task<List<Appointments>> ObtenerTodosLosAppointmentsAsync()
        {
            return await consumer.ObtenerTodosLosAppointmentsAsync();
        }
        // Obtener appointments por ID
        public static async Task<Appointments> ObtenerAppointmentPorIdAsync(long id)
        {
            return await consumer.ObtenerAppointmentPorIdAsync(id);
        }
        // Obtner appointments por usuario
        public static async Task<List<Appointments>> ObtenerAppointmentsPorUsuarioAsync(long userId)
        {
            return await consumer.ObtenerAppointmentsPorUsuarioAsync(userId);
        }
        // Crear Appointment
        public static async Task<Appointments> CrearAppointmentAsync(AppointmentDTO appointmentDTO)
        {
            return await consumer.CrearAppointmentAsync(appointmentDTO);
        }
        // Editar Appointment
        public static async Task<Appointments> EditarAppointmentAsync(long id, AppointmentDTO appointmentDTO)
        {
            return await consumer.EditarAppointmentAsync(id, appointmentDTO);
        }
        // Eliminar Appointment
        public static async Task<bool> EliminarAppointmentAsync(long id)
        {
            return await consumer.EliminarAppointmentAsync(id);
        }

        // Mostrar todas las actividades fisicas
        public static async Task<List<PhysicalActivity>> ObtenerTodasLasActividadesFisicasAsync()
        {
            return await consumer.ObtenerTodasLasPhysicalActivitiesAsync();
        }
        // Mostrar por ID
        public static async Task<PhysicalActivity> ObtenerPhysicalActivityPorIdAsync(long id)
        {
            return await consumer.ObtenerPhysicalActivityPorIdAsync(id);
        }
        // Mostrar por usuario
        public static async Task<List<PhysicalActivity>> ObtenerPhysicalActivitiesPorUsuarioAsync(long usuarioId)
        {
            return await consumer.ObtenerPhysicalActivitiesPorUsuarioAsync(usuarioId);
        }
        // Crear con DTO
        public static async Task<PhysicalActivity> CrearPhysicalActivityAsync(PhysicalActivityDTO dto)
        {
            return await consumer.CrearPhysicalActivityDTOAsync(dto);
        }
        // Editar
        public static async Task<PhysicalActivity> EditarPhysicalActivityAsync(long id, PhysicalActivityDTO updatedActivityDTO)
        {
            return await consumer.EditarPhysicalActivityAsync(id, updatedActivityDTO);
        }
        // Eliminar
        public static async Task<bool> EliminarPhysicalActivityAsync(long id)
        {
            return await consumer.EliminarPhysicalActivityAsync(id);
        }

        // Mostrar todos los mood
        public static async Task<List<Mood>> ObtenerTodosLosMoodsAsync()
        {
            return await consumer.ObtenerTodosLosMoodsAsync();
        }
        // Mostrar por id los mood
        public static async Task<Mood> ObtenerMoodPorIdAsync(long id)
        {
            return await consumer.ObtenerMoodPorIdAsync(id);
        }
        // Crear con DTO el mood
        public static async Task<Mood> CrearMoodAsync(MoodDTO moodDTO)
        {
            return await consumer.CrearMoodAsync(moodDTO);
        }
        // Editar con DTO el mood
        public static async Task<Mood> EditarMoodAsync(long id, MoodDTO moodDTO)
        {
            return await consumer.EditarMoodAsync(id, moodDTO);
        }
        // Eliminar el mood
        public static async Task<bool> EliminarMoodAsync(long id)
        {
            return await consumer.EliminarMoodAsync(id);
        }









        public static async Task<List<Mood>> ObtnerListaMood()
        {
            return await consumer.ObtenerMoodAsync("mood");
        }

        public static async Task<List<PhysicalActivity>> ObtenerListaDePhysicalActivities()
        {
            return await consumer.ObtenerPhysicalActivitiesAsync("activities");
        }


        public static async Task<List<Reminders>> ObtenerListaDeReminders()
        {
            return await consumer.ObtnerListaRemindersAsync("reminders");
        }

        

        

        

        


        public static async Task<bool> EliminarPhysicalActivities(long id)
        {
            return await consumer.EliminarPhysicalActivityAsync(id);
        }

        public static async Task<bool> CrearNuevoUsuario(Users usuario)
        {
            return await consumer.CrearUsuarioAsync(usuario, "/users/create"); 
        }

        public async Task<Users> ActualizarUsuarioAsync(long id, Users usuario)
        {
            // Llamamos a WebConsumer para hacer la solicitud PUT
            return await consumer.ActualizarUsuarioAsync(id, usuario);
        }

        public async Task<bool> EliminarUsuarioAsync(long id)
        {
            // Llamamos a WebConsumer para hacer la solicitud DELETE
            return await consumer.EliminarUsuarioAsync(id);
        }

    }
}
