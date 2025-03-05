using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System;
using System.Collections.Generic;
using System.Data.SqlTypes;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using TFG.Enumerados;
using TFG.Fecha;

namespace TFG.BD
{
    public class PhysicalActivityDTO
    {
        public long id { get; set; }

        public long usuario_id { get; set; }
        public string fecha { get; set; }

        public string tipo { get; set; }

        public int duracion { get; set; }
    }
}
