package Adapter

import Enums.Dia
import Enums.Estado
import Models.Medication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.medmonitor.R

class MedicationsAdapter(
    private var medications: List<Medication>,
    private val onEditClick: (Medication) -> Unit,
    private val onDeleteClick: (Medication) -> Unit,
    private val onUpdateStatusClick: (Medication, Estado) -> Unit
) : RecyclerView.Adapter<MedicationsAdapter.MedicationViewHolder>() {

    inner class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.medicationName) // Campo Nombre
        private val description: TextView = itemView.findViewById(R.id.medicationDescription) // Campo Descripcion
        private val btnOmitido: Button = itemView.findViewById(R.id.markSkippedButton) // Boton Omitido
        private val btnTomado: Button = itemView.findViewById(R.id.markTakenButton) // Boton Tomado
        private val btnEditar: Button = itemView.findViewById(R.id.editButton) // Boton para editar
        private val btnDelete: Button = itemView.findViewById(R.id.deleteButton) // Boton para eliminar
        private val days = mapOf( // Campo dias
            Dia.Lunes to itemView.findViewById<TextView>(R.id.dayMon),
            Dia.Martes to itemView.findViewById<TextView>(R.id.dayTue),
            Dia.Miercoles to itemView.findViewById<TextView>(R.id.dayWed),
            Dia.Jueves to itemView.findViewById<TextView>(R.id.dayThu),
            Dia.Viernes to itemView.findViewById<TextView>(R.id.dayFri),
            Dia.Sabado to itemView.findViewById<TextView>(R.id.daySat),
            Dia.Domingo to itemView.findViewById<TextView>(R.id.daySun)
        )
        private val info: TextView = itemView.findViewById(R.id.medicationInfo) // Campo info
        private val status: TextView = itemView.findViewById(R.id.medicationStatus) // Campo status

        fun bind(medication: Medication) {
            // Asignar los datos del medicamento
            name.text = medication.nombre
            description.text = medication.descripcion

            // Asignar días seleccionados
            days.forEach { (days, view) ->
                if (days == medication.dias) {
                    view.setBackgroundResource(R.drawable.circle_selected) // Cambia el fondo
                    view.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                } else {
                    view.setBackgroundResource(R.drawable.circle_default)
                    view.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray))
                }
            }

            // Información adicional
            info.text = "Comprimidos: ${medication.comprimidos} | Veces al día: ${medication.vecesAlDia} | Horas: ${medication.horas}"

            // Manejar el estado del medicamento (con valor por defecto si es null)
            val medicationStatus = medication.status ?: Estado.pendiente
            status.text = "Estado: ${medicationStatus.toString().lowercase()}"


            // Accion al pulsar el boton editar
            btnEditar.setOnClickListener {
                onEditClick(medication)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(medication)
            }

            btnTomado.setOnClickListener {
                onUpdateStatusClick(medication, Estado.tomado)
            }

            btnOmitido.setOnClickListener {
                onUpdateStatusClick(medication, Estado.omitido)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medications, parent, false)
        return MedicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        holder.bind(medications[position])
    }

    override fun getItemCount(): Int = medications.size

    fun updateList(newMedications: List<Medication>) {
        medications = newMedications
        notifyDataSetChanged()
    }





}