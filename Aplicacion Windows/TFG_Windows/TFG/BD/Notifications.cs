using System;
using System.Collections.Generic;
using System.Data.SqlTypes;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TFG.Enumerados;

namespace TFG.BD
{
    public class Notifications
    {
        private int id {  get; set; }

        private Users usuario_id { get; set; }

        private Tipo tipo { get; set; }
        
        private string mensaje { get; set; }

        private SqlDateTime fecha { get; set; } 
    }
}
