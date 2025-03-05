package Adapter

import Models.Reminder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.medmonitor.R

class ReminderAdapter(private var reminders: List<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        holder.bind(reminder)
    }

    override fun getItemCount(): Int = reminders.size

    fun updateList(newReminders: List<Reminder>) {
        reminders = newReminders
        notifyDataSetChanged()
    }

    inner class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.reminderTitle)
        private val details: TextView = itemView.findViewById(R.id.reminderDetails)

        fun bind(reminder: Reminder) {
            title.text = "Medicamento: ${reminder.medicamentoId.nombre}"
            details.text = "Hora: ${reminder.hora}, Frecuencia: ${reminder.frecuencia}"
        }
    }
}