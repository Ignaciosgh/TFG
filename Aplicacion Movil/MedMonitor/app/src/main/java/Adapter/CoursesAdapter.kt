package Adapter

import Domain.CoursesDomain
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.ua.eps.medmonitor.R

class CoursesAdapter(
    private val items: ArrayList<CoursesDomain>, // Lista de datos
    private val listener: (CoursesDomain) -> Unit // Listener para manejar clics
) : RecyclerView.Adapter<CoursesAdapter.ViewHolder>() {

    // Clase interna ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTxt)
        val pic: ImageView = itemView.findViewById(R.id.pic)
        val backgroundImg: ImageView = itemView.findViewById(R.id.background_img)
        val layout: ConstraintLayout = itemView.findViewById(R.id.mail_layout)

        // Asigna datos y configura el clic
        fun bind(item: CoursesDomain, listener: (CoursesDomain) -> Unit) {
            itemView.setOnClickListener { listener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.viewholder_list, parent, false) // Cambiado a R.layout.viewholder_list
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Asignar título
        holder.title.text = item.title

        // Cargar imagen desde los recursos
        val drawableResourceId = holder.itemView.resources.getIdentifier(
            item.picPath, "drawable", holder.itemView.context.packageName
        )
        Glide.with(holder.itemView.context)
            .load(drawableResourceId)
            .into(holder.pic)

        // Configurar fondos e imágenes según la posición
        when (position) {
            0 -> {
                holder.backgroundImg.setImageResource(R.drawable.bg_1)
                holder.layout.setBackgroundResource(R.drawable.list_background_1)
            }
            1 -> {
                holder.backgroundImg.setImageResource(R.drawable.bg_2)
                holder.layout.setBackgroundResource(R.drawable.list_background_2)
            }
            2 -> {
                holder.backgroundImg.setImageResource(R.drawable.bg_3)
                holder.layout.setBackgroundResource(R.drawable.list_background_3)
            }
            3 -> {
                holder.backgroundImg.setImageResource(R.drawable.bg_4)
                holder.layout.setBackgroundResource(R.drawable.list_background_4)
            }
        }

        // Configurar el listener de clic para este elemento
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}