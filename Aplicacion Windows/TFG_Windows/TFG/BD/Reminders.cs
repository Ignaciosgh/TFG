using System;
using System.Collections.Generic;
using System.Data.SqlTypes;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TFG.Enumerados;

namespace TFG.BD
{
    public class Reminders
    {
        public long id {  get; set; }

        public Medications medicamento_id { get; set; }

        public DateTime hora { get; set; }

        public Frecuencia frecuencia { get; set; }
    }
}
