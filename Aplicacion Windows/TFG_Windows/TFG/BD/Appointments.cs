using System;
using System.Collections.Generic;
using System.Data.SqlTypes;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TFG.BD
{
    public class Appointments
    {
        public long id {  get; set; }

        public Users usuario { get; set; }

        public string fecha { get; set; }

        public string hora { get; set; }

        public string descripcion {  get; set; }


    }
}
