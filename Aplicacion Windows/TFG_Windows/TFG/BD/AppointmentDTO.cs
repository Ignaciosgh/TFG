using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TFG.BD
{
    public class AppointmentDTO
    {
        public long id { get; set; }

        public long usuario_id { get; set; }

        public string fecha { get; set; } // formato "yyyy-MM-dd"

        public string hora { get; set; } // formato "HH:mm"

        public string descripcion { get; set; }
    }
}
