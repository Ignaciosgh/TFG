using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TFG.Enumerados;

namespace TFG.BD
{
    internal class RemindersDTO
    {
        public long id {  get; set; }

        public long medicamentos_id { get; set; }

        public string hora { get; set; }

        public Frecuencia frecuencia { get; set; }
    }
}
