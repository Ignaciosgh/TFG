using System;
using System.Collections.Generic;
using System.Data.SqlTypes;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using TFG.Enumerados;
using Newtonsoft.Json;


namespace TFG.BD
{
    public class PhysicalActivity
    {
        public long id {  get; set; }

        public Users usuario { get; set; }
        public DateTime fecha {  get; set; }

        public TipoActividad tipo { get; set; }

        public int duracion { get; set; }  
    }
}
