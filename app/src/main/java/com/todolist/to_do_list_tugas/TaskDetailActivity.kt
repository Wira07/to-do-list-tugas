package com.todolist.to_do_list_tugas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.todolist.to_do_list_tugas.databinding.ActivityTaskDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding // Binding untuk view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi ViewBinding
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menangani WindowInsets untuk padding sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mengambil task ID dari Intent
        val taskId = intent.getStringExtra("TASK_ID")
        if (taskId != null) {
            fetchTaskDetail(taskId)
        } else {
            Toast.makeText(this, "Task ID tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchTaskDetail(taskId: String) {
        // Menggunakan Retrofit untuk mengambil detail tugas
        RetrofitClient.instance.getTaskDetail(taskId).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful && response.body() != null) {
                    val task = response.body()
                    // Menampilkan detail tugas di layout
                    binding.taskTitle.text = task?.title ?: "Title not available"
                    binding.taskDescription.text = task?.description ?: "Description not available"
                } else {
                    Toast.makeText(this@TaskDetailActivity, "Gagal memuat detail tugas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Toast.makeText(this@TaskDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
