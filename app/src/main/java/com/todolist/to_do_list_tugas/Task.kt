package com.todolist.to_do_list_tugas

data class Task(
    val id: String,
    val title: String,
    val created_at: String,
    val description: String // Pastikan ada properti description
)