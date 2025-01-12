package com.todolist.to_do_list_tugas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todolist.to_do_list_tugas.databinding.ItemTaskBinding
class TaskAdapter(
    private val taskList: MutableList<Task>,
    private val onItemClicked: (Task) -> Unit, // Callback untuk klik pada item
    private val onItemDeleted: (Task) -> Unit // Callback untuk menghapus item
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text = task.title

            // Penanganan klik pada item
            binding.root.setOnClickListener {
                onItemClicked(task)  // Trigger item click action
            }

            // Tombol untuk menghapus item
            binding.deleteButton.setOnClickListener {
                onItemDeleted(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun addTask(task: Task) {
        taskList.add(task)
        notifyItemInserted(taskList.size - 1)
    }

    fun removeTask(task: Task) {
        val position = taskList.indexOf(task)
        if (position >= 0) {
            taskList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
