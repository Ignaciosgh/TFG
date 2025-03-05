using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TFG.Enumerados;

namespace TFG.BD
{
    public class MoodDTO
    {
        public long id {  get; set; }
        public long usuario_id { get; set; }
        public string fecha {  get; set; }
        public string estadoMental { get; set; }
        public string nota { get; set; }

    }
}
