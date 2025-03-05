package Adapter

import Models.Appointment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.medmonitor.R

class ConsultationsAdapter(
    private var consultations: List<Appointment>,
    private val onEditClick: (Appointment) -> Unit,
    private val onDeleteClick: (Appointment) -> Unit
) : RecyclerView.Adapter<ConsultationsAdapter.ConsultationViewHolder>() {

    inner class ConsultationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.consultaTitle)
        val date: TextView = itemView.findViewById(R.id.consultaDate)
        val motivo: TextView = itemView.findViewById(R.id.consultaMotivo)
        private val deleteButton: Button = itemView.findViewById(R.id.eliminarConsulta)
        private val editButton: Button = itemView.findViewById(R.id.editConsulta)

        fun bind(consultation: Appointment) {
            title.text = "Consulta Medica"
            date.text = "Fecha: ${consultation.fecha.toString()}"
            motivo.text = "Motivo: ${consultation.descripcion}"

            deleteButton.setOnClickListener {onDeleteClick(consultation)}
            editButton.setOnClickListener {onEditClick(consultation)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_consultas_medicas, parent, false)
        return ConsultationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsultationViewHolder, position: Int) {
        holder.bind(consultations[position])
    }

    override fun getItemCount(): Int = consultations.size

    fun updateList(newConsultations: List<Appointment>) {
        consultations = newConsultations
        notifyDataSetChanged()
    }
}
