using System;
using System.Collections.Generic;
using System.Data.SqlTypes;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TFG.Enumerados;

namespace TFG.BD
{
    public class Mood
    {
        public int id {  get; set; }

        public Users usuario { get; set; }

        public DateTime fecha {  get; set; }

        public EstadoMental estado { get; set; }

        public string nota {  get; set; }

    }
}
