package com.todolist.to_do_list_tugas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.todolist.to_do_list_tugas.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inisialisasi ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupButtonListener()
        setupInsetsListener()
        setupBottomNavigation()
        fetchTasks()

        // Contoh penggunaan binding
        binding.sampleTextView.text = "Welcome to To-Do List App!" // Update dengan binding
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            taskList,
            onItemClicked = { task ->
                // Navigate to TaskDetailActivity and pass the task ID
                val intent = Intent(this, TaskDetailActivity::class.java).apply {
                    putExtra("TASK_ID", task.id) // Pass the task ID
                }
                startActivity(intent)
            },
            onItemDeleted = { task ->
                deleteTask(task)
            }
        )
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.taskRecyclerView.adapter = taskAdapter
    }

    private fun deleteTask(task: Task) {
        // Contoh token (ambil dari autentikasi pengguna atau penyimpanan lokal)
        val token = "Bearer your-auth-token"

        Log.d("DeleteTask", "Menghapus tugas dengan ID: ${task.id}")
        RetrofitClient.instance.deleteTask(token, task.id).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Tugas berhasil dihapus", Toast.LENGTH_SHORT).show()
                    taskAdapter.removeTask(task) // Hapus dari adapter
                } else {
                    Log.e("DeleteTask", "Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(
                        this@MainActivity,
                        "Gagal menghapus tugas. Kode: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("DeleteTask", "Failure: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Gagal menghapus tugas", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun setupButtonListener() {
        binding.addTaskButton.setOnClickListener {
            val title = binding.inputTask.text.toString()
            if (title.isNotEmpty()) {
                addTask(title)
            } else {
                Toast.makeText(this, "Masukkan tugas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bottomNavigation

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchTasks() {
        RetrofitClient.instance.getTasks().enqueue(object : Callback<List<Task>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    response.body()?.let { tasks ->
                        taskList.clear()
                        taskList.addAll(tasks)
                        taskAdapter.notifyDataSetChanged()
                    } ?: run {
                        Toast.makeText(this@MainActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("FetchTasks", "Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@MainActivity, "Gagal memuat data: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.e("FetchTasks", "Failure: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addTask(title: String) {
        RetrofitClient.instance.addTask(title).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Tugas berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    binding.inputTask.text.clear()
                    fetchTasks()
                } else {
                    Log.e("AddTask", "Response error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@MainActivity, "Gagal menambahkan tugas. Kode: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("AddTask", "Failure: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Gagal menambahkan tugas", Toast.LENGTH_SHORT).show()
            }
        })
    }
}