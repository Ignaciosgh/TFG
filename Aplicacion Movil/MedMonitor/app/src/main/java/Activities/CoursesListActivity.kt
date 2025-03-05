package Activities

import Adapter.CoursesAdapter
import Domain.CoursesDomain
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.medmonitor.R

class CoursesListActivity : AppCompatActivity() {
    private lateinit var adapterCoursesList: CoursesAdapter
    private lateinit var recyclerViewCourses: RecyclerView
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses_list)

        userId = intent.getLongExtra("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initRecyclerView()

        val myImageView: ImageView = findViewById(R.id.imageView)
        myImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        val items = arrayListOf(
            CoursesDomain("Crear Medicamentos", "btn1"),
            CoursesDomain("Crear Consulta Médica", "btn2"),
            CoursesDomain("Registrar Actividad Física", "btn3"),
            CoursesDomain("Registrar Estado de Ánimo", "btn4")
        )

        recyclerViewCourses = findViewById(R.id.view)
        recyclerViewCourses.layoutManager = LinearLayoutManager(this)

        adapterCoursesList = CoursesAdapter(items) { item ->
            when (item.picPath) {
                "btn1" -> {
                    val intent = Intent(this, CreateEditMedicationActivity::class.java).apply {
                        putExtra("userId", userId) // Asegúrate de que 'userId' esté definido y tenga el valor adecuado
                    }
                    startActivity(intent)
                }
                "btn2" -> {
                    val intent = Intent(this, CreateEditAppointmentActivity::class.java).apply {
                        putExtra("userId", userId) // Pasa el mismo 'userId' o cualquier otro dato necesario
                    }
                    startActivity(intent)
                }
                "btn3" -> {
                    val intent = Intent(this, CreateEditPhysicalActivityActivity::class.java).apply {
                        putExtra("userId", userId) // Pasa el 'userId' o el dato requerido
                    }
                    startActivity(intent)
                }
                "btn4" -> {
                    val intent = Intent(this, CreateEditMoodActivity::class.java).apply {
                        putExtra("userId", userId) // Pasa el 'userId' o cualquier dato necesario
                    }
                    startActivity(intent)
                }
                else -> Toast.makeText(this, "Acción no definida", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerViewCourses.adapter = adapterCoursesList
    }
}