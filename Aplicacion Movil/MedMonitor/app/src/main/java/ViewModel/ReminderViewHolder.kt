package ViewModel

import Models.Reminder
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.medmonitor.R

class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.reminderTitle)
    private val details: TextView = itemView.findViewById(R.id.reminderDetails)

    fun bind(reminder: Reminder) {
        title.text = "Medicamento: ${reminder.medicamentoId.nombre}"
        details.text = "Hora: ${reminder.hora}, Frecuencia: ${reminder.frecuencia}"
    }
}
