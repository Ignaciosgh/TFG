using System;
using TFG.Enumerados;

namespace TFG.BD
{
    public class MedicationDTO
    {
        public long id {  get; set; }
        public string nombre { get; set; }
        public string descripcion { get; set; }
        public long usuario_id { get; set; }  // Solo el ID del usuario
        public Dias dias { get; set; }
        public Estado status { get; set; }
        public int comprimidos { get; set; }
        public int veces_al_dia { get; set; }
        public string horas { get; set; }
    }

}
