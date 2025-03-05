package Adapter

import Models.Mood
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.medmonitor.R

class MoodAdapter(
    private var moods: List<Mood>,
    private val onEditClick: (Mood) -> Unit,
    private val onDeleteClick: (Mood) -> Unit
) : RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    inner class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.moodDate)
        val type: TextView = itemView.findViewById(R.id.moodType)
        val notes: TextView = itemView.findViewById(R.id.moodNotes)
        private val editButton: Button = itemView.findViewById(R.id.editMoodButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteMoodButton)

        fun bind(mood: Mood) {
            date.text = mood.fecha.toString()
            type.text = "Estado de Ánimo: ${mood.estado.toString()}"
            notes.text = if (mood.nota.isNotEmpty()) "Notas: ${mood.nota}" else "Sin notas"

            editButton.setOnClickListener {onEditClick(mood)}
            deleteButton.setOnClickListener {onDeleteClick(mood)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estado_animo, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        holder.bind(moods[position])
    }

    override fun getItemCount(): Int = moods.size

    fun updateList(newMoods: List<Mood>) {
        moods = newMoods
        notifyDataSetChanged()
    }
}