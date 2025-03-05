using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace TFG.Fecha
{
    public class CustomDateConverter : Newtonsoft.Json.JsonConverter
    {
        public override bool CanConvert(Type objectType)
        {
            // Indica que este convertidor se aplica a los objetos DateTime
            return objectType == typeof(DateTime);
        }

        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
            // Escribe la fecha como una cadena con el formato deseado
            var dateTime = (DateTime)value;
            writer.WriteValue(dateTime.ToString("yyyy-MM-dd"));
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {
            // Lee la fecha desde JSON como una cadena y la convierte en DateTime
            var dateStr = reader.Value.ToString();
            return DateTime.ParseExact(dateStr, "yyyy-MM-dd", null);
        }
    }
}
