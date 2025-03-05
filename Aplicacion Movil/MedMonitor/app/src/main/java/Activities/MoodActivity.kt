package Activities

import Adapter.MoodAdapter
import Models.Mood
import Network.RetrofitClient
import Repositories.MoodRepository
import ViewModel.MoodViewModel
import ViewModel.MoodViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityMoodBinding

class MoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoodBinding
    private lateinit var moodViewModel: MoodViewModel
    private lateinit var moodAdapter: MoodAdapter
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "Error: Invalid user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        moodAdapter = MoodAdapter(
            moods = emptyList(),
            onEditClick = { mood ->
                val intent = Intent(this, CreateEditMoodActivity::class.java).apply {
                    putExtra("mood", mood)
                    putExtra("userId", userId)
                }
                startActivity(intent)
            },
            onDeleteClick = { mood ->
                showDeleteConfirmationDialog(mood)
            }
        )

        // Configurar RecyclerView
        binding.recyclerViewMood.apply {
            layoutManager = LinearLayoutManager(this@MoodActivity)
            adapter = moodAdapter
        }

        // Inicializar ViewModel
        val repository = MoodRepository(RetrofitClient.apiService)
        val factory = MoodViewModelFactory(repository)
        moodViewModel = ViewModelProvider(this, factory).get(MoodViewModel::class.java)

        loadMoods()


        val addMood: FloatingActionButton = findViewById(R.id.addMoodButton)
        addMood.setOnClickListener {
            val intent = Intent(this, CreateEditMoodActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

    private fun loadMoods() {
        moodViewModel.getMoodsByUser(userId)
        moodViewModel.moods.observe(this) { moods ->
            if (moods.isNotEmpty()) {
                moodAdapter.updateList(moods)
            } else {
                Toast.makeText(this, "No se encontraron estados de animo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(mood: Mood) {
        AlertDialog.Builder(this)
            .setTitle("Eliinar estado de animo")
            .setMessage("Estas seguro de que quieres eliminar el estado de animo")
            .setPositiveButton("Yes") { _, _ -> deleteMood(mood) }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteMood(mood: Mood) {
        moodViewModel.deleteMood(mood.id!!) { success ->
            if (success) {
                Toast.makeText(this, "Estado de animo eliminado correctamente", Toast.LENGTH_SHORT).show()
                loadMoods()
            } else {
                Toast.makeText(this, "Error al eliminar el estado de animo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadMoods()
    }
}