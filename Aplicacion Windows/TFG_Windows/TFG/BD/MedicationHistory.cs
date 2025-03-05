using System;
using System.Collections.Generic;
using System.Data.SqlTypes;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TFG.Enumerados;

namespace TFG.BD
{
    public class MedicationHistory
    {
        private int id {  get; set; }

        private Medications medicamento_id { get; set; }

        private SqlDateTime fecha {  get; set; }

        private SqlDateTime hora { get; set; }

        private Estado estado { get; set; }
    }
}
