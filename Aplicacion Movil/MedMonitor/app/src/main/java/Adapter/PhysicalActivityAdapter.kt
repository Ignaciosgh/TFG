package Adapter

import Models.PhysicalActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.medmonitor.R

class PhysicalActivityAdapter(
    private var activities: List<PhysicalActivity>,
    private val onEditClick: (PhysicalActivity) -> Unit,
    private val onDeleteClick: (PhysicalActivity) -> Unit
) : RecyclerView.Adapter<PhysicalActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.activityDate)
        val type: TextView = itemView.findViewById(R.id.activityType)
        val duration: TextView = itemView.findViewById(R.id.activityDuration)
        private val editButton: Button = itemView.findViewById(R.id.editButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(activity: PhysicalActivity) {
            date.text = activity.fecha.toString()
            type.text = "Deporte: ${activity.tipo}"
            duration.text = "Duración: ${activity.duracion} minutos"

            editButton.setOnClickListener {onEditClick(activity)}
            deleteButton.setOnClickListener {onDeleteClick(activity)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_actividad_fisica, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size

    fun updateList(newActivities: List<PhysicalActivity>) {
        activities = newActivities
        notifyDataSetChanged()
    }
}
