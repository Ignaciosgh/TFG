using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TFG.Enumerados;

namespace TFG.BD
{
    public class Medications
    {
        public long id {  get; set; }

        public string nombre { get; set; }

        public string descripcion { get; set; }

        public Users usuario { get; set; }

        public Dias dias { get; set; }

        public int comprimidos { get; set; }

        public int veces_al_dia { get; set; }

        public string horas {  get; set; }

        public Estado estado { get; set; }


    }
}
